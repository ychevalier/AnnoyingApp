package edu.hci.annoyingapp.activities;

import java.util.Calendar;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.fragments.SettingsFragment;
import edu.hci.annoyingapp.fragments.SettingsFragment.OnSettingChoiceListener;
import edu.hci.annoyingapp.fragments.StatsFragment;
import edu.hci.annoyingapp.services.AnnoyingService;
import edu.hci.annoyingapp.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements
		OnSettingChoiceListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	private int mConfig;
	private int mInterval;
	private boolean mIsRunning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		FragmentManager fm = getSupportFragmentManager();
		SettingsFragment setFragment = (SettingsFragment) fm
				.findFragmentByTag(SettingsFragment.TAG);
		if (setFragment == null) {

			SharedPreferences settings = getSharedPreferences(
					AnnoyingApplication.PREFS_NAME, 0);
			mConfig = settings.getInt(AnnoyingApplication.CONFIG_TYPE,
					AnnoyingApplication.DEFAULT_CONFIG);
			mInterval = settings.getInt(AnnoyingApplication.INTERVAL,
					AnnoyingApplication.DEFAULT_INTERVAL);
			mIsRunning = settings.getBoolean(
					AnnoyingApplication.IS_SERVICE_RUNNING,
					AnnoyingApplication.DEFAULT_IS_RUNNING);

			Bundle args = new Bundle();
			args.putInt(SettingsFragment.CONFIG_TYPE, mConfig);
			args.putInt(SettingsFragment.INTERVAL, mInterval);
			args.putBoolean(SettingsFragment.IS_RUNNING, mIsRunning);

			setFragment = SettingsFragment.newInstance(args);
			fm.beginTransaction()
					.add(R.id.content_activity_main, setFragment,
							SettingsFragment.TAG).commit();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(mIsRunning) {
			stopService();
		}
	}

	@Override
	protected void onStop() {

		super.onStop();

		SharedPreferences settings = getSharedPreferences(AnnoyingApplication.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(AnnoyingApplication.CONFIG_TYPE, mConfig);
		editor.putInt(AnnoyingApplication.INTERVAL, mInterval);
		editor.putBoolean(AnnoyingApplication.IS_SERVICE_RUNNING, mIsRunning);
		editor.commit();

		if (mIsRunning) {
			Calendar cal = Calendar.getInstance();
			Intent background = new Intent(this, AnnoyingService.class);

			// A service can be stop by the system, so we need to schedule it...
			AnnoyingApplication.mPendingIntent = PendingIntent.getService(this,
					0, background, 0);
			AnnoyingApplication.mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			// Start every DIALOG_TIME seconds.
			AnnoyingApplication.mAlarmManager.setRepeating(
					AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					mInterval * 1000, AnnoyingApplication.mPendingIntent);

			// Little trick so that activity is not on background when dialog
			// shows up.
			finish();
		}

	}

	private boolean stopService() {
		if (AnnoyingApplication.mAlarmManager != null
				&& AnnoyingApplication.mPendingIntent != null) {
			AnnoyingApplication.mAlarmManager
					.cancel(AnnoyingApplication.mPendingIntent);
			AnnoyingApplication.mAlarmManager = null;
			AnnoyingApplication.mPendingIntent = null;

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onServiceStart() {
		mIsRunning = true;
	}

	@Override
	public void onServiceStop() {
		stopService();
		mIsRunning = false;
	}

	@Override
	public void onViewStats() {

		StatsFragment statsFragment = StatsFragment.newInstance();

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.content_activity_main, statsFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}

	@Override
	public void onSetTime(int interval) {
		mInterval = interval;
	}

	@Override
	public void onBackPressed() {

		FragmentManager fm = getSupportFragmentManager();
		StatsFragment statsFragment = (StatsFragment) fm
				.findFragmentByTag(StatsFragment.TAG);
		if (statsFragment != null) {
			fm.popBackStack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onConfigChanged(int config) {
		mConfig = config;
	}
}
