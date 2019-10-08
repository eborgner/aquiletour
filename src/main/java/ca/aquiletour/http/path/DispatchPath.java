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


package ca.aquiletour.http.path;

import java.nio.file.Path;
import java.nio.file.Paths;


public class DispatchPath {
	
	private static final int CODE_INDEX = 0;


	public static boolean isValidLoginPath(Path urlPath){
		
		boolean isValidLoginPath = false;

		String loginCode = getLoginCode(urlPath);
		
		if(loginCode != null) {
			
			isValidLoginPath = isLoginCode(loginCode);

		}
		
		return isValidLoginPath;
	}

	public static String getLoginCode(Path urlPath){
		String loginCode = Utils.getName(urlPath, CODE_INDEX);
		return loginCode;
	}
	
	private static boolean isLoginCode(String loginCode) {
		
		boolean isLoginCode = false;
		
		try {
			
			Integer.valueOf(loginCode);
			isLoginCode = true;
			
			
		}catch(Exception e) {}
		
		return isLoginCode;
	}
}
