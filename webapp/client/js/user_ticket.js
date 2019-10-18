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
/// <reference path="main.ts"/>
/// <reference path="websocket_common.ts"/>
// @ts-ignore Cookies is from cookies-js
var ticketId = Cookies.get('ticketId');
var MsgRegisterTicketSocket = /** @class */ (function (_super) {
    __extends(MsgRegisterTicketSocket, _super);
    function MsgRegisterTicketSocket() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    return MsgRegisterTicketSocket;
}(TicketMessage));
var MsgCloseTicket = /** @class */ (function (_super) {
    __extends(MsgCloseTicket, _super);
    function MsgCloseTicket() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    return MsgCloseTicket;
}(TicketMessage));
var MsgAddComment = /** @class */ (function (_super) {
    __extends(MsgAddComment, _super);
    function MsgAddComment(ticketId, comment) {
        var _this = _super.call(this, ticketId) || this;
        _this.comment = comment;
        return _this;
    }
    return MsgAddComment;
}(TicketMessage));
function adjustPosition(position) {
    var spanNumInQueue = $('#num-in-queue');
    spanNumInQueue.text(position);
    var spanNumInQueueMessage = $('#num-in-queue-msg');
    spanNumInQueueMessage.css('border-color', 'red');
    spanNumInQueueMessage.animate({ 'border-color': 'transparent' }, 'slow');
}
function closeTicket(locationOnClose) {
    window.location = locationOnClose;
}
$(document).ready(function () {
    var connectionString = $('#connection-string').val().toString();
    var locationOnClose = "/" + $('#teacher-id').val();
    function onOpen(webSocket) {
        var msgRregister = new MsgRegisterTicketSocket(ticketId);
        msgRregister.send(webSocket);
        $('#button-close-ticket').click(function () {
            var msgCloseTicket = new MsgCloseTicket(ticketId);
            msgCloseTicket.send(webSocket);
            closeTicket(locationOnClose);
        });
        $('#button-add-comment').click(function () {
            var commentElm = $('#comment');
            var comment = $(commentElm).val();
            var msgAddComment = new MsgAddComment(ticketId, comment);
            msgAddComment.send(webSocket);
            $(this).prop('disabled', true);
            $(this).animate({ 'opacity': '0.5' }, 'slow', function () {
                $(this).animate({ 'opacity': '1' }, 'slow', function () {
                    $(this).prop('disabled', false);
                });
            });
            $(commentElm).prop('disabled', true);
            $(commentElm).animate({ 'opacity': '0.5' }, 'slow', function () {
                $(commentElm).animate({ 'opacity': '1' }, 'slow', function () {
                    $(this).prop('disabled', false);
                });
            });
        });
    }
    function onMessage(webSocket, message) {
        console.log(message);
        if (message._type == "MsgAdjustPosition") {
            adjustPosition(message.position);
        }
        else if (message._type == "MsgRemoveDisplayedTicket") {
            closeTicket(locationOnClose);
        }
    }
    openSocket(connectionString, onOpen, onMessage);
});
