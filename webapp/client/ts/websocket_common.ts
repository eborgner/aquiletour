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

class OutgoingMessage{

    _type:string

    constructor(){
        // @ts-ignore Seems to work fine on recent browsers
        this._type = this.constructor.name;
    }

    send(socket){
        socket.send(JSON.stringify(this));
    }
}

class TicketMessage extends OutgoingMessage {

    ticketId:string

    constructor(ticketId){
        super();
        this.ticketId = ticketId;
    }
}

let reconnectDelayInSeconds = 5;
let stepDelayInSeconds = 1;
let initialDelayInSeconds = 1;

function reconnect(connectionString,
                   onOpen,
                   onMessage){

    let initialTimer = setTimeout(function(){

        clearTimeout(initialTimer);

        $('#reconnecting').css('display', 'block');

        console.log("reconnecting websocket...");
        reconnectAfterDelay(connectionString, onOpen, onMessage, reconnectDelayInSeconds);

    }, initialDelayInSeconds * 1000);

}


function reconnectAfterDelay(connectionString,
                             onOpen,
                             onMessage,
                             delayInSeconds){

    let reconnectTimer = setTimeout(function(){

        clearTimeout(reconnectTimer);

        console.log(delayInSeconds);

        $('#reconnecting-in').text(delayInSeconds);

        if(delayInSeconds > 0){

            let remainingDelay = delayInSeconds - stepDelayInSeconds;
            reconnectAfterDelay(connectionString, onOpen, onMessage, remainingDelay);

        }else{

            $('#reconnecting-in').text(" ");
            openSocket(connectionString, onOpen, onMessage);

        }

    }, stepDelayInSeconds * 1000);

    $('button-retry-now').click(function(){
        clearTimeout(reconnectTimer);
        $('#reconnecting').css('display', 'none');
        openSocket(connectionString, onOpen, onMessage);
    });
}

function openSocket(connectionString,
                    onOpen,
                    onMessage){

    let webSocket = new WebSocket(connectionString);

    webSocket.addEventListener('open',
                function(e){
                    console.log('websocket connected');
                    $('#reconnecting').css('display', 'none');
                    onOpen(webSocket);
            });

    webSocket.addEventListener('message',
                function(e){
                    let message = JSON.parse(e.data);
                    onMessage(webSocket, message);
            });

    webSocket.addEventListener('error',
        function(e){
            webSocket.close();
            return false;
    });

    webSocket.addEventListener('close',
        function(e){
            reconnect(connectionString, onOpen, onMessage);
            return false;
    });
}
