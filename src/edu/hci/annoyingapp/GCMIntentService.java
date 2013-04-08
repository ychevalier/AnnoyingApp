package edu.hci.annoyingapp;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;

import edu.hci.annoyingapp.utils.Common;

public class GCMIntentService extends GCMBaseIntentService {
	
	public GCMIntentService() {
	    super(Common.GCM_SENDER_ID);
	}

	/**
	 * Called when the device tries to register or unregister, but GCM returned
	 * an error. Typically, there is nothing to be done other than evaluating
	 * the error (returned by errorId) and trying to fix the problem.
	 */
	@Override
	protected void onError(Context context, String errorId) {
		// TODO Auto-generated method stub

	}

	/**
	 * Called when your server sends a message to GCM, and GCM delivers it to
	 * the device. If the message has a payload, its contents are available as
	 * extras in the intent.
	 */
	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub

	}

	/**
	 * Called after a registration intent is received, passes the registration
	 * ID assigned by GCM to that device/application pair as parameter.
	 * Typically, you should send the regid to your server so it can use it to
	 * send messages to this device.
	 */
	@Override
	protected void onRegistered(Context context, String regId) {
		// TODO Auto-generated method stub

	}

	/**
	 * Called after the device has been unregistered from GCM. Typically, you
	 * should send the regid to the server so it unregisters the device.
	 */
	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO Auto-generated method stub

	}

}
