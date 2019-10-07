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


package ca.aquiletour;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ca.aquiletour.settings.Pages;

public class Constants {
	
	public static final long TEACHER_DECONNECTION_DELAY = 2000;
	public static final long INITIALIZATION_DELAY = 1000;

    public static final String JSON_EXTENSION = ".json";

    public static Path ROOT_DIR;
    private static final String WEBAPP_DIRNAME = "webapp";

    private static final String APP_DIRNAME = "aquiletour";
    public static Path APP_DIR;

    private static String SERVER_DIRNAME = "server";
    public static Path SERVER_DIR;

    private static String CONF_DIRNAME = "conf";
    public static Path CONF_DIR;

    private static String CLIENT_DIRNAME = "client";
    public static Path CLIENT_DIR;

    private static String HTML_DIRNAME = "html";
    public static Path HTML_DIR;
    
    private static String LANG_DIRNAME = "lang";
    public static Path LANG_DIR;

    private static String TICKETS_DIRNAME;
    public static Path TICKETS_DIR;

    private static String DEFAULT_TEMPLATE_FILENAME = "default_template.html";
    public static Path DEFAULT_TEMPLATE_PATH;
    
    public static boolean EXECUTING_AS_JAR;
    
    public static final String HTML_EXTENSION = ".html";
    public static final String HTTP_INDEX_FILE = String.format("main%s", HTML_EXTENSION);
    
    static {
        
        initializeRootDirAndInJar();
        initializeDirs();

    }
    
    private static void initializeRootDirAndInJar() {
        

        try {

            String pathToMain = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getCanonicalPath();
            
            if(pathToMain.endsWith(".jar")) {
                
                EXECUTING_AS_JAR = true;

                String rootPath = pathToMain.replaceFirst("/[^/]*jar", "");
                
                ROOT_DIR = Paths.get(rootPath);

            }else {
            	
                EXECUTING_AS_JAR = false;

                String rootPath = pathToMain.replaceFirst(String.format("/%s/.*$", APP_DIRNAME), "");
                
                ROOT_DIR = Paths.get(rootPath, APP_DIRNAME, WEBAPP_DIRNAME);

            }

        } catch (IOException | URISyntaxException e1) {

            throw new RuntimeException("[FATAL] cannot initialize ROOT_DIR");

        } 
    }
    
    private static void initializeDirs() {

    	SERVER_DIR = Paths.get(SERVER_DIRNAME);
    	CONF_DIR = Paths.get(SERVER_DIR.toString(), CONF_DIRNAME);
        LANG_DIR = Paths.get(CONF_DIR.toString(), LANG_DIRNAME);
        CLIENT_DIR = Paths.get(CLIENT_DIRNAME);
        HTML_DIR = Paths.get(CLIENT_DIR.toString(), HTML_DIRNAME);
        DEFAULT_TEMPLATE_PATH = Paths.get(HTML_DIR.toString(), DEFAULT_TEMPLATE_FILENAME);

    }

}
