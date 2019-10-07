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


package ca.aquiletour.http.responses;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;

import ca.aquiletour.Constants;
import ca.aquiletour.http.routers.Cookies;
import ca.aquiletour.utils.FileVsJar;

public class TemplateResponse extends Response {

	private final Pattern VARIABLE_PATTERN = Pattern.compile("\\$(\\w+)");
	private final String BODY_MARKER = "@body";
	
	private Path templatePath = Constants.DEFAULT_TEMPLATE_PATH;
	private Path bodyPath;
	
	private ValueResolver valueResolver;
	
	private byte[] responseBytes;
	private Cookies cookies;
	
	public TemplateResponse(Cookies cookies, String responseBasename, ValueResolver valueResolver) {
		this.cookies = cookies;
		this.valueResolver = valueResolver;

		this.bodyPath = Paths.get(Constants.HTML_DIR.toString(), responseBasename + Constants.HTML_EXTENSION);
		
		buildResponse();

	}

	private void buildResponse() {
		
		try {

		InputStream templateStream = FileVsJar.getInputStream(templatePath);

		InputStreamReader streamReader = new InputStreamReader(templateStream);
		
		BufferedReader bufferedReader = new BufferedReader(streamReader);
		
		StringBuilder responseBuilder = new StringBuilder();
		
		String line = null;
		
		do {
			
			line = bufferedReader.readLine();
			
			if(line != null) {
				if(line.matches("\\s*" + BODY_MARKER + "\\s*$")) {

					line = replaceVariablesInBody();

				}else {
					
					line = replaceVariables(line);
					
				}
				
				responseBuilder.append(line + "\n");
				
			}
			
			
		} while(line != null);

		
			responseBytes = responseBuilder.toString().getBytes();
		
		}catch(IOException e) {
			
			e.printStackTrace();

			responseBytes = "ERROR".getBytes();
			
		}
		
	}
	

	
	private String replaceVariables(String line) {
		
		Matcher matcher = VARIABLE_PATTERN.matcher(line);
		
		while(matcher.find()) {
			
			String stringId = matcher.group(1);
			String variable = "\\$" + stringId;
			
			String stringValue = valueResolver.getString(stringId);
			
			if(stringValue == null) {

				stringValue = String.format("[%s not found]", variable);

			}

			
			
			line = line.replaceAll(variable, stringValue);

		}

		return line;

	}

	private String replaceVariablesInBody() {
		
		String body = "";
		
			try {
				
				InputStream bodyStream = FileVsJar.getInputStream(bodyPath);

				Scanner scanner = new Scanner(bodyStream);
				
				StringBuilder builder = new StringBuilder();
				
				while(scanner.hasNextLine()) {
					
					String line = scanner.nextLine();
					
					line = replaceVariables(line);
					
					builder.append(line + "\n");
					
				}
				
				scanner.close();
				
				
				body = builder.toString();

			} catch (FileNotFoundException e) {
				
				throw new RuntimeException("[FATAL] cannot find body file: " + bodyPath.toString());

			}
			
			return body;
	}

	@Override
	public void send(HttpExchange exchange) throws IOException {

		addResponseCookies(exchange, cookies);

		serveBytes(exchange, responseBytes, HTTP_OK);

	} 
}
