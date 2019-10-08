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


package ca.aquiletour.settings;

import ca.aquiletour.http.HttpFetchListener;
import ca.aquiletour.http.HttpOut;
import ca.aquiletour.http.path.PrivateDispatchPath;
import ca.aquiletour.http.path.RoutePath;
import ca.aquiletour.utils.Json;

public class Login extends Dictionary {

    protected String loginCode;
    protected String studentToken;

    private static Login instance;

    public static synchronized Login getInstance() {
        return instance;
    }

    private static synchronized void updateInstance(Login instance) {
    	Login.instance = instance;
    }
    
    public static void initialize() {
		requestNewLoginInfo();
    }

    public static void requestNewLoginInfo() {

		String connectionString = String.format("http://localhost:%s/%s/%s/%s", 
													Dispatch.getInstance().getDispatchHttpPort(),
													RoutePath.DISPATCH_PREFIX,
													Teacher.getInstance().getTeacherId(),
													PrivateDispatchPath.ACTION_NEXT_CODE);
		
		HttpOut.fetch(connectionString, new HttpFetchListener() {
			@Override
			public void receiveResponse(String response) {
				Login newInstance = Json.fromJson(response, Login.class);
				updateInstance(newInstance);
				System.out.println("Login initialized");
			}

			@Override
			public void onError() {
				throw new RuntimeException("[FATAL] cannot obtain initial login info");
			}
		});
    }

    
    public Login(String loginCode, String studentToken) {
    	this.loginCode = loginCode;
    	this.studentToken = studentToken;
    }

	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	public String getStudentToken() {
		return studentToken;
	}

	public void setStudentToken(String studentToken) {
		this.studentToken = studentToken;
	}
}
