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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.aquiletour.Constants;
import ca.aquiletour.settings.Conf;
import ca.aquiletour.utils.Digest;
import ca.aquiletour.utils.Json;

public class Ticket {

	protected String id;
	protected User   studentAsUser;
	protected String httpUserAgent;
	protected String comment;
	
	private static int ticketNum = 0;
	private static String salt = Instant.now().getEpochSecond() + Conf.getInstance().getTeacherId();

	private static final String patternIdFromFileName = String.format("\\w+_\\w+_(\\w+)%s", Constants.JSON_EXTENSION);
    private static final Pattern ID_FROM_FILENAME = Pattern.compile(patternIdFromFileName, Pattern.UNICODE_CHARACTER_CLASS);
    
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
	
	private Path getPath() {
		String filename = String.format("%s_%s_%s%s", studentAsUser.getName(), studentAsUser.getSurname(), id, Constants.JSON_EXTENSION);
        return Paths.get(Constants.TICKETS_DIR.toString(), filename);
	}

	public static String idFromFilename(String filename) {
		String ticketId = null;
		
		Matcher matcher = ID_FROM_FILENAME.matcher(filename);
		
		if(matcher.find()) {
			
			ticketId = matcher.group(1);
			
		}

		return ticketId;
	}
	
	public void writeOnDisk() {
		
		Path ticketPath = getPath();

        try {
        	
        	Json.writeJson(this, ticketPath);

        } catch (IOException e) {

            System.out.println("[WARNING] cannot write ticket file " + ticketPath.toString());
            e.printStackTrace();

        }
	}

	public void deleteOnDisk() {
		Path ticketPath = getPath();
		ticketPath.toFile().delete();
		
	}

	public static Ticket fromFile(Path filePath) throws FileNotFoundException {
	    return Json.fromJson(filePath, Ticket.class);
	}

	public static Ticket fromFile(File file) throws FileNotFoundException {
	    return Json.fromJson(file, Ticket.class);
	}
}
