package edu.hci.annoyingserver.servlets.registration;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import edu.hci.annoyingserver.BaseServlet;
import edu.hci.annoyingserver.Utils;
import edu.hci.annoyingserver.db.MySQLAccess;
import edu.hci.annoyingserver.helpers.ParametersHandler;
import edu.hci.annoyingserver.model.User;
import edu.hci.annoyingserver.protocol.Queries;
import edu.hci.annoyingserver.protocol.Registration;

@SuppressWarnings("serial")
public class StartRegisterServlet extends BaseServlet {
	
	private static final boolean DEBUG_MODE = Utils.DEBUG_MODE;
	private static final String TAG = StartRegisterServlet.class.getSimpleName() + ": ";
	
	private String mDeviceId;
	private String mEmail;
	private int mVersion;
	
	private boolean checkParameters(HttpServletRequest req) {
		
		try {
			mDeviceId = ParametersHandler.checkDeviceId(getParameter(req, Queries.PARAMETER_DEVICE_ID));
			if(mDeviceId == null) {
				return false;
			}

			mEmail = ParametersHandler.checkEmail(getParameter(req, Queries.PARAMETER_EMAIL));
			if(mEmail == null) {
				return false;
			}
			
			mVersion = ParametersHandler.checkVersion(getParameter(req, Queries.PARAMETER_VERSION));
			if(mVersion == -1) {
				return false;
			}
			
			return true;
		} catch(ServletException e) {
			//e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		
		if(!checkParameters(req)) {
			if(DEBUG_MODE) {
				System.out.println(TAG + "Received an incorrect query.");
			}
			setFailureBadRequest(resp);
			return;
		}
		
		if(DEBUG_MODE) {
			System.out.println(TAG + "Received an new query with parameters:");
			System.out.println("\tDeviceId = " + mDeviceId);
			System.out.println("\tMail = " + mEmail);
			System.out.println("\tVersion = " + mVersion);
		}

		User u = MySQLAccess.start_register(mDeviceId, mEmail, mVersion);
		String url = MySQLAccess.getConfig("url");
		
		if(u != null) {
			
			StringBuilder content = new StringBuilder();
			
			content.append(Registration.addParameter(Registration.UID, u.getUid()));
			
			content.append(Registration.addParameter(Registration.CONDITION, u.getCondition()));
			
			content.append(Registration.addParameter(Registration.POSITION, u.getPosition()));
			
			content.append(Registration.addParameter(Registration.THEME, u.getTheme()));
			
			content.append(Registration.addParameter(Registration.DIALOG_TEXT, u.getDialogText()));
			
			content.append(Registration.addParameter(Registration.RUNNING, u.isRunning()));
			
			content.append(Registration.addParameter(Registration.LITTLE_INTERVAL, u.getLittleInterval()));
			
			content.append(Registration.addParameter(Registration.BIG_INTERVAL, u.getBigInterval()));
			
			content.append(Registration.addParameter(Registration.DATA_INTERVAL, u.getDataInterval()));
			
			content.append(Registration.addParameter(Registration.DIALOG_TITLE, u.getTitle()));
			
			content.append(Registration.addParameter(Registration.TOKEN, u.getToken()));
			
			content.append(Registration.addParameter(Registration.FIRST_SURVEY, url));
			
			try {
				PrintWriter out = resp.getWriter();
				out.print(content);
				setSuccess(resp, content.length());
			} catch (IOException e) {
				setFailureServer(resp);
			}
		} else {
			setFailureServer(resp);
		}
	}
}
