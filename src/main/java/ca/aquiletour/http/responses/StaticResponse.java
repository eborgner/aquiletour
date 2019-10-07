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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;

import com.sun.net.httpserver.HttpExchange;

import ca.aquiletour.utils.FileVsJar;

public class StaticResponse extends Response {

	private final int BUFFER_SIZE = 1024;
	
	private Path filePath;
	
	public StaticResponse(Path filePath) {

		this.filePath = filePath;

	}

	private String getMimeType(Path filePath) {
		
		String mimeType = null;
		
		String filePathString = filePath.toString();

		if(filePathString.endsWith("js")) {
			
			mimeType = "text/javascript; charset=utf8";

		}else if(filePathString.endsWith("css")) {

			mimeType = "text/css; charset=utf8";
			
		}else if(filePathString.endsWith("png")) {

			mimeType = "image/png";
			
		}
		
		return mimeType;

	}
	
	private void serveFile(HttpExchange exchange, Path filePath) throws IOException {

		InputStream inputStream = FileVsJar.getInputStream(filePath);
		
		serveInputStreamAsBytes(exchange, inputStream);

	}

	private void serveInputStreamAsBytes(HttpExchange exchange, InputStream inputStream) throws IOException {

		byte[] buffer = new byte[BUFFER_SIZE];

		byte[] bytes = new byte[0];

		int readSize = 0;

		do {

			readSize = inputStream.read(buffer);

			byte[] newBytes = Arrays.copyOf(bytes, bytes.length + readSize);

			for (int i = 0; i < readSize; i++) {
				newBytes[bytes.length + i] = buffer[i];
			}

			bytes = newBytes;

		} while (readSize > 0);

		serveBytes(exchange, bytes, HTTP_OK);

	}

	@Override
	public void send(HttpExchange exchange) throws IOException {
		
		String mimeType = getMimeType(filePath);
		
		addMimeType(exchange, mimeType);
		
		serveFile(exchange, filePath);
		
	}


}
