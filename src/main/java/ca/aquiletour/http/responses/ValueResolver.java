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

import java.util.HashMap;
import java.util.Map;

import ca.aquiletour.settings.Lang;
import ca.aquiletour.settings.Login;
import ca.aquiletour.settings.Pages;
import ca.aquiletour.settings.Dispatch;
import ca.aquiletour.settings.Public;
import ca.aquiletour.settings.Strings;
import ca.aquiletour.settings.Teacher;

public class ValueResolver {

    private Map<String, String> localValues = new HashMap<>();

    public ValueResolver(Map<String, String> localValues) {

        if (localValues == null) {

            this.localValues = new HashMap<String, String>();

        } else {

            this.localValues = localValues;

        }

    }

    public String getString(String key) {
        String value = null;

        if (localValues.containsKey(key)) {

            value = localValues.get(key);

        } else if (Lang.getInstance().hasString(key)) {

            value = Lang.getInstance().getString(key);

        } else if (Teacher.getInstance().hasString(key)) {

            value = Teacher.getInstance().getString(key);

        } else if (Dispatch.getInstance().hasString(key)) {

            value = Dispatch.getInstance().getString(key);

        } else if (Public.getInstance().hasString(key)) {

            value = Public.getInstance().getString(key);

        } else if (Login.getInstance() != null && Login.getInstance().hasString(key)) {

            value = Login.getInstance().getString(key);

        } else if (Pages.getInstance().hasString(key)) {
        	
        	value = Pages.getInstance().getString(key);

        } else if (Strings.getInstance().hasString(key)) {

            value = Strings.getInstance().getString(key);

        }

        return value;
    }

}
