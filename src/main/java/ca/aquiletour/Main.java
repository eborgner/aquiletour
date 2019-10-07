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

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.net.httpserver.*;

import ca.aquiletour.controlers.MainControler;
import ca.aquiletour.controlers.WebSockets;
import ca.aquiletour.http.connectors.LoginAndTeacher;
import ca.aquiletour.settings.Login;
import ca.aquiletour.settings.Private;

public class Main{
    
    private static int httpPort = Private.getInstance().getPrivateHttpPort();
    
    private static HttpServer httpServer;
    private static WebSockets webSocketServer;

    public static void main(String[] args) throws IOException{
    	
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

			HttpHandler mainHttpHandler = new LoginAndTeacher();

			httpServer = HttpServer.create(new InetSocketAddress(httpPort),0);

			httpServer.createContext("/", mainHttpHandler);

			httpServer.start();
			
    	}catch(BindException e) {
    		
    		System.out.println(String.format("[FATAL] port %d already used", httpPort));
    		shutdown();
    		
    		
    	}

    }

    private static void initializeWebSocketServer() throws UnknownHostException {

			webSocketServer = WebSockets.getInstance();

			int webSocketPort = Private.getInstance().getPrivateWsPort();
			
			if(isPortUsed(webSocketPort)) {
				
				System.out.println(String.format("[FATAL] port %d already used", webSocketPort));
				shutdown();

			}else {

				webSocketServer.start();

			}
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

