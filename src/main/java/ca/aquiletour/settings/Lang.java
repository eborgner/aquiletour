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


package ca.aquiletour.settings;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ca.aquiletour.Constants;
import ca.aquiletour.utils.Json;

public class Lang extends Dictionary {

    protected String lang;
    protected String studentsFile;

    private static Path path = Paths.get(Constants.CONF_DIR.toString(), Lang.class.getSimpleName() + Constants.JSON_EXTENSION);

    private static Lang instance;

    public static Lang getInstance() {
        return instance;
    }

    static {

        try {

            instance = Json.fromJson(path, Lang.class);
            instance.failIfSomeFieldIsNull();

        } catch (FileNotFoundException e) {
            throw new RuntimeException("[FATAL] cannot read main conf file " + path.toString());
        }

    }

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getStudentsFile() {
		return studentsFile;
	}

	public void setStudentsFile(String studentsFile) {
		this.studentsFile = studentsFile;
	}
    
}
