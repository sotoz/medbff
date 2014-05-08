package com.med.bff;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.Date;

@SuppressWarnings("serial")
public class WallAdd extends HttpServlet {
	@SuppressWarnings("unused")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService usrService = UserServiceFactory.getUserService();
		User usr = usrService.getCurrentUser();

		HtmlHelper htmlhelp = new HtmlHelper();

		if (usr != null) {
			resp.setContentType("text/html");
			String header = htmlhelp.getHeaderHtml(usr.getNickname()
					+ " | <a href=\"" + usrService.createLogoutURL("/")
					+ "\">Logout</a>");
			resp.getWriter().println(header);

			resp.getWriter().print("<form action=\"/add\" method=post>");
			resp.getWriter().print(
					"<p>Name: <input type=\"text\" name=\"personname\" disabled value=\""
							+ usr.getNickname() + "\" /></p>");
			resp.getWriter()
					.print("<p>Wall Post: <input type=\"text\" name=\"wallpost\"></p>");

			resp.getWriter().print("</br>");
			resp.getWriter().print("<input type=\"submit\" value=\"send\">");
			resp.getWriter().print("</form>");
			String footer = htmlhelp.getFooterHtml();
			resp.getWriter().println(footer);
		} else {
			resp.sendRedirect(usrService.createLoginURL(req.getRequestURI()));
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		Key contactKey = KeyFactory.createKey("Person", user.getNickname());
		Date date = new Date();
		Entity contact = new Entity("WallPost", contactKey);
		contact.setProperty("PersonName", user.getNickname());
		contact.setProperty("created", date);
		contact.setProperty("post",
				req.getParameter("wallpost").replaceAll("\\<.*?\\>", ""));

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		datastore.put(contact);

		HtmlHelper htmlhelp = new HtmlHelper();

		// add html header
		String header = htmlhelp.getHeaderHtml(user.getNickname());
		resp.getWriter().println(header);
		resp.getWriter().println("Wall post Created.");
		String footer = htmlhelp.getFooterHtml();
		resp.getWriter().println(footer);
	}
}
