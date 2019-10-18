// Copyright (C) (2019) (Mathieu Bergeron) (mathieu.bergeron@cmontmorency.qc.ca)
//
// This file is part of aquiletour
//
// aquiletour is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// aquiletour is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with aquiletour.  If not, see <https://www.gnu.org/licenses/>


package ca.aquiletour;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.DefaultWebSocketServerFactory;

import com.sun.net.httpserver.*;

import ca.aquiletour.controlers.MainControler;
import ca.aquiletour.controlers.WebSockets;
import ca.aquiletour.http.connectors.DispatchAndTeacher;
import ca.aquiletour.settings.Login;
import ca.aquiletour.settings.Teacher;
import ca.aquiletour.utils.FileVsJar;
import ca.aquiletour.settings.Dispatch;

public class Main{
    
    private static int httpPort = Teacher.getInstance().getTeacherHttpPort();
    
    private static HttpServer httpServer;
    private static WebSockets webSocketServer;

    public static void main(String[] args) throws IOException, KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, CertificateException{
    	
    	installShutdownHook();
		MainControler.initialize();
		initializeHttpServer();
		initializeWebSocketServer();

		new Timer().schedule(new TimerTask() {
			public void run() {
				Login.initialize();
			}
		}, Constants.INITIALIZATION_DELAY);

    }
    
    private static void installShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                shutdown();
            }
        });
    }
    
    private static void shutdown() {
		System.out.println("[Shutting down]");
		System.out.flush();
		
		MainControler.shutdown();

		if(httpServer != null) {
			httpServer.stop(0);
		}
		
		try {

			if(webSocketServer != null) {
				webSocketServer.stop();
			}
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("[Bye]");
		System.out.flush();
		
		Runtime.getRuntime().halt(0);
    }

    private static void initializeHttpServer() throws IOException {
    	
    	try {

			HttpHandler mainHttpHandler = new DispatchAndTeacher();

			httpServer = HttpServer.create(new InetSocketAddress(httpPort),0);

			httpServer.createContext("/", mainHttpHandler);

			httpServer.start();
			
    	}catch(BindException e) {
    		
    		System.out.println(String.format("[FATAL] port %d already used", httpPort));
    		shutdown();
    		
    		
    	}

    }

    private static void initializeWebSocketServer() throws KeyManagementException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, CertificateException, FileNotFoundException, IOException {

			webSocketServer = WebSockets.getInstance();

			int webSocketPort = Teacher.getInstance().getTeacherWsPort();
			
			System.out.println("WebSocket port: " + webSocketPort);
			
			if(isPortUsed(webSocketPort)) {
				
				System.out.println(String.format("[FATAL] port %d already used", webSocketPort));
				shutdown();

			}else {

				initializeSSLWebSocketServer();

			}
    }

    // from: https://github.com/TooTallNate/Java-WebSocket/blob/master/src/main/example/SSLServerExample.java
    private static void initializeSSLWebSocketServer() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
    	
    	/*
    	 * Keystore with certificate created like so (in JKS format):
    	 *
    	 *keytool -genkey -keyalg RSA -validity 3650 -keystore "keystore.jks" -storepass "storepassword" -keypass "keypassword" -alias "default" -dname "CN=127.0.0.1, OU=MyOrgUnit, O=MyOrg, L=MyCity, S=MyRegion, C=MyCountry"
    	 */

			webSocketServer = WebSockets.getInstance();

			String STORETYPE = "JKS";
			String KEYSTORE = "keystore.jks";
			String STOREPASSWORD = "storepassword";
			String KEYPASSWORD = "keypassword";

			KeyStore ks = KeyStore.getInstance( STORETYPE );
			
			Path keyStorePath = Paths.get(Constants.SSL_DIR.toString(), KEYSTORE);
			
			InputStream keyStoreInputStream = FileVsJar.getInputStream(keyStorePath);

			ks.load( keyStoreInputStream, STOREPASSWORD.toCharArray() );

			KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
			kmf.init( ks, KEYPASSWORD.toCharArray() );
			TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
			tmf.init( ks );

			SSLContext sslContext = null;
			sslContext = SSLContext.getInstance( "TLS" );
			sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
			
			webSocketServer.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
			
			webSocketServer.start();

    }

    private static boolean isPortUsed(int port) {
    	
    	boolean isPortUsed = false;

        try {

            new ServerSocket(port).close();

        } catch(BindException e) {
        	
        	isPortUsed = true;
        	
        } catch (IOException e) {}
        
        return isPortUsed;
    }

}

