package com.med.bff;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class MedbffServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService usrService = UserServiceFactory.getUserService();
		User usr = usrService.getCurrentUser();
		if (usr != null) {
			resp.setContentType("text/html");
			resp.getWriter().println("Hello, " + usr.getNickname());
		} else {
			resp.sendRedirect(usrService.createLoginURL(req.getRequestURI()));
		}

	}
}
