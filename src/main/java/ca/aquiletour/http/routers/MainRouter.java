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


package ca.aquiletour.http.routers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import ca.aquiletour.controlers.LoginControler;
import ca.aquiletour.controlers.MainControler;
import ca.aquiletour.data.Student;
import ca.aquiletour.data.Ticket;
import ca.aquiletour.http.path.MainPath;
import ca.aquiletour.http.responses.RedirectResponse;
import ca.aquiletour.http.responses.Response;
import ca.aquiletour.http.responses.TemplateResponse;
import ca.aquiletour.http.responses.ValueResolver;
import ca.aquiletour.settings.Conf;
import ca.aquiletour.settings.Login;
import ca.aquiletour.settings.Pages;

public class MainRouter {

    private static final String OPEN_TICKET = "open_ticket";
    private static final String WHO_ARE_YOU = "who_are_you";
    private static final String USER_TICKET = "user_ticket";
    private static final String TICKET_ALREADY_OPENNED = "ticket_already_openned";
    private static final String DISPLAY_TICKETS = "display_tickets";
    
    public static Response route(Path urlPath, Cookies cookies, String ipAddress,
            String httpUserAgent) {

        Response response = null;
        
        if(MainPath.ifContainsToken(urlPath)) {
        	
        	String authToken = MainPath.getAuthToken(urlPath);

        	cookies.setAuthToken(authToken);
        	
        	Path pathWithoutToken  = MainPath.removeAuthToken(urlPath);
        	
        	response = new RedirectResponse(pathWithoutToken, cookies);
        	
        }else if(userIsAuthenticated(cookies)) {

	    	response = routeAuthenticated(urlPath, cookies, ipAddress, httpUserAgent);

        }else {
        	
        	response = LoginRouter.redirectToRoot(cookies);

        }
        
    	return response;
    }

    public static Response routeAuthenticated(Path urlPath, Cookies cookies, String ipAddress,
            String httpUserAgent) {

        Response builder = null;
        
        if(userWantsToDisplayAllTickets(urlPath) && userIsAllowedToDisplayAllTickets(cookies)) {

        	LoginControler.openQueue();

    		builder = displayAllTickets(cookies);
    		
    	}else if(userWantsToDisplayHerTicket(urlPath)) {
    		
    		builder = routeDisplayUserTicket(cookies, ipAddress, httpUserAgent);

    	}else {

    		builder = routeDefault(urlPath, cookies);

    	}

        return builder;

    }
    
    public static Response authenticateUser(Cookies cookies) {
    	return LoginRouter.redirectToRoot(cookies);
    }

    public static Response routeDisplayUserTicket(Cookies cookies, String ipAddress, String httpUserAgent) {

        Response builder = null;
        
        if(userOwnsAValidTicket(cookies)) {

        	builder = displayTicket(cookies);

        }else if(ticketCreatedInAnotherBrowser(cookies)) {

        	cookies.setTicketId(null);
        	builder = warnTicketCreatedByAnotherBrowser(cookies);

        }else {
        	
        	builder = createAndDisplayTicket(cookies, httpUserAgent);

        }
        
        return builder;

    }

    public static Response routeDefault(Path urlPath, Cookies cookies) {

        Response builder = null;
        
        if(shouldAskStudentName(cookies)) {
        	
        	builder = askStudentName(cookies);

        }else {
        	
        	if(cookies.getTicketId() != null) {
				MainControler.deleteTicket(cookies.getTicketId());
				cookies.setTicketId(null);
        	}
        	
			builder = offerToCreateTicket(cookies);

        }

        return builder;

    }

    private static boolean userIsAuthenticated(Cookies cookies) {
    	
    	boolean userIsAuthenticated = false;

    	String authToken = cookies.getAuthToken();

    	if(authToken != null) {

    		if(authToken.equals(Conf.getInstance().getTeacherToken())) {

    			userIsAuthenticated = true;

    		}else if(authToken.equals(Login.getInstance().getStudentToken())) {

    			userIsAuthenticated = true;

    		}
    	}

    	return userIsAuthenticated;
    }

