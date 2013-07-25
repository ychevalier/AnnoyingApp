package edu.hci.annoyingserver.servlets.reports;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.hci.annoyingserver.BaseServlet;
import edu.hci.annoyingserver.db.MySQLAccess;
import edu.hci.annoyingserver.html.Attributes;

@SuppressWarnings("serial")
public class ReportServlet extends BaseServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Processes the request to add a new message.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		// String file = "myfile2.csv";

		// File f = new File(file);
		// f.createNewFile();

		String reportType = req.getParameter(Attributes.REPORTBT);
		String content = "";
		if (reportType != null) {
			if (reportType.equals(Attributes.USERS_REPORT)) {
				content = MySQLAccess.getUsersReport();
			} else if (reportType.equals(Attributes.DIALOGS_REPORT)) {
				content = MySQLAccess.getDialogsReport();
			} else if (reportType.equals(Attributes.INTERACTIONS_REPORT)) {
				content = MySQLAccess.getInteractionsReport();
			} else if (reportType.equals(Attributes.DEFAULTS_REPORT)) {
				content = MySQLAccess.getDefaultsReport();
			} else if (reportType.equals(Attributes.CONDITIONS_REPORT)) {
				content = MySQLAccess.getConditionsReport();
			} else if (reportType.equals(Attributes.BUTTONS_REPORT)) {
				content = MySQLAccess.getButtonsReport();
			} else if (reportType.equals(Attributes.DROPOUT_REPORT)) {
				
				String dropoutTime = req.getParameter(Attributes.DROPOUT_TIME_TEXT);
				int time = 0;
				try {
					time = Integer.parseInt(dropoutTime);
				} catch(NumberFormatException e) {
					// Do nothing...
				}
				
				content = MySQLAccess.getDropoutsReport(time);
			} else if (reportType.equals(Attributes.TOKENS_REPORT)) {
				content = MySQLAccess.getTokensReport();
			}
		} else {
			content = "No Content";
		}

		resp.setContentType("text");
		PrintWriter out = resp.getWriter();
		out.println(content);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

}
