package edu.hci.annoyingapp;

import java.util.LinkedList;
import java.util.List;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import edu.hci.annoyingapp.model.Data;

public class AnnoyingApplication extends Application {
	
	public static final boolean DEBUG_MODE = true;
	
	// Little trick so that every instance 
	// of SettingsActivity can access the alarm...
	public static AlarmManager mAlarmManager;
	public static PendingIntent mPendingIntent;
	
	private static boolean mIsDialogRunning;
	private static List<Data> mDataList;
	
	@Override
	public void onCreate() {
		super.onCreate();

		mAlarmManager = null;
		mPendingIntent = null;
		
		mIsDialogRunning = false;
		
		mDataList = new LinkedList<Data>();
	}
	
	public static void startDialog() {
		mIsDialogRunning = true;
	}
	
	public static void stopDialog(Data d) {
		mIsDialogRunning = false;
		mDataList.add(d);
	}
	
	public static boolean isDialogStarted() {
		return mIsDialogRunning;
	}
}
