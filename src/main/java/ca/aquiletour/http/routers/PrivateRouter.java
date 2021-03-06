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

import ca.aquiletour.controlers.LoginControler;
import ca.aquiletour.http.path.PrivatePath;
import ca.aquiletour.http.responses.JsonResponse;
import ca.aquiletour.http.responses.Response;
import ca.aquiletour.settings.Login;

public class PrivateRouter {

	
    public static Response route(Path urlPath) {

        Response response;
        
        String teacherId = PrivatePath.getTeacherId(urlPath);

        if(PrivatePath.isActionOpen(urlPath)) {
        	
        	LoginControler.open(teacherId);
        	
        	response = JsonResponse.emptyResponse();

        }else if(PrivatePath.isActionNextCode(urlPath)) {

        	String nextCode = LoginControler.nextCode(teacherId);
        	
        	Login login = new Login(nextCode, LoginControler.nextToken(nextCode));
        	
        	response = new JsonResponse(login);

        	
        }else if(PrivatePath.isActionClose(urlPath)) {

        	LoginControler.close(teacherId);

        	response = JsonResponse.emptyResponse();

        }else {

        	response = JsonResponse.emptyResponse();
        	
        }
        return response;
    }
}