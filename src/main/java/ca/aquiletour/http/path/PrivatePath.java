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

public class PrivatePath {

	private static final int TEACHER_ID_INDEX = 0;
	private static final int ACTION_INDEX = 1;
	
	public static final String ACTION_OPEN = "open";
	public static final String ACTION_NEXT_CODE = "next";
	public static final String ACTION_CLOSE = "close";
	
	public static String getTeacherId(Path urlPath) {
		return Utils.getName(urlPath, TEACHER_ID_INDEX);
	}

	public static String getAction(Path urlPath) {
		return Utils.getName(urlPath, ACTION_INDEX);
	}

	public static boolean isActionOpen(Path urlPath) {
		return Utils.isNameEqual(urlPath, ACTION_INDEX, ACTION_OPEN);
	}

	public static boolean isActionClose(Path urlPath) {
		return Utils.isNameEqual(urlPath, ACTION_INDEX, ACTION_CLOSE);
	}

	public static boolean isActionNextCode(Path urlPath) {
		return Utils.isNameEqual(urlPath, ACTION_INDEX, ACTION_NEXT_CODE);
	}
}
