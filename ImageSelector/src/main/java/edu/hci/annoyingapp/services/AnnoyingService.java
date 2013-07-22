package edu.hci.annoyingapp.services;

import android.app.IntentService;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.util.Log;

import java.util.Calendar;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.activities.AnnoyingActivity;
import edu.hci.annoyingapp.utils.Common;

public class AnnoyingService extends IntentService {
	
	private static final String TAG = AnnoyingService.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	public AnnoyingService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
		int littleInterval = settings.getInt(Common.PREF_LITTLE_INTERVAL, -1);
		
		if(AnnoyingApplication.isDialogStarted()) {
			if(DEBUG_MODE) {
				Log.d(TAG, "Dialog is already running, aborting.");
			}
			//AnnoyingApplication.startService(this, littleInterval);
			return;
		}

		Calendar cal = Calendar.getInstance();
		if(!AnnoyingApplication.canIDisplayMoreDialogs(cal.get(Calendar.DAY_OF_MONTH))) {
			if(DEBUG_MODE) {
				Log.d(TAG, "No more dialogs today, aborting");
			}
			//AnnoyingApplication.startService(this, littleInterval);
			return;
		}
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		
		KeyguardManager kgMgr = 
			    (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		boolean showing = kgMgr.inKeyguardRestrictedInputMode();
		
		if(pm.isScreenOn() && !showing) {
			if(DEBUG_MODE) {
				Log.d(TAG, "Launching dialog!");
			}

			Intent i = new Intent(this, AnnoyingActivity.class);
			
			// This is highly deprectated...
			//intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
			
			// Clear task works only after api 11...
			//i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			
			// This works always.
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			AnnoyingApplication.oneMoreDialog();
		} else if(littleInterval != -1) {
			if(DEBUG_MODE) {
				Log.d(TAG, "Screen is off or locked...");
			}
			AnnoyingApplication.startService(this, littleInterval);
		}

	}
}
