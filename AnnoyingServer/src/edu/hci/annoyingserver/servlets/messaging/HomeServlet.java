package edu.hci.annoyingserver.servlets.messaging;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.hci.annoyingserver.BaseServlet;
import edu.hci.annoyingserver.db.MySQLAccess;
import edu.hci.annoyingserver.html.Attributes;
import edu.hci.annoyingserver.model.User;
import edu.hci.annoyingserver.protocol.Conditions;
import edu.hci.annoyingserver.protocol.Position;
import edu.hci.annoyingserver.protocol.Theme;

@SuppressWarnings("serial")
public class HomeServlet extends BaseServlet {

	static final String ATTRIBUTE_STATUS = "status";

	private static String DEFAULT_DROPOUT_TIME = "24";

	/**
	 * Displays the existing messages and offer the option to send a new one.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();

		String status = (String) req.getAttribute(ATTRIBUTE_STATUS);
		out.print(createHomePage(status));

		resp.setStatus(HttpServletResponse.SC_OK);
	}

	private String createHomePage(String status) {
		StringBuilder page = new StringBuilder();

		page.append("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
		page.append("<html xmlns='http://www.w3.org/1999/xhtml'>");

		page.append(createHeader());

		page.append(createContent(status));

		page.append("</html>");

		return page.toString();
	}

	private String createHeader() {

		StringBuilder header = new StringBuilder();

		header.append("<head>");
		header.append("<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />");
		header.append("<title>Annoying Server</title>");
		header.append("<link rel='icon' href='favicon.png'/>");
		header.append("<style type='text/css'>@import url('style.css');</style>");
		header.append("<link href='http://fonts.googleapis.com/css?family=Arbutus+Slab' rel='stylesheet' type='text/css'>");
		header.append("<link href='http://fonts.googleapis.com/css?family=Noto+Serif' rel='stylesheet' type='text/css'>");
		header.append("<script language='JavaScript'>");
		header.append("function toggle(source) {");
		header.append("checkboxes = document.getElementsByName('"
				+ Attributes.UID + "');");
		header.append("for(var i=0, n=checkboxes.length;i<n;i++) {");
		header.append("checkboxes[i].checked = source.checked;");
		header.append("}}");

		header.append("function resetRadio(source, attribute) {checkboxes = document.getElementsByName(attribute);for(var i=0, n=checkboxes.length;i<n;i++) {checkboxes[i].checked = false;}}");
		header.append("function putDefault(source) {uidbox = document.getElementsByName('"
				+ Attributes.UID_COMMAS
				+ "');uidbox[0].value='"
				+ Attributes.DEFAULT + "';}");

		header.append("function putDefault2(source) { uidbox = document.forms['sendform']['"+ Attributes.UID_COMMAS +"'];if(source.checked) {uidbox.disabled=true;} else {uidbox.disabled=false;	}}");
		
		header.append("function toggleMe(a){");
		header.append("	var e=document.getElementById(a);");
		header.append("	if(!e) return true;");
		header.append("	if(e.style.display=='none') {");
		header.append("		e.style.display='block'");
		header.append("	}");
		header.append("	else {");
		header.append("		e.style.display='none'");
		header.append("	}");
		header.append("	return true;");
		header.append("}");

		header.append("\nfunction validateForm()"
				+ '\n'
				+ "{"
				+ '\n'
				+ "   var x=document.forms['sendform']['"
				+ Attributes.UID_COMMAS
				+ "'].value;"
				+ "   var y=document.forms['sendform']['"
				+ Attributes.DEFAULT_BOX
				+ "'];"
				+ '\n'
				+ "   if ((x==null || x==\"\") && !y.checked)"
				+ '\n'
				+ "   {"
				+ '\n'
				+ "      alert('No device selected.');"
				+ '\n'
				+ "      return false;"
				+ '\n'
				+ "   }"
				+ '\n'
				+ "   var x=document.forms['sendform']['"
				+ Attributes.DIALOGTITLE
				+ "'].value;"
				+ '\n'
				+ "   if (x!=null && x!=\"\" && x.length > 256)"
				+ '\n'
				+ "   {"
				+ '\n'
				+ "      alert('Title is too long.');"
				+ '\n'
				+ "      return false;"
				+ '\n'
				+ "   }"
				+ '\n'
				+ "   var x=document.forms['sendform']['"
				+ Attributes.DIALOGTXT
				+ "'].value;"
				+ '\n'
				+ "   if (x!=null && x!=\"\" && x.length > 512)"
				+ '\n'
				+ "   {"
				+ '\n'
				+ "      alert('Text is too long.');"
				+ '\n'
				+ "      return false;"
				+ '\n'
				+ "   }"
				+ '\n'
				+ "   var x=document.forms['sendform']['"
				+ Attributes.LITTLE_INTERVAL
				+ "'].value;"
				+ '\n'
				+ "   if (x!=null && x!=\"\" && isNaN(x))"
				+ '\n'
				+ "   {"
				+ '\n'
				+ "      alert('Little Interval must be a number.');"
				+ '\n'
				+ "      return false;"
				+ '\n'
				+ "   }"
				+ '\n'
				+ "   var x=document.forms['sendform']['"
				+ Attributes.BIG_INTERVAL
				+ "'].value;"
				+ '\n'
				+ "   if (x!=null && x!=\"\" && isNaN(x))"
				+ '\n'
				+ "   {"
				+ '\n'
				+ "      alert('Big Interval must be a number.');"
				+ '\n'
				+ "      return false;"
				+ '\n'
				+ "   }"
				+ '\n'
				+ "   var x=document.forms['sendform']['"
				+ Attributes.DATA_INTERVAL
				+ "'].value;"
				+ '\n'
				+ "   if (x!=null && x!=\"\" && isNaN(x))"
				+ '\n'
				+ "   {"
				+ '\n'
				+ "      alert('Data Interval must be a number.');"
				+ '\n'
				+ "      return false;"
				+ '\n'
				+ "   }"
				+ '\n'
				+ "   var x=document.forms['sendform']['"
				+ Attributes.CONDITION
				+ "'].value;"
				+ '\n'
				+ "	 if(document.getElementById('position').checked || document.getElementById('both').checked) {"
				+ '\n'
				+ " 		 if(!document.getElementById('top').checked && !document.getElementById('bottom').checked) {"
				+ '\n' + "      	alert('You must choose a position.');" + '\n'
				+ "      	return false;" + '\n' + "	 	 }" + '\n' + "	 }" + '\n'
				+ "}");

		header.append("</script>");
		header.append("</head>");

		return header.toString();
	}

	private String createContent(String status) {
		StringBuilder content = new StringBuilder();

		content.append("<body>");

		content.append("<h1><a class='title' href='home'>Dashboard</a></h1>");

		content.append("<form name='sendform' method='POST' action='sendAll' onsubmit='return validateForm()' enctype='multipart/form-data'>");

		content.append(createDevicesSection());

		content.append(createOptionsSection());

		content.append("</form>");

		content.append(createLastResultSection(status));

		content.append(createReportSection());

		content.append("</body>");

		return content.toString();
	}

	private String createDevicesSection() {

		StringBuilder devSection = new StringBuilder();

		List<User> devices = MySQLAccess.getAvailableUsers();

		devSection.append("<h2><a class='section' href='nowhere' onClick=\"toggleMe('devices1'); return false\">Devices</a></h2>");

		devSection.append("<div class='content' id='devices1'>");

		if (devices == null || devices.isEmpty()) {
			devSection.append("<p>No devices registered!</p>");
		} else {
			devSection.append("<table id='hor-minimalist-b'>");

			devSection.append("<thead>");
			devSection.append("<tr>");
			/*
			 * devSection .append(
			 * "<th scope='col'><input type='checkbox' onClick='toggle(this)'/></th>"
			 * );
			 */
			devSection.append("<th scope='col'>UID</th>");
			devSection.append("<th scope='col'>Name</th>");
			devSection.append("<th scope='col'>Registration</th>");
			/*
			 * devSection.append("<th scope='col'>Service</th>");
			 * devSection.append("<th scope='col'>Condition</th>");
			 * devSection.append("<th scope='col'>Intervals</th>");
			 * devSection.append("<th scope='col'>Buttons</th>");
			 * devSection.append("<th scope='col'>Text</th>");
			 */
			devSection.append("</tr>");
			devSection.append("</thead>");

