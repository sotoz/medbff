package com.med.bff;

public class HtmlHelper {

	public String getHeaderHtml(String username){
		String html = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">"
				+"<html>"
				+"<head>"
				+ "<title>BFF</title>"
				+ "</head>"
				+ "<body style='background-color:#fefefe'>"
				+ "<div style='width:960px;margin:0 auto;background-color:#eee;padding:25px 15px;'>"
				+ "<div style='width:150px;margin:0 auto; text-align:center;'><h2>Med BFF</h2></div>"
				+ "<div style='width: 960px;text-align: center;margin: 15px;'>Hello "+username+"</div>"
				+ "<div style='text-align: center;width:700px;margin:0 auto;padding:5px; background-color:#CECECE;'><a href=\"readwall\">Read the wall</a> | <a href=\"add\">Add a message to the wall</a> | <a href=\"addlink\">Add a link to the wall</a> | <a href=\"listusers\">List all users that have written a post</a></div>";
		return html;
	}
	public String getFooterHtml(){
		String html = "</div><!-- end div-->"
				+ "</body>"
				+ "</html>";
		return html;
	}
}
