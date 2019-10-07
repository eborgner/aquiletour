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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Cookies {

	private String studentId;
	private String ticketId;
	private String authToken;

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void deleteAll() {

		this.studentId = null;
		this.ticketId = null;
		this.authToken = null;

	}
	
	public void readCookieString(String cookieString) {
		if(cookieString != null && !cookieString.isEmpty()) {
			String[] cookies = cookieString.split("\\s*;\\s*");

			for (String cookie : cookies) {
				this.readCookie(cookie);
			}
		}
	}


	private void readCookie(String cookie) {
		if(cookie != null && !cookie.isEmpty()) {

			String[] parameters = cookie.split(";");
			
				try {

					String[] nameValue = parameters[0].split("=");

					String name = nameValue[0];
					String value = nameValue[1];

					String setterName = setterNameFromFieldName(name);

					try {

						Method setter = this.getClass().getMethod(setterName, String.class);
						setter.invoke(this, value);

					} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {

						System.out.println("[ERROR] cannot save cookie " + cookie);
						e.printStackTrace();
					}

				} catch (IndexOutOfBoundsException e) {

					System.out.println("[ERROR] cannot parse" + cookie);
					e.printStackTrace();

				}
			}
	}

	private String setterNameFromFieldName(String fieldName) {

		String setterName = "set";

		String firstLetter = fieldName.substring(0, 1);
		String remainder = fieldName.substring(1);

		setterName += firstLetter.toUpperCase() + remainder;

		return setterName;

	}

	private String fieldNameFromGetterName(String getterName) {

		String capitalizedFieldName = getterName.replace("get", "");

		String firstLetter = capitalizedFieldName.substring(0, 1);
		String remainder = capitalizedFieldName.substring(1);

		String fieldName = firstLetter.toLowerCase() + remainder;

		return fieldName;

	}

	public List<String> writeCookies() {

		List<String> cookies = new ArrayList<>();

		for (Method method : this.getClass().getMethods()) {

			if (method.getName().startsWith("get")) {

				String fieldName = fieldNameFromGetterName(method.getName());

				try {

					Object value = method.invoke(this);

					if (value instanceof String && value != null) {
						
						cookies.add(String.format("%s=%s; Path=/; Max-Age=%s", fieldName, value, 60 * 60 * 24 * 30 * 4));

					}else if(value == null) {

						cookies.add(String.format("%s=; Path=/; Expires=%s", fieldName,"Sat 1 Jan 2000 00:00:00 GMT"));

					}

				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| ClassCastException e) {

					System.out.println("[WARNING] cannot write cookie" + fieldName);

				}

			}

		}

		return cookies;

	}
}
