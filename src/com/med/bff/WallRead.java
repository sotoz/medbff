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

import java.util.List;

import com.med.bff.HtmlHelper;

@SuppressWarnings("serial")
public class WallRead extends HttpServlet {
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
			// add html header

			String header = htmlhelp.getHeaderHtml(usr.getNickname() +" | <a href=\""+ usrService.createLogoutURL("/")+"\">Logout</a>");
			resp.getWriter().println(header);
			resp.getWriter().println("<h1>Wall Posts</h1>");
			resp.getWriter().println("<table style='width:100%'>");
			resp.getWriter().println("<th>Who</th><th>What</th><th>Date</th>");

			for (Entity wPost : wallposts) {
				resp.getWriter().println("<tr>");
				String isLink = "";
				try {
					isLink = wPost.getProperty("isLink").toString();
				} catch (NullPointerException e) {
					isLink = "0";
				}

				if (isLink.equalsIgnoreCase("0")) {
					// its simple text post
					resp.getWriter().println(
							"<td>" + wPost.getProperty("PersonName")
									+ "</td><td>" + wPost.getProperty("post")
									+ "</td><td>"
									+ wPost.getProperty("created").toString()
									+ "</td>");
				} else {
					// link post
					resp.getWriter()
							.println(
									"<td> "
											+ wPost.getProperty("PersonName")
											+ "</td><td>"
											+ "<a href=\""
											+ wPost.getProperty("post")
											+ "\" title=\"wall link\" target=\"_blank\">"
											+ wPost.getProperty("post")
											+ "</a></td><td>"
											+ wPost.getProperty("created")
													.toString() + "</td>");
				}
				resp.getWriter().println("</tr>");
			}
			resp.getWriter().println("</table>");
			// add html footer
			String footer = htmlhelp.getFooterHtml();
			resp.getWriter().println(footer);
		} else {
			resp.sendRedirect(usrService.createLoginURL(req.getRequestURI()));
		}
	}
}