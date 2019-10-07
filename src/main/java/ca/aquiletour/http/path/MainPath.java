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

import ca.aquiletour.settings.Conf;

public class MainPath {
	
	private static final int TEACHER_ID_INDEX = 0;
	private static final int PAGE_INDEX = 1;
	private static final int AUTH_TOKEN_INDEX = 2;
	

	public static boolean ifTeacherIdEquals(Path urlPath, String teacherId) {
		return teacherId.equals(getTeacherId(urlPath));
	}
	
	public static String getTeacherId(Path urlPath) {
		return Utils.getName(urlPath, TEACHER_ID_INDEX);
	}

    public static boolean isPage(Path urlPath, String page) {
    	return page.equalsIgnoreCase(getPageName(urlPath));
    }

	public static Path setTeacherId(Path urlPath, String teacherId) {
		return Utils.setName(urlPath, TEACHER_ID_INDEX, teacherId);
	}

    public static String getPageName(Path urlPath) {
    	return Utils.getName(urlPath, PAGE_INDEX);
    }

	public static String getAuthToken(Path urlPath) {
		return Utils.getName(urlPath, AUTH_TOKEN_INDEX);
	}

	public static Path removeAuthToken(Path urlPath) {
		return Utils.removeName(urlPath, AUTH_TOKEN_INDEX);
	}

	public static boolean ifContainsToken(Path urlPath) {
		return getAuthToken(urlPath) != null;
	}
}
