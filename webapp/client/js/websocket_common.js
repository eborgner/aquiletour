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
        console.log(delayInSeconds);
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
