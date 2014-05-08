package com.med.bff;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ListUsers extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query query = new Query("WallPost");
		List<Entity> wallposts = datastore.prepare(query).asList(
				FetchOptions.Builder.withDefaults());
		resp.setContentType("text/html");
		UserService usrService = UserServiceFactory.getUserService();
		User usr = usrService.getCurrentUser();
		HtmlHelper htmlhelp = new HtmlHelper();

		if (usr != null) {
			//test
			// add html header
			String header = htmlhelp.getHeaderHtml(usr.getNickname() +" | <a href=\""+ usrService.createLogoutURL("/")+"\">Logout</a>");
			resp.getWriter().println(header);
			resp.getWriter().println("Users that have posted on the wall");
			ArrayList<String> usersThatPosted = new ArrayList<String>();

			resp.getWriter().println("<table>");
			for (Entity wPost : wallposts) {
				String toCheck = wPost.getProperty("PersonName").toString();
				if (!usersThatPosted.contains(toCheck)) {
					usersThatPosted.add(toCheck);
				}
			}

			for (String s : usersThatPosted) {
				resp.getWriter().println("<tr><td>" + s + "</td></tr>");
			}

			resp.getWriter().println("</table>");

			String footer = htmlhelp.getFooterHtml();
			resp.getWriter().println(footer);
		} else {
			resp.sendRedirect(usrService.createLoginURL(req.getRequestURI()));
		}
	}
}