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

package ca.aquiletour.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ca.aquiletour.http.responses.Response;

public class HttpOut {

	public static void send(String connectionString) {
    	Thread sender = new Thread() {
    		@Override
    		public void run() {
				try {
					
					URL url = new URL(connectionString);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");
					con.getResponseCode();

				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	};
    	sender.start();
	}
	
	public static void fetch(String connectionString, HttpFetchListener listener) {
    	Thread fetcher = new Thread() {
    		@Override
    		public void run() {
				try {
					

					URL url = new URL(connectionString);
					HttpURLConnection con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("GET");

					int status = con.getResponseCode();
					
					if(status == Response.HTTP_OK) {
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

						String inputLine;
						StringBuffer content = new StringBuffer();
						while ((inputLine = in.readLine()) != null) {
							content.append(inputLine);
						}
						in.close();
						con.disconnect();
						
						listener.receiveResponse(content.toString());

					}else {
						listener.onError();
					}

				} catch (IOException e) {
					listener.onError();
				}
    		}
    	};
    	
    	fetcher.start();
		
	}

}
