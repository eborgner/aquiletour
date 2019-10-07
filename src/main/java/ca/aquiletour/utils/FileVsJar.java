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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import ca.aquiletour.Constants;
import ca.aquiletour.Main;

public class FileVsJar {

	public static boolean ifExists(Path path) {
		
		boolean ifExists = false;
		
		try {
			
			if(getInputStream(path) != null) {
				ifExists = true;
			}
			
		} catch(FileNotFoundException e) {}
		
		return ifExists;

	}
	
	public static InputStream getInputStream(Path path) throws FileNotFoundException {
		
		InputStream inputStream = null;

		try {
			
			inputStream = getInputStreamFromFile(path);
			
		}catch(FileNotFoundException e) {
			
			inputStream = getInputStreamFromJar(path);
			
		}

		return inputStream;
	}
	
	private static InputStream getInputStreamFromFile(Path path) throws FileNotFoundException {
		
		InputStream inputStream = null;
		
		Path filePath = Paths.get(Constants.ROOT_DIR.toString(), path.toString());
		
		File file = filePath.toFile();
		
		inputStream = new FileInputStream(file);
		
		return inputStream;

	}

	private static InputStream getInputStreamFromJar(Path path) throws FileNotFoundException {

		InputStream inputStream = null;
		
		Path jarPath = Paths.get("/", path.toString());

		inputStream = Main.class.getResourceAsStream(jarPath.toString());
		
		if(inputStream == null) {
			
			throw new FileNotFoundException("[ERROR] path not found in .jar: " + path.toString());
			
		}

		return inputStream;

	}
}