			for (User u : devices) {
				devSection.append("<tr>");
				/*
				 * devSection.append("<td>");
				 * devSection.append("<input type='checkbox' name='" +
				 * Attributes.UID + "' value='" + u.getUid() + "'/>");
				 * devSection.append("</td>");
				 */
				devSection.append("<td>");
				devSection.append(u.getUid());
				devSection.append("</td>");

				devSection.append("<td>");
				devSection.append(u.getEmail());
				devSection.append("</td>");

				devSection.append("<td>");
				Date date = new Date(u.getRegistrationDate());
				devSection.append(date.toString());
				devSection.append("</td>");

				/*
				 * 
				 * devSection.append("<td>"); if(u.isRunning()) {
				 * devSection.append("ON"); } else { devSection.append("OFF"); }
				 * devSection.append("</td>");
				 * 
				 * 
				 * 
				 * devSection.append("<td>");
				 * devSection.append(u.getCondition());
				 * devSection.append("</td>");
				 * 
				 * devSection.append("<td>");
				 * devSection.append(u.getBigInterval());
				 * devSection.append('/');
				 * devSection.append(u.getLittleInterval());
				 * devSection.append("</td>");
				 * 
				 * devSection.append("<td>");
				 * devSection.append(u.getPositiveButton());
				 * devSection.append('/');
				 * devSection.append(u.getNegativeButton());
				 * devSection.append("</td>");
				 * 
				 * devSection.append("<td>");
				 * devSection.append(u.getDialogText());
				 * devSection.append("</td>");
				 */
				devSection.append("</tr>");

			}
			devSection.append("</table>");
		}
		devSection.append("</div>");

		return devSection.toString();
	}

	private String createOptionsSection() {

		StringBuilder optSection = new StringBuilder();

		optSection.append("<h2><a class='section' href='nowhere' onClick=\"toggleMe('options1'); return false\">Options</a></h2>");
		
		optSection.append("<div class='content' id='options1'>");

		optSection.append("<table id='option-tab'>");

		optSection.append("<tr>");
		optSection.append("<td>Send to (<input type='checkbox' name='"+ Attributes.DEFAULT_BOX + "' onClick='putDefault2(this)' id='default_box'/><label for='default_box'>default</label>)");
		// <a class='special-link'
		// href='nowhere'
		// onClick='putDefault(this);
		// return
		// false'>(default)</a></td>");
		optSection.append("<td><input type='file' name='"
				+ Attributes.UID_COMMAS + "' /></td>");
		// optSection.append("<td><input onfocus=\"if (this.value == '" +
		// Attributes.DEFAULT +
		// "') {this.value = '';}\" id='option-text' type='text' name='" +
		// Attributes.UID_COMMAS + "'/></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td><p></p></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td>Service</td>");
		optSection.append("<td>");
		optSection.append("<div class='onoffswitch'>");
		optSection.append("<input type='checkbox' name='"
				+ Attributes.ON_OFF_SWITCH
				+ "' class='onoffswitch-checkbox' id='myonoffswitch' checked>");
		optSection
				.append("<label class='onoffswitch-label' for='myonoffswitch'>");
		optSection.append("<div class='onoffswitch-inner'></div>");
		optSection.append("<div class='onoffswitch-switch'></div>");
		optSection.append("</label>");
		optSection.append("</div>");
		optSection.append("</td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td><p></p></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection
				.append("<td>Theme <a class='special-link' href='nowhere' onClick=\"resetRadio(this, '"
						+ Attributes.THEME
						+ "'); return false\">(reset)</a></td>");
		optSection.append("<td>");
		optSection.append("<input type='radio' name='" + Attributes.THEME
				+ "' value='" + Theme.DARK
				+ "' id='dark'/><label for='dark'>&nbsp;Dark</label>");
		optSection.append("<br/>");
		// optSection.append("<span id='space'/>");
		optSection
				.append("<input  type='radio' name='"
						+ Attributes.THEME
						+ "' value='"
						+ Theme.LIGHT
						+ "' id='light'/><label for='light'>&nbsp;Light</label>");
		// optSection.append("<span id='space'/>");
		optSection.append("</td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td><p></p></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection
				.append("<td>Condition <a class='special-link' href='nowhere' onClick=\"resetRadio(this, '"
						+ Attributes.CONDITION
						+ "'); return false\">(reset)</a></td>");
		optSection.append("<td>");

		// optSection.append("<table>");
		// optSection.append("<tr>");
		// optSection.append("<td>");
		optSection.append("<input type='radio' name='" + Attributes.CONDITION
				+ "' value='" + Conditions.RANDOM
				+ "' id='random'/><label for='random'>&nbsp;Random</label>");
		// optSection.append("</td>");
		// optSection.append("<span id='space'/>");
		optSection.append("<br/>");
		// optSection.append("<td>");
		optSection
				.append("<input  type='radio' name='"
						+ Attributes.CONDITION
						+ "' value='"
						+ Conditions.POSITION
						+ "' id='position'/><label for='position'>&nbsp;Position</label>");
		// optSection.append("</td>");
		// optSection.append("<span id='space'/>");

		// optSection.append("</tr>");
		// optSection.append("<tr>");

		optSection.append("<br/>");
		// optSection.append("<td>");
		optSection.append("<input type='radio' name='" + Attributes.CONDITION
				+ "' value='" + Conditions.ANSWER
				+ "' id='answer'/><label for='answer'>&nbsp;Answer</label>");
		// optSection.append("</td>");
		// optSection.append("<span id='space'/>");
		optSection.append("<br/>");
		// optSection.append("<td>");
		optSection.append("<input type='radio' name='" + Attributes.CONDITION
				+ "' value='" + Conditions.BOTH
				+ "' id='both'/><label for='both'>&nbsp;Both</label>");
		// optSection.append("</td>");
		// optSection.append("</tr>");
		// optSection.append("</table>");

		optSection.append("</td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td><p></p></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td>New Image</td>");
		optSection.append("<td>");
		optSection.append("<input type='checkbox' name='"
				+ Attributes.NEW_IMAGE + "'>");
		optSection.append("</td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td><p></p></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection
				.append("<td>Position <a class='special-link' href='nowhere' onClick=\"resetRadio(this, '"
						+ Attributes.POSITION
						+ "'); return false\">(reset)</a></td>");
		optSection.append("<td>");
		optSection.append("<input type='radio' name='" + Attributes.POSITION
				+ "' value='" + Position.TOP
				+ "' id='top'/><label for='top'>&nbsp;Top</label>");
		// optSection.append("<span id='space'/>");
		optSection.append("<br/>");
		optSection.append("<input  type='radio' name='" + Attributes.POSITION
				+ "' value='" + Position.BOTTOM
				+ "' id='bottom'/><label for='bottom'>&nbsp;Bottom</label>");
		// optSection.append("<span id='space'/>");
		optSection.append("</td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td><p></p></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td>Dialog Title</td>");
		optSection.append("<td><input id='option-text' type='text' name='"
				+ Attributes.DIALOGTITLE + "'/></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td>Dialog Text</td>");
		optSection.append("<td><input id='option-text' type='text' name='"
				+ Attributes.DIALOGTXT + "'/></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td><p></p></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td>Little Interval (seconds)</td>");
		optSection.append("<td><input id='option-text' type='text' name='"
				+ Attributes.LITTLE_INTERVAL + "'/></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td>Big Interval (seconds)</td>");
		optSection.append("<td><input id='option-text' type='text' name='"
				+ Attributes.BIG_INTERVAL + "'/></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td>Data Gathering Interval (seconds)</td>");
		optSection.append("<td><input id='option-text' type='text' name='"
				+ Attributes.DATA_INTERVAL + "'/></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td><p></p></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td>Survey (URL)</td>");
		optSection.append("<td><input id='option-text' type='text' name='"
				+ Attributes.SURVEY_URL + "'/></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td><p></p></td>");
		optSection.append("</tr>");
		optSection.append("<tr>");
		optSection.append("<td></td>");
		optSection
				.append("<td><button class='cupid-green' type='submit'>Do it!</button></td>");
		optSection.append("</tr>");
		optSection.append("</table>");

		optSection.append("</div>");

		return optSection.toString();
	}

	private String createLastResultSection(String status) {

		StringBuilder lastSection = new StringBuilder();
		
		lastSection.append("<h2><a class='section' href='nowhere' onClick=\"toggleMe('last1'); return false\">Last Result</a></h2>");
		
		lastSection.append("<div class='content' id='last1'>");
		
		lastSection.append("<p>");

		if (status != null) {
			lastSection.append(status);
		} else {
			lastSection.append("Nothing sent yet.");
		}
		lastSection.append("</p>");
		lastSection.append("</div>");

		return lastSection.toString();
	}

	private String createReportSection() {

		StringBuilder reportSection = new StringBuilder();

		reportSection.append("<h2><a class='section' href='nowhere' onClick=\"toggleMe('report1'); return false\">Reporting</a></h2>");
		
		reportSection.append("<div class='content' id='report1'>");

		reportSection
				.append("<form name='form' method='POST' action='report'>");
		reportSection.append("<table>");

		reportSection.append("<tr>");
		reportSection.append("<td>");
		reportSection.append("<button id='report' value='"
				+ Attributes.USERS_REPORT + "' type='submit' name='"
				+ Attributes.REPORTBT + "' >Get Users</button>");
		reportSection.append("</td>");
		reportSection.append("<td></td>");
		reportSection.append("</tr>");
		reportSection.append("<tr>");
		reportSection.append("<td><p></p></td>");
		reportSection.append("</tr>");
		reportSection.append("<tr>");
		reportSection.append("<td>");
		reportSection.append("<button id='report' value='"
				+ Attributes.DIALOGS_REPORT + "' type='submit' name='"
				+ Attributes.REPORTBT + "'>Get Dialogs</button>");
		reportSection.append("</td>");
		reportSection.append("<td></td>");
		reportSection.append("</tr>");
		reportSection.append("<tr>");
		reportSection.append("<td><p></p></td>");
		reportSection.append("</tr>");
		reportSection.append("<tr>");
		reportSection.append("<td>");
		reportSection.append("<button id='report' value='"
				+ Attributes.INTERACTIONS_REPORT + "' type='submit' name='"
				+ Attributes.REPORTBT + "'>Get Interactions</button>");
		reportSection.append("</td>");
		reportSection.append("<td></td>");
		reportSection.append("</tr>");
		reportSection.append("<td><p></p></td>");
		reportSection.append("<tr>");
		reportSection.append("<td>");
		reportSection.append("<button id='report' value='"
				+ Attributes.DROPOUT_REPORT + "' type='submit' name='"
				+ Attributes.REPORTBT + "'>Get Dropouts</button>");
		reportSection.append("</td>");
		reportSection.append("<td>");
		reportSection.append("<input id='dropoutinput' value='"
				+ DEFAULT_DROPOUT_TIME + "' "
				+ " onblur=\"if (this.value == '') {this.value = '"
				+ DEFAULT_DROPOUT_TIME + "';}\""
				+ " onfocus=\"if (this.value == '" + DEFAULT_DROPOUT_TIME
				+ "') {this.value = '';}\" type='text' name='"
				+ Attributes.DROPOUT_TIME_TEXT + "'/>");

		reportSection.append("&nbsp;hours");
		reportSection.append("</td>");
		reportSection.append("</tr>");
		reportSection.append("<tr>");
		reportSection.append("<td><p></p></td>");
		reportSection.append("</tr>");
		reportSection.append("<tr>");
		reportSection.append("<td>");
		reportSection.append("<button id='report' value='"
				+ Attributes.DEFAULTS_REPORT + "' type='submit' name='"
				+ Attributes.REPORTBT + "'>Get Default Values</button>");
		reportSection.append("</td>");
		reportSection.append("<td></td>");
		reportSection.append("</tr>");

		reportSection.append("<tr>");
		reportSection.append("<td><p></p></td>");
		reportSection.append("</tr>");
		reportSection.append("<tr>");
		reportSection.append("<td>");
		reportSection.append("<button id='report' value='"
				+ Attributes.CONDITIONS_REPORT + "' type='submit' name='"
				+ Attributes.REPORTBT + "'>Get Conditions</button>");
		reportSection.append("</td>");
		reportSection.append("<td></td>");
		reportSection.append("</tr>");

		reportSection.append("<tr>");
		reportSection.append("<td><p></p></td>");
		reportSection.append("</tr>");
		reportSection.append("<tr>");
		reportSection.append("<td>");
		reportSection.append("<button id='report' value='"
				+ Attributes.BUTTONS_REPORT + "' type='submit' name='"
				+ Attributes.REPORTBT + "'>Get Buttons</button>");
		reportSection.append("</td>");
		reportSection.append("<td></td>");
		reportSection.append("</tr>");

		reportSection.append("<tr>");
		reportSection.append("<td><p></p></td>");
		reportSection.append("</tr>");
		reportSection.append("<tr>");
		reportSection.append("<td>");
		reportSection.append("<button id='report' value='"
				+ Attributes.TOKENS_REPORT + "' type='submit' name='"
				+ Attributes.REPORTBT + "'>Get Tokens</button>");
		reportSection.append("</td>");
		reportSection.append("<td></td>");
		reportSection.append("</tr>");

		reportSection.append("</table>");
		reportSection.append("</form>");

		reportSection.append("</div>");

		return reportSection.toString();
	}

	/*
	 * @Override protected void doPost(HttpServletRequest req,
	 * HttpServletResponse resp) throws IOException { doGet(req, resp); }
	 */

}
