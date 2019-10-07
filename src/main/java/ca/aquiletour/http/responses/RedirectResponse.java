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
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;

import ca.aquiletour.http.routers.Cookies;

public class RedirectResponse extends Response {
	
	private final int HTTP_REDIRECT = 302;

	private Path redirectPath;
	private Cookies cookies;
	
	public RedirectResponse(Path redirectPath, Cookies cookies) {
		this.redirectPath = Paths.get("/", redirectPath.toString());
		this.cookies = cookies;
	}

	public Path getRedirectPath() {
		return redirectPath;
	}

	@Override
	public void send(HttpExchange exchange) throws IOException {
		addResponseCookies(exchange, cookies);
		exchange.getResponseHeaders().set("Location", redirectPath.toString());
		exchange.sendResponseHeaders(HTTP_REDIRECT, 0);
	}

	

}
