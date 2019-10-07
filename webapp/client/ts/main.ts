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

const userByIdToAutocompleteSource = function(userById){

    let source = []

    for(let userId in userById){
        let name = userById[userId].name + ' ' + userById[userId].surname;
        let item = {value:userId, label:name};
        source.push(item);
    }

    return source;
}

const installAutocomplete = function(inputId, userById, selectionListener){

    const users = userByIdToAutocompleteSource(userById);

    $('#'+inputId).autocomplete({
        source: users,

        focus: function(event, ui){
            event.preventDefault();

            selectionListener(ui.item.value)

            $(this).val(ui.item.label);
        },

        select: function(event, ui){
            event.preventDefault();

            selectionListener(ui.item.value)

            $(this).val(ui.item.label);
        }
    });
}


// 4 months
const maxCookieAge:string = new String(60*60*24*30*4).toString();

const deleteCookie = function(cookieId){
    // @ts-ignore Cookies is from cookies-js
    Cookies.set(cookieId, "", {'Expires':'Sat 1 Jan 2000 00:00:00 GMT'});
}

