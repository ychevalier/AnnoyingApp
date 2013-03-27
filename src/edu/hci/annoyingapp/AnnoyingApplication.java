package edu.hci.annoyingapp;

import java.util.LinkedList;
import java.util.List;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import edu.hci.annoyingapp.activities.AnnoyingActivity;
import edu.hci.annoyingapp.model.Stat;

public class AnnoyingApplication extends Application {
	
	public static final boolean DEBUG_MODE = true;
	
	// Little trick so that every instance 
	// of SettingsActivity can access the alarm...
	public static AlarmManager mAlarmManager;
	public static PendingIntent mPendingIntent;
	
	public static final String PREFS_NAME = "AnnoyingPrefs";
	
	public static final String IS_SERVICE_RUNNING = "IsServiceRunning";
	public static final String CONFIG_TYPE = "ConfigType";
	public static final String INTERVAL = "Interval";
	
	public static final boolean DEFAULT_IS_RUNNING = false;
	public static final int DEFAULT_INTERVAL = 20;
	public static final int DEFAULT_CONFIG = AnnoyingActivity.CONFIG_1;
	
	public static final int BUTTON_YES = 0;
	public static final int BUTTON_NO = 1;
	public static final int BUTTON_OTHER = 2;
	
	public static final int UID = 42;
	
	private static boolean mIsDialogRunning;
	private static List<Stat> mDataList;
	
	@Override
	public void onCreate() {
		super.onCreate();

		mAlarmManager = null;
		mPendingIntent = null;
		
		mIsDialogRunning = false;
		
		mDataList = new LinkedList<Stat>();
	}
	
	public static void startDialog() {
		mIsDialogRunning = true;
	}
	
	public static void stopDialog(Stat d) {
		mIsDialogRunning = false;
		mDataList.add(d);
	}
	
	public static boolean isDialogStarted() {
		return mIsDialogRunning;
	}
	
	public static Stat[] getStats() {
		Stat[] ret = new Stat[mDataList.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = mDataList.get(i);
		}
		return ret;
	}
}
