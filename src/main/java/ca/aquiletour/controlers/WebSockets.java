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


package ca.aquiletour.controlers;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import ca.aquiletour.messages.Message;
import ca.aquiletour.messages.incoming.MsgAddComment;
import ca.aquiletour.messages.incoming.MsgCloseTicket;
import ca.aquiletour.messages.incoming.MsgRegisterTeacherSocket;
import ca.aquiletour.messages.incoming.MsgRegisterTicketSocket;
import ca.aquiletour.messages.outgoing.MsgDisplayNewTicket;
import ca.aquiletour.messages.outgoing.MsgDisplayTicketsList;
import ca.aquiletour.messages.outgoing.MsgAdjustPosition;
import ca.aquiletour.messages.outgoing.MsgDisplayComment;
import ca.aquiletour.messages.outgoing.MsgRemoveDisplayedTicket;
import ca.aquiletour.Constants;
import ca.aquiletour.data.Ticket;
import ca.aquiletour.settings.Conf;
import ca.aquiletour.settings.Login;

public class WebSockets extends WebSocketServer {
	
	private static WebSockets instance = new WebSockets(Conf.getInstance().getPrivateWsPort());

	public static WebSockets getInstance() {return instance;}
	
	private WebSockets(int port) {
        super( new InetSocketAddress( port ) );
	}
	
	private BiMap<String, WebSocket> ticketSocketsByTicketId = HashBiMap.create();
	private Set<WebSocket> teacherSockets = new HashSet<>();

    @Override
    public void onStart() {
        System.out.println("WebSocket server running");
        setConnectionLostTimeout(0);
    }
    

    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {}
    
    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		closeTicketSocket(conn);
    	
    	if(isPublicSocket(conn)) {

			teacherSockets.remove(conn);

			if(teacherSockets.isEmpty()) {
				new Timer().schedule(new TimerTask() {
					public void run() {
						if(teacherSockets.isEmpty()) {
							Login.requestNewLoginInfo();
						}
					}
				}, Constants.TEACHER_DECONNECTION_DELAY);
			}
    	}
    }
    
    private boolean isPublicSocket(WebSocket conn) {
    	return teacherSockets.contains(conn);
    }
    
    private void closeTicketSocket(WebSocket conn) {
    	ticketSocketsByTicketId.inverse().remove(conn);
    }

    @Override
    public void onMessage( WebSocket conn, String messageJson ) {

    	Message message = Message.fromJson(messageJson);
    	
    	// FIXME: refactor this put behavion inside MsgClass

    	if(message instanceof MsgRegisterTeacherSocket) {
    		onRegisterTeacherSocket((MsgRegisterTeacherSocket) message, conn);

    	}else if(message instanceof MsgRegisterTicketSocket) {
    		onRegisterTicketSocket((MsgRegisterTicketSocket) message, conn);

    	}else if(message instanceof MsgAddComment) {
    		onAddComment((MsgAddComment) message);

    	}else if(message instanceof MsgCloseTicket) {
    		onCloseTicket((MsgCloseTicket) message);
    	}
    }
    
    private void onRegisterTeacherSocket(MsgRegisterTeacherSocket message, WebSocket conn) {
    	if(Conf.getInstance().getTeacherToken().equals(message.getAuthToken())) {
			teacherSockets.add(conn);

			MsgDisplayTicketsList display = new MsgDisplayTicketsList(MainControler.getTicketsList());
			display.send(conn);
    	}
    }

	private void onRegisterTicketSocket(MsgRegisterTicketSocket message, WebSocket conn) {
		String ticketId = message.getTicketId();

		if(MainControler.ifTicketExists(ticketId)) {
			ticketSocketsByTicketId.put(ticketId, conn);
		}
    }

    private void onCloseTicket(MsgCloseTicket message) {
    	if(Conf.getInstance().getTeacherToken().equals(message.getAuthToken())) {
			MainControler.deleteTicket(message.getTicketId());
    	}
    }
    
    private void onAddComment(MsgAddComment message) {

		MainControler.addComment(message);

    }
    
    public void updateComment(MsgAddComment message) {
    	
    	MsgDisplayComment outMessage = message.toOutgoing();
    	
		for(WebSocket socket : teacherSockets) {

			outMessage.send(socket);

		}
    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
    	throw new RuntimeException("WebSocket.onMessage: ByteBuffer not supported");
    }

    @Override
    public void onError( WebSocket conn, Exception ex ) {
        ex.printStackTrace();
        if( conn != null ) {
        	closeTicketSocket(conn);
			teacherSockets.remove(conn);
        }
    }
    
    public void displayNewTicket(Ticket ticket) {

    	MsgDisplayNewTicket message = new MsgDisplayNewTicket(ticket);
    	message.broadcast(teacherSockets);

    }

    public void removeDisplayedTicket(String ticketId) {

    	MsgRemoveDisplayedTicket message = new MsgRemoveDisplayedTicket(ticketId);
    	message.broadcast(teacherSockets);
    	
    	WebSocket ticketSocket = ticketSocketsByTicketId.get(ticketId);
    	if(ticketSocket != null) {
    		message.send(ticketSocket);
    	}
    }
    
    public void adjustPosition(String ticketId, int position) {

		MsgAdjustPosition message = new MsgAdjustPosition(position);
		WebSocket conn = ticketSocketsByTicketId.get(ticketId);

		if(conn != null) {
			message.send(conn);
		}
    }
}