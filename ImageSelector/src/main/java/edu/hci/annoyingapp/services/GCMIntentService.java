package edu.hci.annoyingapp.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.URLUtil;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.network.ServerUtilities;
import edu.hci.annoyingapp.protocol.PushMessages;
import edu.hci.annoyingapp.protocol.Receivers;
import edu.hci.annoyingapp.utils.Common;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = GCMIntentService.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	/**
	 * Default and mandatory constructor. SENDER_ID is the project ID defined
	 * when you create a new Google API project.
	 */
	public GCMIntentService() {
		super(Common.SENDER_ID);
	}

	/**
	 * Called when GCM confirm registration. This method sends the registration
	 * ID to 3th party server. It also sends a broadcast message in the
	 * application, which is used to refresh the UI.
	 */
	@Override
	protected void onRegistered(Context context, String registrationId) {
		if (DEBUG_MODE) {
			Log.d(TAG, "Device registered: regId = " + registrationId);
		}

		SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
		String uid = settings.getString(Common.PREF_UID, null);

		boolean success = false;
		if (uid != null) {
			if (success = ServerUtilities.finishRegister(registrationId, uid)) {
				GCMRegistrar.setRegisteredOnServer(context, true);
			}
		}

		Intent intent = new Intent(Receivers.REGISTERED);
		intent.putExtra(Receivers.REGISTERED_SUCCESS, success);
		this.sendBroadcast(intent);
	}

	/**
	 * This Method is called when GCM confirms the unregistration. If it is
	 * still registered on 3th party server, it unregisters there as well. This
	 * app never fully unregisters to the 3th party server, it only erases its
	 * GCM registration id. It also sends a broadcast message to refresh the UI.
	 */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		if (DEBUG_MODE) {
			Log.d(TAG, "Device unregistered");
		}
		if (GCMRegistrar.isRegisteredOnServer(context)) {

			SharedPreferences settings = getSharedPreferences(
					Common.PREFS_NAME, 0);
			String uid = settings.getString(Common.PREF_UID, null);

			if (ServerUtilities.unregister(uid)) {
				GCMRegistrar.setRegisteredOnServer(context, false);
			}

		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			if (DEBUG_MODE) {
				Log.d(TAG, "Ignoring unregister callback");
			}
		}

		Intent intent = new Intent(Receivers.UNREGISTERED);
		this.sendBroadcast(intent);
	}

	/**
	 * This method is called whenever a GCM message is received. It creates a
	 * broadcast intent with the bundle for the UI.
	 */
	@Override
	protected void onMessage(Context context, Intent intent) {

		if (DEBUG_MODE) {
			Log.i(TAG, "Received message");
		}

		String survey = PushMessages.saveParams(context, intent.getExtras());

		if (survey != null && URLUtil.isHttpUrl(survey)) {
			if (DEBUG_MODE) {
				Log.i(TAG, "Received survey : " + survey);
			}
			SharedPreferences settings = getSharedPreferences(
					Common.PREFS_NAME, 0);
			String token = settings.getString(Common.PREF_TOKEN, null);

			Common.launchSurveyNotification(this, survey + token);
		}
	}

	@Override
	public void onError(Context context, String errorId) {
		if (DEBUG_MODE) {
			Log.i(TAG, "Received error: " + errorId);
		}
	}
}
