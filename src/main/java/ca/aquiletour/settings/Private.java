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

public class Private extends Dictionary {

    protected int privateHttpPort;
    protected int privateWsPort;

    private static Path path = Paths.get(Constants.CONF_DIR.toString(), Private.class.getSimpleName() + Constants.JSON_EXTENSION);

    private static Private instance;

    public static Private getInstance() {
        return instance;
    }

    static {

        try {

            instance = Json.fromJson(path, Private.class);
            instance.failIfSomeFieldIsNull();

        } catch (FileNotFoundException e) {
            throw new RuntimeException("[FATAL] cannot read main conf file " + path.toString());
        }

    }

	public int getPrivateHttpPort() {
		return privateHttpPort;
	}

	public void setPrivateHttpPort(int privateHttpPort) {
		this.privateHttpPort = privateHttpPort;
	}

	public int getPrivateWsPort() {
		return privateWsPort;
	}

	public void setPrivateWsPort(int privateWsPort) {
		this.privateWsPort = privateWsPort;
	}
    
}

