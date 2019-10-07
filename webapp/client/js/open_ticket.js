var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
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
var OutgoingMessage = /** @class */ (function () {
    function OutgoingMessage() {
        // @ts-ignore Seems to work fine on recent browsers
        this._type = this.constructor.name;
    }
    OutgoingMessage.prototype.send = function (socket) {
        socket.send(JSON.stringify(this));
    };
    return OutgoingMessage;
}());
var TicketMessage = /** @class */ (function (_super) {
    __extends(TicketMessage, _super);
    function TicketMessage(ticketId) {
        var _this = _super.call(this) || this;
        _this.ticketId = ticketId;
        return _this;
    }
    return TicketMessage;
}(OutgoingMessage));
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
