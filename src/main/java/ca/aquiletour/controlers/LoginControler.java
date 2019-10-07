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


package ca.aquiletour.controlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import ca.aquiletour.http.HttpOut;
import ca.aquiletour.http.path.PrivatePath;
import ca.aquiletour.http.path.RoutePath;
import ca.aquiletour.settings.Login;
import ca.aquiletour.settings.Public;
import ca.aquiletour.settings.Teacher;
import ca.aquiletour.utils.Digest;

public class LoginControler {
	
	private static BiMap<String, String> loginCodeByTeacherId = HashBiMap.create();
	private static Map<String, String> tokenByLoginCode = new HashMap<>();
	private static Set<String> opennedTeachers = new HashSet<>();

	private static final Random seededRandom = new Random(System.currentTimeMillis());

	private static final String token = Digest.digest(Teacher.getInstance().getTeacherId() + String.format("%.10f", seededRandom.nextDouble()));
	
	private static String nextCodeNonUnique() {
		int code = 1 + seededRandom.nextInt(9998);
		return String.format("%04d", code);
	}

	public static String nextCode(String teacherId) {
		String nextCode;
		do {

			nextCode = nextCodeNonUnique();

		}while(loginCodeByTeacherId.inverse().containsKey(nextCode));
		
		loginCodeByTeacherId.put(teacherId, nextCode);

		return nextCode;
	}
	
	public static String nextToken(String loginCode) {
		// TODO?: generate a new token each time?
		//        more secure, but student
		//        will input loginCode each time
		String nextToken = LoginControler.token;
		//String nextToken = Digest.digest(Conf.getInstance().getTeacherId() + String.format("%.10f", seededRandom.nextDouble()));
		
		tokenByLoginCode.put(loginCode, nextToken);

		return nextToken;
		
	}

	public static String getToken(String loginCode) {
		return tokenByLoginCode.get(loginCode);
	}

	public static void open(String teacherId) {
		opennedTeachers.add(teacherId);
	}

	public static void close(String teacherId) {
		opennedTeachers.remove(teacherId);
	}

	public static String teacherIdFromLoginCode(String loginCode) {
		return loginCodeByTeacherId.inverse().get(loginCode);
	}


    public static void openQueue() {
		String connectionString = String.format("http://localhost:%s/%s/%s/%s", 
													Public.getInstance().getPublicHttpPort(),
													RoutePath.PRIVATE_PREFIX,
													Teacher.getInstance().getTeacherId(),
													PrivatePath.ACTION_OPEN);

		HttpOut.send(connectionString);
    }
}
