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

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import ca.aquiletour.data.Student;
import ca.aquiletour.data.Ticket;
import ca.aquiletour.data.TicketsList;
import ca.aquiletour.data.User;
import ca.aquiletour.filewatcher.FileWatcher;
import ca.aquiletour.filewatcher.OnModifyListener;
import ca.aquiletour.messages.incoming.MsgAddComment;
import ca.aquiletour.settings.Lang;
import ca.aquiletour.utils.Json;

public class MainControler {
	
    private static final Path studentsPath = Paths.get(Lang.getInstance().getStudentsFile());

    private static Map<String, Student> studentById = new HashMap<>();
    private static Map<String, Student> studentByAuthToken = new HashMap<>();
    private static Map<String, User> studentAsUserById = new HashMap<>();
    
    private static TicketsList tickets = new TicketsList();

    private static FileWatcher studentsFileWatcher;

    public static void initialize() {
        
        readStudents();
        initializeStudentsFileWatcher();

    }
    
    public static void shutdown() {
    	if(studentsFileWatcher != null) {
			studentsFileWatcher.interrupt();
    	}
    }
    

    private static void readStudents() {

        studentById = readUsers(studentsPath, new TypeToken<Map<String, Student>>(){});
        studentAsUserById = readUsers(studentsPath, new TypeToken<Map<String, User>>(){});
        
        for(Student student : studentById.values()) {
        	studentByAuthToken.put(student.getAuthToken(), student);
        }
    }

    private static <T> T readUsers(Path usersPath, TypeToken<T> typeToken) {
        
        T value = null;

        try {
            
            value = Json.fromJson(usersPath, typeToken);
            
        } catch (FileNotFoundException e) {

            System.out.println("[FATAL] cannot read users list: " + usersPath.toString());
            e.printStackTrace();

        }
        
        return value;

    }
    
    private static void initializeStudentsFileWatcher() {
    	
		studentsFileWatcher = new FileWatcher(studentsPath);
				
		studentsFileWatcher.setOnModifyListener(new OnModifyListener() {
			
			@Override
			public void onModify(String filename) {
				readStudents();
			}

		});

       studentsFileWatcher.start();
    }

    
    public static void addTicket(Ticket ticket) {

        tickets.addTicket(ticket);

        WebSockets.getInstance().displayNewTicket(ticket);

    }

    public static boolean ifTicketExists(String ticketId) {
        return tickets.ifTicketExists(ticketId);
    }

    public static boolean ifStudentHasTicket(String studentId) {
        return tickets.ifStudentHasTicket(studentId);
    }

    public static Collection<Ticket> getTicketsList() {
        return tickets.asTicketsCollection();
    }

    public static String getStudentsList() {

        List<String> usersList = new ArrayList<>();
        
        for(Map.Entry<String, Student> entry : studentById.entrySet()) {
            usersList.add(entry.getValue().toString());
        }

        return Json.toJson(usersList);
    }

    public static String getStudentAsUserById() {
    	return Json.toJson(studentAsUserById);
    }

    public static Student getStudent(String studentId) {
        return studentById.get(studentId);
    }

    public static Ticket getTicket(String ticketId) {
        return tickets.getTicketById(ticketId);
    }

    public static void deleteTicket(String ticketId) {
    	
        Ticket ticketToRemove  = tickets.getTicketById(ticketId);

        tickets.removeById(ticketId);

        if(ticketToRemove != null) {

            notifyToAdjustPosition();

            WebSockets.getInstance().removeDisplayedTicket(ticketToRemove.getId());

        }
    }
    
    private static void notifyToAdjustPosition() {

        int position = 1;
        
        for(Ticket ticket : tickets.asTicketsCollection()) {

            String ticketIdToNotify = ticket.getId();
            WebSockets.getInstance().adjustPosition(ticketIdToNotify, position);
            
            position++;

        }
    }

    public static void addComment(MsgAddComment message) {
        String ticketId = message.getTicketId();
        String comment = message.getComment();
        
        Ticket ticket = tickets.getTicketById(ticketId);
        
        if(!comment.equals(ticket.getComment())) {
			ticket.setComment(comment);
			
			WebSockets.getInstance().updateComment(message);

        }
    }

    public static int numberOfTickets() {
        return tickets.size();
    }

    public static Ticket getTicketByStudentId(String studentId) {
        return tickets.getTicketByStudentId(studentId);
    }

	public static boolean doesStudentTokenExists(String authToken) {
		return studentByAuthToken.containsKey(authToken);
	}
}