    private static boolean userWantsToDisplayAllTickets(Path urlPath) {
        return MainPath.isPage(urlPath, Pages.getInstance().getDisplayTickets());
    }

    private static boolean userIsAllowedToDisplayAllTickets(Cookies cookies) {
    	return Conf.getInstance().getTeacherToken().equals(cookies.getAuthToken());
    }

    private static boolean shouldAskStudentName(Cookies cookies) {
        return cookies.getStudentId() == null;
    }

    private static boolean userWantsToDisplayHerTicket(Path urlPath) {
        return MainPath.isPage(urlPath, Pages.getInstance().getMyTicket());
    }


    private static Response displayAllTickets(Cookies cookies) {
    	
        return new TemplateResponse(cookies, DISPLAY_TICKETS, new ValueResolver(null));

    }
    
    private static boolean userOwnsAValidTicket(Cookies cookies) {
        
        boolean userOwnsAValidTicket = false;
        
        String ticketId = cookies.getTicketId();
        
        if(ticketId != null) {
            
            userOwnsAValidTicket = MainControler.ifTicketExists(ticketId);
            
        }
        
        return userOwnsAValidTicket;

    }

    private static boolean ticketCreatedInAnotherBrowser(Cookies cookies) {

    	return MainControler.ifStudentHasTicket(cookies.getStudentId());

    }

    private static Response askStudentName(Cookies cookies) {

        Map<String, String> localValues = new HashMap<>();

        localValues.put("studentById", MainControler.getStudentAsUserById());

        return new TemplateResponse(cookies, WHO_ARE_YOU, new ValueResolver(localValues));
        
    }

    private static Response offerToCreateTicket(Cookies cookies) {
        
        Map<String, String> localValues = new HashMap<>();
        
        String studentId = cookies.getStudentId();
        localValues.putAll(localValuesForStudent(studentId));
        
        return new TemplateResponse(cookies, OPEN_TICKET, new ValueResolver(localValues));

    }


    private static Response displayTicket(Cookies cookies) {

        int position = MainControler.numberOfTickets();

        Map<String, String> localValues = new HashMap<>();
        
        String studentId = cookies.getStudentId();
        localValues.putAll(localValuesForStudent(studentId));

        localValues.put("numInQueue", String.valueOf(position));

        return new TemplateResponse(cookies, USER_TICKET, new ValueResolver(localValues));

    }

    private static Response createAndDisplayTicket(Cookies cookies, String httpUserAgent) {
        
        Student student = MainControler.getStudent(cookies.getStudentId());

        Ticket myTicket = new Ticket(student, httpUserAgent);
        
        cookies.setTicketId(myTicket.getId());

        MainControler.addTicket(myTicket);
        
        return displayTicket(cookies);

    }
    
    private static Map<String, String> localValuesForStudent(String studentId){
        
        Map<String, String> localValues = new HashMap<>();

        Student student = MainControler.getStudent(studentId);
        localValues.put("studentId", studentId);
        localValues.put("studentName", student.getName());
        localValues.put("studentSurname", student.getSurname());
        
        return localValues;
    }

    private static Response warnTicketCreatedByAnotherBrowser(Cookies cookies) {
    	
    	Response builder = null;
        
        String studentId = cookies.getStudentId();
        
        Ticket ticket = MainControler.getTicketByStudentId(studentId);
        
        if(ticket != null) {

			Map<String, String> localValues = new HashMap<>();
			localValues.put("httpUserAgent", ticket.getHttpUserAgent());

            builder = new TemplateResponse(cookies, TICKET_ALREADY_OPENNED, new ValueResolver(localValues));

        }else {

        	builder = offerToCreateTicket(cookies);

        }
        
        return builder;
    }
}
