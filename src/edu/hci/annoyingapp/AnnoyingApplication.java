package edu.hci.annoyingapp;

import edu.hci.annoyingapp.io.DataSender;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;

public class AnnoyingApplication extends Application {
	
	public static final boolean DEBUG_MODE = true;
	
	public static final String WS_Server = "http://www.cs.bham.ac.uk/research/projects/hci/dialog_habits/WS.php";
	
	// Little trick so that every instance 
	// of SettingsActivity can access the alarm...
	public static AlarmManager mAlarmManager;
	public static PendingIntent mPendingIntent;
	
	private static boolean mIsDialogRunning;
	
	// Shared Global Stuff...
	public static final String PREFS_NAME = "AnnoyingPrefs";
	
	public static final String IS_SERVICE_RUNNING = "IsServiceRunning";
	public static final String CONFIG_TYPE = "ConfigType";
	public static final String INTERVAL = "Interval";
	
	public static final int BUTTON_YES = 0;
	public static final int BUTTON_NO = 1;
	public static final int BUTTON_OTHER = 2;
	
	public static final int CONFIG_YES_NO = 1;
	public static final int CONFIG_NO_YES = 2;
	
	public static final boolean DEFAULT_IS_RUNNING = false;
	public static final int DEFAULT_INTERVAL = 20;
	public static final int DEFAULT_CONFIG = CONFIG_YES_NO;
	
	public static final int UID = 42;
	
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
}
