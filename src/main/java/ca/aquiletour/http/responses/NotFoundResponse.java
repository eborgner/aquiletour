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

import com.sun.net.httpserver.HttpExchange;

import ca.aquiletour.settings.Strings;

public class NotFoundResponse extends Response {

	private final int HTTP_NOT_FOUND = 404;
	private final byte[] errorMessage = Strings.getInstance().getError404().getBytes();

	@Override
	public void send(HttpExchange exchange) throws IOException {
		serveBytes(exchange, errorMessage, HTTP_NOT_FOUND);
	}

}
