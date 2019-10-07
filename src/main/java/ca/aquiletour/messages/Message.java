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


package ca.aquiletour.messages;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.java_websocket.WebSocket;

import ca.aquiletour.messages.incoming.MsgRegisterTeacherSocket;
import ca.aquiletour.messages.outgoing.MsgAdjustPosition;
import ca.aquiletour.utils.Json;

public abstract class Message {

    private static final Pattern TYPE_PATTERN = Pattern.compile("_type[\"']\\s*:\\s*[\"'](\\w+)[\"']");
    
    protected String _type;
    
    protected Message() {
        this._type = this.getClass().getSimpleName();
    }
    
    public void send(WebSocket webSocket) {
    	webSocket.send(Json.toJson(this));
    }

    public void broadcast(Collection<WebSocket> sockets) {
    	for(WebSocket socket : sockets) {
    		send(socket);
    	}
    }

    
    public static Message fromJson(String json) {
        
        Message message = null;

        Matcher matcher = TYPE_PATTERN.matcher(json);
        
        if(matcher.find()) {
            
            String className = matcher.group(1);
            

            try {
                
                Class<? extends Message> messageClass = findClass(className);
                message = Json.fromJson(json, messageClass);

            } catch (ClassNotFoundException e) {

                throw new RuntimeException("Message class not found: " + className);
            }
            
        }else {

            throw new RuntimeException("Cannot parse message: " + json);

        }

        return message;
    }

    private static Class<? extends Message> findClass(String className) throws ClassNotFoundException{

        Class<? extends Message> messageClass = null;
    
        try {

            messageClass = findIncoming(className);

        } catch (ClassNotFoundException e) {

            messageClass = findOutgoing(className);

        }
        
        return messageClass;
    }


    private static Class<? extends Message> findIncoming(String className) throws ClassNotFoundException{
        return findInSamePackage(className, MsgRegisterTeacherSocket.class);
    }

    private static Class<? extends Message> findOutgoing(String className) throws ClassNotFoundException{
        return findInSamePackage(className, MsgAdjustPosition.class);
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Message> findInSamePackage(String className, Class <? extends Message> classInSamePackage) throws ClassNotFoundException{

        String packageName = classInSamePackage.getPackage().getName().replace("package ", "");
        String quanlifiedClassName = packageName + "."  + className;
        
        Class<? extends Message> messageClass = (Class<? extends Message>) Class.forName(quanlifiedClassName);
        
        return messageClass;

    }
}
