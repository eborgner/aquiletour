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

import java.lang.reflect.Field;


public class Dictionary {

    public void failIfSomeFieldIsNull()  {

        Object value = null;

        for(Field field : this.getClass().getDeclaredFields()) {
            try {

                value = field.get(this);

            }catch(ClassCastException | IllegalArgumentException | IllegalAccessException e) {}

            if(value == null){

                throw new RuntimeException(String.format("[FATAL] field %s must have a value in %s", field.getName(), this.getClass().getSimpleName()));

            }

        }
    }

    public boolean hasString(String key) {

        String value = getString(key);
        return value != null;

    }

    public Object getValue(String key) {

        Object value = null;

        for(Field field : this.getClass().getDeclaredFields()) {
            if(field.getName().equals(key)){

                try {

                    value = field.get(this);

                }catch(ClassCastException | IllegalArgumentException | IllegalAccessException e) {}
            }
        }

        return value;
    }

    public String getString(String key) {

        String value = null;

        try {

            value = (String) getValue(key);

        }catch(ClassCastException e) {}


        return value;
    }
}
