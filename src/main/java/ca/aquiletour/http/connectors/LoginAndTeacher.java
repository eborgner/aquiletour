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


package ca.aquiletour.http.connectors;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ca.aquiletour.http.path.MainPath;
import ca.aquiletour.http.path.RoutePath;
import ca.aquiletour.http.responses.Response;
import ca.aquiletour.http.routers.Cookies;
import ca.aquiletour.http.routers.LoginRouter;
import ca.aquiletour.http.routers.MainRouter;
import ca.aquiletour.http.routers.PrivateRouter;
import ca.aquiletour.http.routers.StaticRouter;
import ca.aquiletour.settings.Conf;

public class LoginAndTeacher implements HttpHandler {



	@Override
	public void handle(HttpExchange exchange) throws IOException {

		Path urlPath = Paths.get(exchange.getRequestURI().getPath());

		try {

			Response response;
			
			if(RoutePath.isStatic(urlPath)) {
				
				response = routeStatic(exchange, urlPath);

			}else {

				response = routeDynamic(exchange, urlPath);
			}
			
			response.send(exchange);

			exchange.close();

		} catch (IOException e) {

			System.out.println("[IOException] on urlPath " + urlPath + ": "  + e.getMessage());

		} catch (Exception e) {

			System.out.println("[Exception] on urlPath " + urlPath);
			e.printStackTrace();

		}
	}

	private boolean isTeacher(Path urlPath) {
		return MainPath.ifTeacherIdEquals(urlPath, Conf.getInstance().getTeacherId());
	}

	private Response routeStatic(HttpExchange exchange, Path urlPath) {

		urlPath = RoutePath.removeTeacherPrefix(urlPath);

		urlPath = RoutePath.removeStaticPrefix(urlPath);

		return StaticRouter.route(urlPath);
		
	}


	private Response routeDynamic(HttpExchange exchange, Path urlPath) {
		
		String ipAddress = getIpAddress(exchange);
		
		if(RoutePath.isPrivate(urlPath, ipAddress)) {
			
			return routePrivateDynamic(exchange, urlPath);
			
		}else {

			return routePublicDynamic(exchange, urlPath, ipAddress);
		}

	}
	
	private Response routePrivateDynamic(HttpExchange exchange, Path urlPath) {
		
		urlPath = RoutePath.removePrivatePrefix(urlPath);
		
		return PrivateRouter.route(urlPath);

	}

	private Response routePublicDynamic(HttpExchange exchange, Path urlPath, String ipAddress) {

		Cookies requestCookies = parseCookies(exchange);
		
		String httpUserAgent = parseUserAgent(exchange);

		if(isTeacher(urlPath)) {

			return MainRouter.route(urlPath, requestCookies, ipAddress, httpUserAgent);
		
		}else {

			return LoginRouter.route(urlPath, requestCookies, ipAddress, httpUserAgent);

		}
	}

	private String getIpAddress(HttpExchange exchange) {
		
		String ipAddress;
		
		List<String> values =  exchange.getRequestHeaders().get("X-Forwarded-For");
		
		if(values != null && values.size() > 0) {

			ipAddress  = values.get(0);

		}else {
			
			ipAddress = exchange.getRemoteAddress().getAddress().getHostAddress();

		}
		
		return ipAddress;
	}
	

	
	private String parseUserAgent(HttpExchange exchange) {
		
		String httpUserAgent = null;
		
		List<String> values =  exchange.getRequestHeaders().get("User-Agent");
		
		if(values != null && values.size() > 0) {
			
			httpUserAgent = values.get(0);
			
		}
		
		// TODO: parse and simplify the User-Agent string
		
		return httpUserAgent;

	}


	private Cookies parseCookies(HttpExchange exchange) {

		Cookies transactionState = new Cookies();

		List<String> cookieStrings = exchange.getRequestHeaders().get("Cookie");

		if (cookieStrings != null) {

			// XXX: it seems cookieStrings always contains ONE string
			// with a ;-separated list of cookies

			for (String cookieString : cookieStrings) {

				transactionState.readCookieString(cookieString);

			}
		}

		return transactionState;
	}



	
}
