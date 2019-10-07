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
import java.util.concurrent.BrokenBarrierException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ca.aquiletour.http.path.MainPath;
import ca.aquiletour.http.path.RoutePath;
import ca.aquiletour.http.responses.JsonResponse;
import ca.aquiletour.http.responses.Response;
import ca.aquiletour.http.routers.Cookies;
import ca.aquiletour.http.routers.LoginRouter;
import ca.aquiletour.http.routers.MainRouter;
import ca.aquiletour.http.routers.PrivateRouter;
import ca.aquiletour.http.routers.StaticRouter;
import ca.aquiletour.settings.Login;

public class LoginOnly implements HttpHandler {

	@Override
	public void handle(HttpExchange arg0) throws IOException {
		// TODO
	}
	
}
