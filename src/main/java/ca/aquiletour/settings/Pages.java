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

public class Pages extends Dictionary {

    protected String displayTickets;
    protected String myTicket;
    protected String ready;
    protected String error;
    protected String login;

    private static Path path = Paths.get(Constants.LANG_DIR.toString(), Lang.getInstance().getLang(), Pages.class.getSimpleName() + Constants.JSON_EXTENSION);

    private static Pages instance;

    public static Pages getInstance() {
        return instance;
    }

    static {
        try {

            instance = Json.fromJson(path, Pages.class);

            instance.failIfSomeFieldIsNull();

        } catch (FileNotFoundException e) {

            throw new RuntimeException("[FATAL] cannot read language file " + path.toString());

        }
    }

    public String getMyTicket() {
        return myTicket;
    }

    public void setMyTicket(String myTicket) {
        this.myTicket = myTicket;
    }

    public String getDisplayTickets() {
        return displayTickets;
    }

    public void setDisplayTickets(String displayTickets) {
        this.displayTickets = displayTickets;
    }

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getReady() {
		return ready;
	}

	public void setReady(String ready) {
		this.ready = ready;
	}
	

}
