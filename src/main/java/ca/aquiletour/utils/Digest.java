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


package ca.aquiletour.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Digest {

	private static MessageDigest sha1;

	static {
		try {

			sha1 = MessageDigest.getInstance("SHA-1");

		} catch (NoSuchAlgorithmException e) {
			
			throw new RuntimeException("[FATAL] cannot instantiate MessageDigest(sha1)");
			
		}
	}
	
	public static String digest(String message) {

		sha1.update(message.getBytes());

		return byteToHex(sha1.digest());
	}

	private static String byteToHex(final byte[] hash){
		
		String result;

	    Formatter formatter = new Formatter();

	    for (byte b : hash) {
	        formatter.format("%02x", b);
	    }

	    result = formatter.toString();
	    formatter.close();

	    return result;
	}

}
