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
var userByIdToAutocompleteSource = function (userById) {
    var source = [];
    for (var userId in userById) {
        var name_1 = userById[userId].name + ' ' + userById[userId].surname;
        var item = { value: userId, label: name_1 };
        source.push(item);
    }
    return source;
};
var installAutocomplete = function (inputId, userById, selectionListener) {
    var users = userByIdToAutocompleteSource(userById);
    $('#' + inputId).autocomplete({
        source: users,
        focus: function (event, ui) {
            event.preventDefault();
            selectionListener(ui.item.value);
            $(this).val(ui.item.label);
        },
        select: function (event, ui) {
            event.preventDefault();
            selectionListener(ui.item.value);
            $(this).val(ui.item.label);
        }
    });
};
// 4 months
var maxCookieAge = new String(60 * 60 * 24 * 30 * 4).toString();
var deleteCookie = function (cookieId) {
    // @ts-ignore Cookies is from cookies-js
    Cookies.set(cookieId, "", { 'Expires': 'Sat 1 Jan 2000 00:00:00 GMT' });
};
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
/// <reference path="main.ts"/>
$('#a-not-me').click(function () {
    deleteCookie('studentId');
});
