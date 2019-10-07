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

import java.time.Instant;
import ca.aquiletour.settings.Teacher;
import ca.aquiletour.utils.Digest;

public class Ticket {

	protected String id;
	protected User   studentAsUser;
	protected String httpUserAgent;
	protected String comment;
	
	private static int ticketNum = 0;
	private static String salt = Instant.now().getEpochSecond() + Teacher.getInstance().getTeacherId();

	private static String nextTicketId() {
		
		String ticketId = String.format("%d%s", ticketNum, salt);
		
		ticketId = Digest.digest(ticketId);

		ticketNum++;

		return ticketId;
	}

	public Ticket(Ticket ticket) {
		this.id = ticket.getId();
		this.studentAsUser = ticket.getStudentAsUser();
		this.httpUserAgent = ticket.getHttpUserAgent();
		this.comment = ticket.getComment();
	}
	
	public Ticket(Student student, String httpUserAgent) {
		this.id = nextTicketId();
		this.studentAsUser = student;
		this.httpUserAgent = httpUserAgent;
	}
	
	public User getStudentAsUser() {
		return studentAsUser;
	}

	public void setStudentAsUser(Student student) {
		this.studentAsUser = new User(student);
	}

	public String getHttpUserAgent() {
		return httpUserAgent;
	}

	public void setHttpUserAgent(String httpUserAgent) {
		this.httpUserAgent = httpUserAgent;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
