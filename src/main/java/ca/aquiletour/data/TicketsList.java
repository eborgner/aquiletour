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


package ca.aquiletour.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TicketsList {

    // XXX: LinkedHashMap preserves the ordering so we can iterate tickets in order
    private Map<String, Ticket> ticketById = new LinkedHashMap<>();
    private Map<String, String> ticketIdByStudentId = new HashMap<>();
    
    public synchronized Ticket getTicketById(String ticketId) {
    	return ticketById.get(ticketId);
    }

    public synchronized Ticket getTicketByStudentId(String studentId) {
    	String ticketId = ticketIdByStudentId.get(studentId);
    	return getTicketById(ticketId);
    }
    
    public synchronized void removeById(String ticketId) {
    	Ticket ticket = ticketById.get(ticketId);
    	
    	if(ticket != null) {
    		
    		String studentId = ticket.getStudentAsUser().getId();

    		ticketById.remove(ticketId);
    		ticketIdByStudentId.remove(studentId);
    		
    	}
    }

    public synchronized void removeByStudentId(String studentId) {

    	Ticket ticket = getTicketByStudentId(studentId);

		ticketIdByStudentId.remove(studentId);
    	
    	if(ticket != null) {
    		
    		ticketById.remove(ticket.getId());
    		
    	}
    }

    public synchronized void addTicket(Ticket ticket) {
    	
    	String ticketId = ticket.getId();
    	String studentId = ticket.getStudentAsUser().getId();
    	
    	// XXX: there might already be
    	// a ticket for that student
    	removeByStudentId(studentId);

    	ticketIdByStudentId.put(studentId, ticketId);
    	ticketById.put(ticketId, ticket);
    }

    public synchronized boolean ifTicketExists(String ticketId) {
        return ticketById.containsKey(ticketId);
    }
    
    public synchronized boolean ifStudentHasTicket(String studentId) {
    	return ticketIdByStudentId.containsKey(studentId);
    }

	public synchronized Collection<Ticket> asTicketsCollection() {
		return ticketById.values();
	}

	public synchronized int size() {
		return ticketById.size();
	}
}
