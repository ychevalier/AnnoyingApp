package edu.hci.annoyingapp;

import java.util.Calendar;

import edu.hci.annoyingapp.services.AnnoyingService;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AnnoyingApplication extends Application {

	public static final boolean DEBUG_MODE = true;

	// Little trick so that every instance
	// of SettingsActivity can access the alarm...
	private static AlarmManager mAlarmManager;
	private static PendingIntent mPendingIntent;

	private static boolean mIsDialogRunning;

	@Override
	public void onCreate() {
		super.onCreate();

		mAlarmManager = null;
		mPendingIntent = null;

		mIsDialogRunning = false;
	}

	public static void startDialog() {
		mIsDialogRunning = true;
	}

	public static void stopDialog() {
		mIsDialogRunning = false;
	}

	public static boolean isDialogStarted() {
		return mIsDialogRunning;
	}

	public static void startService(Context context, int sec) {
		Intent background = new Intent(context, AnnoyingService.class);

		Calendar cal = Calendar.getInstance();
		long time = cal.getTimeInMillis();
		
		// A service can be stop by the system, so we need to schedule it...
		mPendingIntent = PendingIntent.getService(context, 0,
				background, 0);
		mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		// Start every DIALOG_TIME seconds.
		mAlarmManager.set(AlarmManager.RTC_WAKEUP, time + sec * 1000, mPendingIntent);
	}

	public static boolean stopService() {
		if (mAlarmManager != null && mPendingIntent != null) {
			mAlarmManager.cancel(mPendingIntent);
			mAlarmManager = null;
			mPendingIntent = null;
			return true;
		} else {
			return false;
		}
	}
}
