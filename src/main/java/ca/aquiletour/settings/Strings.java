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

public class Strings extends Dictionary {

    protected String title;
    protected String whoAreYou;
    protected String itsMe;
    protected String itsNotMe;
    protected String forWhichTeacher;
    protected String enterCode;
    protected String changeTeacher;
    protected String itsOk;
    protected String openTicket;
    protected String closeTicket;
    protected String youAre;
    protected String addComment;
    protected String exampleComment;
    protected String comment;
    protected String student;
    protected String position;
    protected String ticketAlreadyOpenned;
    protected String tryAgain;
    protected String error404;
    protected String redirectionError;
    protected String reconnecting;
    protected String retryNow;

	private static Path path = Paths.get(Constants.LANG_DIR.toString(), Conf.getInstance().getLang(), Strings.class.getSimpleName() + Constants.JSON_EXTENSION);

    private static Strings instance;

    public static Strings getInstance() {
        return instance;
    }

    static {

        try {
            
            instance = Json.fromJson(path, Strings.class);
            instance.failIfSomeFieldIsNull();

        } catch (FileNotFoundException e) {
            throw new RuntimeException("[FATAL] cannot read language file " + path.toString());
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWhoAreYou() {
        return whoAreYou;
    }

    public void setWhoAreYou(String whoAreYou) {
        this.whoAreYou = whoAreYou;
    }

    public String getItsMe() {
        return itsMe;
    }

    public void setItsMe(String itsMe) {
        this.itsMe = itsMe;
    }

    public String getForWhichTeacher() {
        return forWhichTeacher;
    }

    public void setForWhichTeacher(String forWhichTeacher) {
        this.forWhichTeacher = forWhichTeacher;
    }

    public String getItsOk() {
        return itsOk;
    }

    public void setItsOk(String itsOk) {
        this.itsOk = itsOk;
    }

    public String getOpenTicket() {
        return openTicket;
    }

    public void setOpenTicket(String openTicket) {
        this.openTicket = openTicket;
    }

    public String getCloseTicket() {
        return closeTicket;
    }

    public void setCloseTicket(String closeTicket) {
        this.closeTicket = closeTicket;
    }

    public String getYouAre() {
        return youAre;
    }

    public void setYouAre(String youAre) {
        this.youAre = youAre;
    }

    public String getAddComment() {
        return addComment;
    }

    public void setAddComment(String addComment) {
        this.addComment = addComment;
    }

    public String getExampleComment() {
        return exampleComment;
    }

    public void setExampleComment(String exampleComment) {
        this.exampleComment = exampleComment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTicketAlreadyOpenned() {
        return ticketAlreadyOpenned;
    }

    public void setTicketAlreadyOpenned(String ticketAlreadyOpenned) {
        this.ticketAlreadyOpenned = ticketAlreadyOpenned;
    }

    public String getTryAgain() {
		return tryAgain;
	}

	public void setTryAgain(String tryAgain) {
		this.tryAgain = tryAgain;
	}

	public String getError404() {
		return error404;
	}

	public void setError404(String error404) {
		this.error404 = error404;
	}

	public String getRedirectionError() {
		return redirectionError;
	}

	public void setRedirectionError(String redirectionError) {
		this.redirectionError = redirectionError;
	}

	public String getItsNotMe() {
		return itsNotMe;
	}

	public void setItsNotMe(String itsNotMe) {
		this.itsNotMe = itsNotMe;
	}

	public String getChangeTeacher() {
		return changeTeacher;
	}

	public void setChangeTeacher(String changeTeacher) {
		this.changeTeacher = changeTeacher;
	}

	public String getEnterCode() {
		return enterCode;
	}

	public void setEnterCode(String enterCode) {
		this.enterCode = enterCode;
	}

	public String getReconnecting() {
		return reconnecting;
	}

	public void setReconnecting(String reconnecting) {
		this.reconnecting = reconnecting;
	}

	public String getRetryNow() {
		return retryNow;
	}

	public void setRetryNow(String retryNow) {
		this.retryNow = retryNow;
	}
	
}
