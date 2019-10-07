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

public class Utils {

	public static boolean isRoot(Path urlPath){
		return urlPath.getNameCount() == 0;
	}

	public static String getName(Path urlPath, int index) {
		String name = null;
		
		if(urlPath.getNameCount() > index) {
			name = urlPath.getName(index).toString();
		}

		return name;
	}

	public static Path setName(Path urlPath, int index, String value) {
		
		int resultNameCount = urlPath.getNameCount();
		
		if(index >= resultNameCount) {
			resultNameCount = index+1;
		}

		String[] names = new String[resultNameCount];
		
		for(int i = 0; i < resultNameCount; i++) {

			if(i == index) {
				names[i] = value;

			}else if(i < urlPath.getNameCount()) {
				names[i] = urlPath.getName(i).toString();
				
			}else {
				names[i] = "";
				
			}
		}
		
		return toPath(names);
		
	}

	public static Path removeName(Path urlPath, int index) {

		int resultNameCount = urlPath.getNameCount();

		if(index < resultNameCount) {
			resultNameCount--;
		}

		String[] names = new String[resultNameCount];

		for(int i = 0; i < resultNameCount; i++) {
			
			if(i < index) {

				names[i] = urlPath.getName(i).toString();

			}else if(i >= index){

				names[i] = urlPath.getName(i+1).toString();

			}
		}
		
		return toPath(names);

	}
	
	public static Path toPath(String[] names) {
		Path result;

		if(names.length == 0) {

			result = Paths.get("");

		}else if(names.length == 1) {

			result = Paths.get(names[0]);

		}else {

			result = Paths.get(names[0]);
			
			for(int i = 1; i < names.length; i++) {
				result = Paths.get(result.toString(), names[i]);
			}
		}
		
		return result;
		
	}

	public static boolean isNameEqual(Path urlPath, int index, String value) {

		boolean isEqual = false;
		
		String name = getName(urlPath, index);
		
		if(name != null && name.equals(value)) {
			isEqual = true;
		}
		
		return isEqual;
	}

}
