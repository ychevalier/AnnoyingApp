package edu.hci.annoyingapp.io;

import edu.hci.annoyingapp.network.ServerUtilities;
import android.os.AsyncTask;

/**
 * Task used to realise the last step of registration.
 * Sends the registration Id to the 3th party server.
 */
public class FinishRegistration extends AsyncTask<Void, Void, Boolean> {

	/**
	 * Interface that should be implemented by the caller.
	 */
	public interface FinishRegistrationListener {
		/**
		 * When registration is completed (success or failure).
		 * @param isSuccess Is registration success or failure.
		 */
		void onFinishRegistrationCompleted(boolean isSuccess);
	}
	
	/**
	 * Reference to the listener that is called when the operation is over.
	 */
	private FinishRegistrationListener mListener;

	/**
	 * Copy of the UID (from 3th party server).
	 */
	private String mUid;
	
	/**
	 * Copy of the registration ID (from Google server).
	 */
	private String mRegId;

	/**
	 * Default constructor.
	 * 
	 * @param listener Interface that should be implemented by the caller.
	 * @param uid User ID (Given by the first step).
	 * @param regId Registration ID (Given in the second step).
	 */
	public FinishRegistration(FinishRegistrationListener listener, String uid, String regId) {
		mListener = listener;

		mUid = uid;
		mRegId = regId;
	}

	/**
	 * Background execution of network registration.
	 */
	@Override
	protected Boolean doInBackground(Void... params) {
		return ServerUtilities.finishRegister(mRegId, mUid);
	}

	/**
	 * Result!
	 */
	@Override
	protected void onPostExecute(Boolean result) {
		if (mListener != null) {
			mListener.onFinishRegistrationCompleted(result.booleanValue());
		}
	}

}
