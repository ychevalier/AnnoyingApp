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
public class FinishRegisterServlet extends BaseServlet {

	private static final boolean DEBUG_MODE = Utils.DEBUG_MODE;
	private static final String TAG = FinishRegisterServlet.class
			.getSimpleName() + ": ";

	private String mRegId;
	private int mUid;

	private boolean checkParameters(HttpServletRequest req) {

		try {
			mRegId = ParametersHandler.checkRegId(getParameter(req,
					Queries.PARAMETER_REG_ID));
			if (mRegId == null) {
				return false;
			}

			mUid = ParametersHandler.checkUid(getParameter(req,
					Queries.PARAMETER_UID));
			if (mUid == -1) {
				return false;
			}

			return true;
		} catch (ServletException e) {
			// e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {

		if (!checkParameters(req)) {
			if (DEBUG_MODE) {
				System.out.println(TAG + "Received an incorrect query.");
			}
			setFailureBadRequest(resp);
			return;
		}

		if (DEBUG_MODE) {
			System.out.println(TAG + "Received an new query with parameters:");
			System.out.println("\tRegId = " + mRegId);
			System.out.println("\tUid = " + mUid);
		}

		if (MySQLAccess.finish_register(mUid, mRegId)) {
			setSuccess(resp);
		} else {
			setFailureServer(resp);
		}
	}

}
