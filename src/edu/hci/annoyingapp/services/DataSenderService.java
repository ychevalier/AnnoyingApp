package edu.hci.annoyingapp.services;

import java.util.Calendar;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.io.DataSender;
import edu.hci.annoyingapp.utils.Common;

public class DataSenderService extends IntentService {

	private static final String TAG = DataSenderService.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	public DataSenderService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null || !i.isConnected() || !i.isAvailable()) {
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		long time = cal.getTimeInMillis();
		
		SharedPreferences settings = getSharedPreferences(
				Common.PREFS_NAME, 0);
		long last = settings.getLong(Common.PREF_LAST_SUCCESSFUL_SENDING, Long.valueOf(0));
		String uid = settings.getString(Common.PREF_UID, null);
		
		if(uid != null) {
			DataSender ds = new DataSender(this);
			if(ds.sendData(last, uid)) {
				SharedPreferences.Editor editor = settings.edit();
				editor.putLong(Common.PREF_LAST_SUCCESSFUL_SENDING, time);
				editor.commit();
			}
		}
	}
}
