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

class MsgRegisterTicketSocket extends TicketMessage {}

class MsgCloseTicket extends TicketMessage {}

class MsgAddComment extends TicketMessage {

    comment:string

    constructor(ticketId, comment){
        super(ticketId);
        this.comment = comment;
    }
}

function adjustPosition(position){

    let spanNumInQueue = $('#num-in-queue');
    spanNumInQueue.text(position);

    let spanNumInQueueMessage = $('#num-in-queue-msg');
    spanNumInQueueMessage.css('border-color', 'red');
    spanNumInQueueMessage.animate({'border-color':'transparent'}, 'slow');

}

function closeTicket(locationOnClose){

    window.location = locationOnClose;

}


$(document).ready(function(){

    const connectionString:string = $('#connection-string').val().toString();
    const locationOnClose:string = "/" + $('#teacher-id').val();

    function onOpen(webSocket:WebSocket){
            var msgRregister = new MsgRegisterTicketSocket(ticketId);
            msgRregister.send(webSocket);

            $('#button-close-ticket').click(function(){

                var msgCloseTicket = new MsgCloseTicket(ticketId);
                msgCloseTicket.send(webSocket);

                closeTicket(locationOnClose);

            });

            $('#button-add-comment').click(function(){

                const commentElm = $('#comment');

                const comment = $(commentElm).val()

                const msgAddComment = new MsgAddComment(ticketId, comment);
                msgAddComment.send(webSocket);

                $(this).prop('disabled', true);
                $(this).animate({'opacity':'0.5'}, 'slow', function(){
                        $(this).animate({'opacity':'1'}, 'slow', function(){
                            $(this).prop('disabled', false);
                        });
                });

                $(commentElm).prop('disabled', true);
                $(commentElm).animate({'opacity':'0.5'}, 'slow', function(){
                    $(commentElm).animate({'opacity':'1'}, 'slow', function(){
                            $(this).prop('disabled', false);
                    });
                });
            });
    }

    function onMessage(webSocket:WebSocket, message){
            console.log(message);

            if(message._type == "MsgAdjustPosition"){
                adjustPosition(message.position);

            }else if(message._type == "MsgRemoveDisplayedTicket"){
                closeTicket(locationOnClose);
            }
    }

    openSocket(connectionString, onOpen, onMessage);

});
