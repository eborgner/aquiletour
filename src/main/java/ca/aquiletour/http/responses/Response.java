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


package ca.aquiletour.http.responses;

import java.io.IOException;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

import ca.aquiletour.http.routers.Cookies;


public abstract class Response {

	public static final int HTTP_OK = 200;
	
	public abstract void send(HttpExchange exchange) throws IOException;

	protected void addResponseCookies(HttpExchange exchange, Cookies cookies) {

		List<String> cookiesList = cookies.writeCookies();

		exchange.getResponseHeaders().put("Set-Cookie", cookiesList);

	}

	protected void serveBytes(HttpExchange exchange, byte[] bytes, int httpCode) throws IOException {

		exchange.sendResponseHeaders(httpCode, bytes.length);

		exchange.getResponseBody().write(bytes);

	}

	protected void addMimeType(HttpExchange exchange, String mimeType) {

		if(mimeType != null) {

			exchange.getResponseHeaders().set("Content-Type", String.format("%s", mimeType));
			
		}
	}
}
