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


package ca.aquiletour.messages.incoming;

import ca.aquiletour.messages.Message;
import ca.aquiletour.messages.outgoing.MsgDisplayComment;

public class MsgAddComment extends Message {

	protected String ticketId;
	protected String comment;
	
	protected MsgAddComment(String ticketId, String comment) {
		super();
		this.ticketId = ticketId;
		this.comment = comment;
	}
	
	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String studentId) {
		this.ticketId = studentId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public MsgDisplayComment toOutgoing() {
		return new MsgDisplayComment(ticketId,comment);
	}
}
