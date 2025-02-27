package com.megacitycab.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.megacitycab.model.User;

public class SessionUtils {
	public static User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("loggedInUser");
        }
        return null;
    }
	
	public static boolean isUserLoggedIn(HttpServletRequest request) {
        return getLoggedInUser(request) != null;
    }

    public static void logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
        	session.removeAttribute("loggedInUser");
            session.invalidate();
        }
    }
}
