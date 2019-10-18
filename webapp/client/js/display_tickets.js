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
var reconnectDelayInSeconds = 5;
var stepDelayInSeconds = 1;
var initialDelayInSeconds = 1;
function reconnect(connectionString, onOpen, onMessage) {
    var initialTimer = setTimeout(function () {
        clearTimeout(initialTimer);
        $('#reconnecting').css('display', 'block');
        console.log("reconnecting websocket...");
        reconnectAfterDelay(connectionString, onOpen, onMessage, reconnectDelayInSeconds);
    }, initialDelayInSeconds * 1000);
}
function reconnectAfterDelay(connectionString, onOpen, onMessage, delayInSeconds) {
    var reconnectTimer = setTimeout(function () {
        clearTimeout(reconnectTimer);
        $('#reconnecting-in').text(delayInSeconds);
        if (delayInSeconds > 0) {
            var remainingDelay = delayInSeconds - stepDelayInSeconds;
            reconnectAfterDelay(connectionString, onOpen, onMessage, remainingDelay);
        }
        else {
            $('#reconnecting-in').text(" ");
            openSocket(connectionString, onOpen, onMessage);
        }
    }, stepDelayInSeconds * 1000);
    $('button-retry-now').click(function () {
        clearTimeout(reconnectTimer);
        $('#reconnecting').css('display', 'none');
        openSocket(connectionString, onOpen, onMessage);
    });
}
function openSocket(connectionString, onOpen, onMessage) {
    var webSocket = new WebSocket(connectionString);
    webSocket.addEventListener('open', function (e) {
        console.log('websocket connected');
        $('#reconnecting').css('display', 'none');
        onOpen(webSocket);
    });
    webSocket.addEventListener('message', function (e) {
        var message = JSON.parse(e.data);
        onMessage(webSocket, message);
    });
    webSocket.addEventListener('error', function (e) {
        webSocket.close();
        return false;
    });
    webSocket.addEventListener('close', function (e) {
        reconnect(connectionString, onOpen, onMessage);
        return false;
    });
}
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
function notifyNewComment(message) {
    playNotificationSound();
}
function notifyNewTicket(ticket) {
    displayNotification(ticket);
}
function playNotificationSound() {
    var audioElm = $('#notif').get(0);
    audioElm.play();
}
function displayNotification(ticket) {
    var student = ticket.studentAsUser;
    new Notification(student.name + " " + student.surname);
}
function requestNotificationPermission() {
    Notification.requestPermission(function (permission) {
    });
}
$(document).ready(function (e) {
    requestNotificationPermission();
});
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
/// <reference path="websocket_common.ts"/>
/// <reference path="notifications.ts"/>
var MsgRegisterTeacherSocket = /** @class */ (function (_super) {
    __extends(MsgRegisterTeacherSocket, _super);
    function MsgRegisterTeacherSocket(authToken) {
        var _this = _super.call(this) || this;
        _this.authToken = authToken;
        return _this;
    }
    return MsgRegisterTeacherSocket;
}(OutgoingMessage));
var MsgCloseTicket = /** @class */ (function (_super) {
    __extends(MsgCloseTicket, _super);
    function MsgCloseTicket(authToken, ticketId) {
        var _this = _super.call(this, ticketId) || this;
        _this.authToken = authToken;
        return _this;
    }
    return MsgCloseTicket;
}(TicketMessage));
function displayTicketList(ticketsList, socket) {
    clearTickets();
    for (var i = 0; i < ticketsList.length; i++) {
        var ticket = ticketsList[i];
        appendTicket(ticket, socket);
    }
}
function recomputePositions() {
    var position = 1;
    $('#tickets-tbody>tr').each(function (i, tr) {
        $(tr).find('.position').text(position);
        position++;
    });
}
function clearTickets() {
    $('#tickets-tbody').empty();
}
function appendTicket(ticket, socket) {
    var trCollection = $('#tickets-tbody>tr');
    // @ts-ignore FIXME: how to cast??
    var position = trCollection.size() + 1;
    var ticketHtml = buildTicketHtml(position, ticket);
    var newItem = $('#tickets-tbody').append(ticketHtml);
    $('#' + ticket.id).click(function () {
        // @ts-ignore
        var authToken = Cookies.get('authToken');
        var msgCloseTicket = new MsgCloseTicket(authToken, ticket.id);
        msgCloseTicket.send(socket);
    });
    recomputePositions();
    newItem.css('opacity', '0');
    newItem.animate({ opacity: 1 }, "slow");
}
var currentFontSizePercent = 100;
var fontSizeIncrement = 30;
function decreaseFontSize() {
    if (currentFontSizePercent > fontSizeIncrement) {
        currentFontSizePercent -= fontSizeIncrement;
        adjustNameFontSize();
    }
}
function increaseFontSize() {
    currentFontSizePercent += fontSizeIncrement;
    adjustNameFontSize();
}
function adjustNameFontSize() {
    $('.name').css('font-size', currentFontSizePercent + '%');
}
function buildTicketHtml(position, ticket) {
    var student = ticket.studentAsUser;
    var positionHtml = '<td class="position">' + position + '</td>';
    var studentHtml = '<td class="name" style="font-size:' + currentFontSizePercent + '%">' + student.name + ' ' + student.surname + '</td>';
    var commentHtml;
    if (ticket.comment) {
        commentHtml = '<td class="comment">' + ticket.comment + '</td>';
    }
    else {
        commentHtml = '<td class="comment">&nbsp;</td>';
    }
    var rowHtml = '<tr id="' + ticket.id + '">';
    rowHtml += positionHtml;
    rowHtml += studentHtml;
    rowHtml += commentHtml;
    rowHtml += '</tr>';
    return rowHtml;
}
function displayComment(ticketId, comment) {
    var commentTd = $('#' + ticketId).children('.comment');
    commentTd.css('opacity', '0');
    commentTd.text(comment);
    commentTd.animate({ opacity: 1 });
}
function modifyTicket(ticket) {
    var ticketId = ticket.id;
    var ticketElm = $('#' + ticketId);
    var position = ticketElm.find('.position').text();
    var ticketHtml = buildTicketHtml(position, ticket);
    ticketElm.replaceWith(ticketHtml);
}
function removeTicket(studentId) {
    var trToRemove = null;
    $('#tickets-tbody>tr').each(function (i, tr) {
        if (tr.id == studentId) {
            trToRemove = tr;
        }
    });
    $(trToRemove).animate({ opacity: 0 }, 'slow', function () {
        $(trToRemove).remove();
        recomputePositions();
    });
}
function onKeyPress(e) {
    var keyPressed = e.which;
    var minus = 45;
    var equal = 61;
    var plus = 43;
    if (keyPressed == minus) {
        decreaseFontSize();
    }
    else if (keyPressed == equal || keyPressed == plus) {
        increaseFontSize();
    }
}
$(document).ready(function () {
    $(document).keypress(function (e) {
        onKeyPress(e);
    });
    var connectionString = $('#connection-string').val().toString();
    function onOpen(webSocket) {
        // @ts-ignore
        var authToken = Cookies.get('authToken');
        var msgRegisterMessage = new MsgRegisterTeacherSocket(authToken);
        msgRegisterMessage.send(webSocket);
    }
    function onMessage(webSocket, message) {
        if (message._type == "MsgDisplayTicketsList") {
            displayTicketList(message.ticketsList, webSocket);
        }
        if (message._type == "MsgDisplayNewTicket") {
            appendTicket(message.ticket, webSocket);
            notifyNewTicket(message.ticket);
        }
        else if (message._type == "MsgDisplayComment") {
            displayComment(message.ticketId, message.comment);
            notifyNewComment(message.comment);
        }
        else if (message._type == "MsgRemoveDisplayedTicket") {
            console.log(message.studentId);
            removeTicket(message.studentId);
        }
    }
    openSocket(connectionString, onOpen, onMessage);
});
