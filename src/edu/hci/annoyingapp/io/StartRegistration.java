package edu.hci.annoyingapp.io;

import java.util.Map;

import edu.hci.annoyingapp.network.ServerUtilities;
import edu.hci.annoyingapp.protocol.Registration;
import android.os.AsyncTask;


/**
 * This is the registration first step task.
 * It registers to the 3th party server and get a UID.
 * 
 * If the device was already registered, it gets its own old UID,
 * otherwise, a new one is created.
 */
public class StartRegistration extends AsyncTask<Void, Void, Map<String, String>> {
	
	/**
	 * This interface should be implemented by the caller of the task.
	 */
	public interface StartRegistrationListener {
		
		/**
		 * This method is called when the registration is completed.
		 * 
		 * @param uid Return of the webservice, null if failure.
		 */
		void onStartRegistrationCompleted(Map<String, String> result);
	}
	
	private StartRegistrationListener mListener;
	
	/**
	 * Unique device ID stored somewhere in the phone.
	 */
	private String mDeviceId;
	
	/**
	 * Email entered by the user at login.
	 */
	private String mEmail;
	
	/**
	 * Android API version.
	 */
	private String mVersion;
	
	/**
	 * Initialise the task.
	 * 
	 * @param listener Interface that should be implemented by the caller.
	 * @param deviceId Unique device id.
	 * @param email Email to register.
	 * @param version Android API version of the device.
	 */
	public StartRegistration(StartRegistrationListener listener, String deviceId, String email, String version) {
		mListener = listener;
		
		mDeviceId = deviceId;
		mEmail = email;
		mVersion = version;
	}

	/**
	 * Background connection to the 3th party server, registration etc...
	 */
	@Override
	protected Map<String, String> doInBackground(Void... params) {
		String result = ServerUtilities.startRegister(mDeviceId, mEmail, mVersion);
		
		Map<String, String> map = Registration.getParams(result);
		
		return map;
	}

	/**
	 * Send the result to the interface at the end.
	 */
	@Override
	protected void onPostExecute(Map<String, String> result) {
		if(mListener != null) {
			mListener.onStartRegistrationCompleted(result);
		}
	}
}
