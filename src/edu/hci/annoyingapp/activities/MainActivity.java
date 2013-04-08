package edu.hci.annoyingapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.fragments.SettingsFragment;
import edu.hci.annoyingapp.fragments.SettingsFragment.OnSettingChoiceListener;
import edu.hci.annoyingapp.fragments.StatsFragment;
import edu.hci.annoyingapp.utils.Common;

public class MainActivity extends FragmentActivity implements
		OnSettingChoiceListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	private int mConfig;
	private int mBigInterval;
	private int mLittleInterval;
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
					Common.PREFS_NAME, 0);
			mConfig = settings.getInt(Common.CONFIG_TYPE,
					Common.DEFAULT_CONFIG);
			mBigInterval = settings.getInt(Common.BIG_INTERVAL,
					Common.DEFAULT_BIG_INTERVAL);
			mLittleInterval = settings.getInt(Common.LITTLE_INTERVAL,
					Common.DEFAULT_LITTLE_INTERVAL);
			mIsRunning = settings.getBoolean(
					Common.IS_SERVICE_RUNNING,
					Common.DEFAULT_IS_RUNNING);

			Bundle args = new Bundle();
			args.putInt(SettingsFragment.CONFIG_TYPE, mConfig);
			args.putInt(SettingsFragment.BIG_INTERVAL, mBigInterval);
			args.putInt(SettingsFragment.LITTLE_INTERVAL, mLittleInterval);
			args.putBoolean(SettingsFragment.IS_RUNNING, mIsRunning);

			setFragment = SettingsFragment.newInstance(args);
			fm.beginTransaction()
					.add(R.id.content_activity_main, setFragment,
							SettingsFragment.TAG).commit();
		}
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
		  GCMRegistrar.register(this, Common.GCM_SENDER_ID);
		} else {
			if(DEBUG_MODE) {
				Log.v(TAG, "Already registered");
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		if(mIsRunning) {
			AnnoyingApplication.stopService();
		}
	}

	@Override
	protected void onStop() {

		super.onStop();

		SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(Common.CONFIG_TYPE, mConfig);
		editor.putInt(Common.BIG_INTERVAL, mBigInterval);
		editor.putInt(Common.LITTLE_INTERVAL, mLittleInterval);
		editor.putBoolean(Common.IS_SERVICE_RUNNING, mIsRunning);
		editor.commit();

		if (mIsRunning) {
			
			AnnoyingApplication.startService(this, Common.DEFAULT_BIG_INTERVAL);
			// Little trick so that activity is not on background when dialog
			// shows up.
			finish();
		}

	}
	

	@Override
	public void onServiceStart() {
		mIsRunning = true;
	}

	@Override
	public void onServiceStop() {
		//AnnoyingApplication.stopService();
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

	@Override
	public void onSetBigInterval(int bigInterval) {
		mBigInterval = bigInterval;
	}

	@Override
	public void onSetLittleInterval(int littleInterval) {
		mLittleInterval = littleInterval;
	}
}
