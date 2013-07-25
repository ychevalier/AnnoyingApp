package edu.hci.annoyingserver.servlets.registration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import edu.hci.annoyingserver.BaseServlet;
import edu.hci.annoyingserver.Utils;
import edu.hci.annoyingserver.db.MySQLAccess;
import edu.hci.annoyingserver.helpers.ParametersHandler;
import edu.hci.annoyingserver.protocol.Queries;

@SuppressWarnings("serial")
public class UnregisterServlet extends BaseServlet {
	
	private static final boolean DEBUG_MODE = Utils.DEBUG_MODE;
	private static final String TAG = UnregisterServlet.class
			.getSimpleName() + ": ";

	private int mUid;

	private boolean checkParameters(HttpServletRequest req) {

		try {
			mUid = ParametersHandler.checkUid(getParameter(req,
					Queries.PARAMETER_UID));
			if (mUid == -1) {
				return false;
			}
		} catch (ServletException e) {
			return false;
		}

		return true;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		
		if (!checkParameters(req)) {
			setFailureBadRequest(resp);
			return;
		}
		
		if (DEBUG_MODE) {
			System.out.println(TAG + "Received an new query with parameters:");
			System.out.println("\tUid = " + mUid);
		}

		if (MySQLAccess.unregister(mUid)) {
			setSuccess(resp);
		} else {
			setFailureServer(resp);
		}
	}
}
