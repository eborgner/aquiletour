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

import ca.aquiletour.settings.Teacher;

public class RoutePath {

	private static final String USER_PREFIX = Teacher.getInstance().getTeacherId();
	public static final String STATIC_PREFIX = "__static__";
    public static final String PRIVATE_PREFIX = "__private__";

	public static boolean isStatic(Path urlPath) {
		return isStatic(urlPath, 0) || isStatic(urlPath, 1);
	}
	
	private static boolean isStatic(Path urlPath, int index) {
		return Utils.isNameEqual(urlPath, index, STATIC_PREFIX);
	}

	public static boolean isPrivate(Path urlPath, String ipAddress) {
		return isPrivate(urlPath, 0) && isPrivate(ipAddress);
	}

	private static boolean isPrivate(Path urlPath, int index) {
		return Utils.isNameEqual(urlPath, index, PRIVATE_PREFIX);
	}

	private static boolean isPrivate(String ipAddress) {
		// FIXME: is that always IPv4???
		return ipAddress.equals("127.0.0.1");
	}

	public static Path removeTeacherPrefix(Path urlPath) {
		return removePrefix(urlPath, USER_PREFIX);
	}

	public static Path removeStaticPrefix(Path urlPath) {
		return removePrefix(urlPath, STATIC_PREFIX);
	}

	public static Path removePrivatePrefix(Path urlPath) {
		return removePrefix(urlPath, PRIVATE_PREFIX);
	}

	private static Path removePrefix(Path urlPath, String prefix) {
		if(Utils.isNameEqual(urlPath, 0, prefix)) {
			urlPath = Utils.removeName(urlPath, 0);
		}
		return urlPath;
	}
}
