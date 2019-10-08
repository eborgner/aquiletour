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

import ca.aquiletour.controlers.DispatchControler;
import ca.aquiletour.http.path.DispatchPath;
import ca.aquiletour.http.path.Utils;
import ca.aquiletour.http.responses.RedirectResponse;
import ca.aquiletour.http.responses.Response;
import ca.aquiletour.http.responses.TemplateResponse;
import ca.aquiletour.http.responses.ValueResolver;
import ca.aquiletour.settings.Login;

public class DispatchRouter {
	
	private static final String ENTER_CODE = "enter_code";
    
    public static Response route(Path urlPath, Cookies cookies, String ipAddress,
            String httpUserAgent) {

        Response response = redirectToRoot(cookies);
        
        if(Utils.isRoot(urlPath)){

			response = new TemplateResponse(cookies, ENTER_CODE, new ValueResolver(null));

        }else if(DispatchPath.isValidLoginPath(urlPath)) {
        	
        	String loginCode = DispatchPath.getLoginCode(urlPath);

			String teacherId = DispatchControler.teacherIdFromLoginCode(loginCode);
				
			if(teacherId != null) {

				String authToken = DispatchControler.getToken(loginCode);

				cookies.setAuthToken(authToken);

				response = new RedirectResponse(Paths.get(teacherId), cookies);
			}
        }
        
        
        return response;
    }

	public static Response redirectToRoot(Cookies cookies) {
		Response response;
		cookies.deleteAll();
		response = new RedirectResponse(Paths.get(""), cookies);
		return response;
	}
}