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


class MsgRegisterTeacherSocket extends OutgoingMessage {

    authToken:string

    constructor(authToken){
        super();
        this.authToken = authToken;
    }
}

class MsgCloseTicket extends TicketMessage {

    authToken:string

    constructor(authToken, ticketId){
        super(ticketId);
        this.authToken = authToken;
    }
}

function displayTicketList(ticketsList, socket){

    clearTickets();

    for(let i=0; i<ticketsList.length; i++){
        let ticket = ticketsList[i];
        appendTicket(ticket, socket);
    }
}

function recomputePositions(){
    let position = 1;

    $('#tickets-tbody>tr').each(function(i, tr){
        $(tr).find('.position').text(position);
        position++;
     });
}

function clearTickets(){
    $('#tickets-tbody').empty();
}

function appendTicket(ticket, socket){

    let trCollection = $('#tickets-tbody>tr');

                   // @ts-ignore FIXME: how to cast??
    let position = trCollection.size() + 1;

    let ticketHtml = buildTicketHtml(position, ticket);

    let newItem = $('#tickets-tbody').append(ticketHtml);

    $('#' + ticket.id).click(function(){

                            // @ts-ignore
            const authToken = Cookies.get('authToken');

            let msgCloseTicket = new MsgCloseTicket(authToken, ticket.id);
            msgCloseTicket.send(socket);

    });

    recomputePositions();

    newItem.css('opacity','0');
    newItem.animate({opacity:1}, "slow");

}

let currentFontSizePercent = 100;
const fontSizeIncrement = 30;

function decreaseFontSize(){
    if(currentFontSizePercent > fontSizeIncrement){
        currentFontSizePercent -= fontSizeIncrement;
        adjustNameFontSize();
    }
}

function increaseFontSize(){
    currentFontSizePercent += fontSizeIncrement;
    adjustNameFontSize();
}

function adjustNameFontSize(){
    $('.name').css('font-size', currentFontSizePercent + '%');
}

function buildTicketHtml(position, ticket){

    let student = ticket.studentAsUser;

    let positionHtml='<td class="position">' + position + '</td>';
    let studentHtml='<td class="name" style="font-size:' + currentFontSizePercent + '%">'  + student.name + ' ' + student.surname + '</td>';
    let commentHtml;
    if(ticket.comment){
        commentHtml='<td class="comment">' + ticket.comment + '</td>';
    }else{
        commentHtml='<td class="comment">&nbsp;</td>';
    }

    let rowHtml='<tr id="' + ticket.id + '">';
    rowHtml+=positionHtml;
    rowHtml+=studentHtml;
    rowHtml+=commentHtml;
    rowHtml+='</tr>';

    return rowHtml;

}

function displayComment(ticketId, comment){
    let commentTd = $('#'+ticketId).children('.comment');
    commentTd.css('opacity', '0');
    commentTd.text(comment);
    commentTd.animate({opacity:1});
}

function modifyTicket(ticket){

    let ticketId = ticket.id;
    let ticketElm = $('#'+ticketId);

    let position = ticketElm.find('.position').text();

    let ticketHtml = buildTicketHtml(position, ticket);

    ticketElm.replaceWith(ticketHtml);

}

function removeTicket(studentId){

    let trToRemove = null;

    $('#tickets-tbody>tr').each(function(i, tr){
            if(tr.id == studentId){
                trToRemove = tr;
            }
    });

    $(trToRemove).animate({opacity:0}, 'slow', function(){
        $(trToRemove).remove();
        recomputePositions();
    });
}


function onKeyPress(e){
    let keyPressed = e.which;

    let minus = 45;
    let equal = 61;
    let plus = 43;

    if(keyPressed == minus){

        decreaseFontSize();

    }else if(keyPressed == equal || keyPressed == plus){

        increaseFontSize();

    }
}


$(document).ready(function(){

    $(document).keypress(function(e){
            onKeyPress(e);
    });

    const connectionString = $('#connection-string').val().toString();

    function onOpen(webSocket:WebSocket){

                              // @ts-ignore
            const authToken = Cookies.get('authToken');

            const msgRegisterMessage = new MsgRegisterTeacherSocket(authToken);
            msgRegisterMessage.send(webSocket);
    }

    function onMessage(webSocket:WebSocket, message){
            if(message._type == "MsgDisplayTicketsList"){
                displayTicketList(message.ticketsList, webSocket);
            }

            if(message._type == "MsgDisplayNewTicket"){
                appendTicket(message.ticket, webSocket);
                notifyNewTicket(message.ticket);
            }

            else if(message._type == "MsgDisplayComment"){
                displayComment(message.ticketId, message.comment);
                notifyNewComment(message.comment);
            }

            else if(message._type == "MsgRemoveDisplayedTicket"){
                console.log(message.studentId);
                removeTicket(message.studentId);
            }
    }

    openSocket(connectionString, onOpen, onMessage);


});
