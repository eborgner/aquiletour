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


package ca.aquiletour.http.routers;

import java.nio.file.Path;
import java.nio.file.Paths;

import ca.aquiletour.Constants;
import ca.aquiletour.http.responses.NotFoundResponse;
import ca.aquiletour.http.responses.Response;
import ca.aquiletour.http.responses.StaticResponse;
import ca.aquiletour.utils.FileVsJar;

public class StaticRouter {
	
	public static Response route(Path urlPath) {
		
		Response response = null;

		Path filePath = Paths.get(Constants.CLIENT_DIR.toString(), urlPath.toString());
		
		if(FileVsJar.ifExists(filePath)) {

			response = new StaticResponse(filePath);
			
		}else {
			
			response = new NotFoundResponse();
			
		}

		return response;

	}

}
