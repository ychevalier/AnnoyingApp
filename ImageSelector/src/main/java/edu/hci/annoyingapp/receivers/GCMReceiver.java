package edu.hci.annoyingapp.receivers;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

public class GCMReceiver extends GCMBroadcastReceiver {
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		return "edu.hci.annoyingapp.services.GCMIntentService";
	}
}
