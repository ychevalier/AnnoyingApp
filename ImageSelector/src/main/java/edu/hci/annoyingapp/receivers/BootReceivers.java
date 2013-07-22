package edu.hci.annoyingapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.utils.Common;

public class BootReceivers extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences settings = context.getSharedPreferences(
				Common.PREFS_NAME, 0);

		int bigInterval = settings.getInt(Common.PREF_BIG_INTERVAL,
				Common.DEFAULT_BIG_INTERVAL);

		boolean isRunning = settings.getBoolean(
				Common.PREF_IS_SERVICE_RUNNING,
				true);

		int dataInterval = settings.getInt(Common.PREF_DATA_INTERVAL,
				Common.DEFAULT_DATA_INTERVAL);

		if (isRunning) {
			AnnoyingApplication.startService(context, bigInterval);
		}

		AnnoyingApplication.launchStatusIcon(context);

		AnnoyingApplication.startDataService(context, dataInterval);
	}
}
