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


package ca.aquiletour.settings;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ca.aquiletour.Constants;
import ca.aquiletour.utils.Json;

public class Conf extends Dictionary {

    protected String lang;

    protected String teacherId;
    protected String teacherName;
    protected String teacherSurname;
    protected String teacherToken;
    protected String serverName;

    protected String publicHttpPort;
    protected String publicWsPort;
    protected int privateHttpPort;
    protected int privateWsPort;

    protected String students;
    protected String teachers;

    private static Path path = Paths.get(Constants.CONF_DIR.toString(), Conf.class.getSimpleName() + Constants.JSON_EXTENSION);

    private static Conf instance;

    public static Conf getInstance() {
        return instance;
    }

    static {

        try {

            instance = Json.fromJson(path, Conf.class);
            instance.failIfSomeFieldIsNull();

        } catch (FileNotFoundException e) {
            throw new RuntimeException("[FATAL] cannot read main conf file " + path.toString());
        }

    }

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}


	public String getTeacherToken() {
		return teacherToken;
	}

	public void setTeacherToken(String teacherToken) {
		this.teacherToken = teacherToken;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getPublicWsPort() {
		return publicWsPort;
	}

	public void setPublicWsPort(String publicWsPort) {
		this.publicWsPort = publicWsPort;
	}

	public int getPrivateHttpPort() {
		return privateHttpPort;
	}

	public void setPrivateHttpPort(int privateHttpPort) {
		this.privateHttpPort = privateHttpPort;
	}

	public int getPrivateWsPort() {
		return privateWsPort;
	}

	public void setPrivateWsPort(int privateWsPort) {
		this.privateWsPort = privateWsPort;
	}

	public String getStudents() {
		return students;
	}

	public void setStudents(String students) {
		this.students = students;
	}

	public String getTeachers() {
		return teachers;
	}

	public void setTeachers(String teachers) {
		this.teachers = teachers;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherSurname() {
		return teacherSurname;
	}

	public void setTeacherSurname(String teacherSurname) {
		this.teacherSurname = teacherSurname;
	}

	public String getPublicHttpPort() {
		return publicHttpPort;
	}

	public void setPublicHttpPort(String publicHttpPort) {
		this.publicHttpPort = publicHttpPort;
	}
}
