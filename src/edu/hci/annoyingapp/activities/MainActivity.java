package edu.hci.annoyingapp.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.fragments.SettingsFragment;
import edu.hci.annoyingapp.fragments.SettingsFragment.OnSettingChoiceListener;
import edu.hci.annoyingapp.fragments.StatsFragment;
import edu.hci.annoyingapp.io.GlobalRegistration;
import edu.hci.annoyingapp.protocol.Receivers;
import edu.hci.annoyingapp.services.AnnoyingService;
import edu.hci.annoyingapp.services.DataSenderService;
import edu.hci.annoyingapp.utils.Common;

public class MainActivity extends FragmentActivity implements
		OnSettingChoiceListener {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		SharedPreferences settings = getSharedPreferences(
				Common.PREFS_NAME, 0);
		int condition = settings.getInt(Common.PREF_CONDITION,
				Common.DEFAULT_CONDITION);
		int bigInterval = settings.getInt(Common.PREF_BIG_INTERVAL,
				Common.DEFAULT_BIG_INTERVAL);
		int littleInterval = settings.getInt(Common.PREF_LITTLE_INTERVAL,
				Common.DEFAULT_LITTLE_INTERVAL);
		boolean isRunning = settings.getBoolean(
				Common.PREF_IS_SERVICE_RUNNING,
				Common.DEFAULT_IS_RUNNING);
		
		FragmentManager fm = getSupportFragmentManager();
		SettingsFragment setFragment = (SettingsFragment) fm
				.findFragmentByTag(SettingsFragment.TAG);
		if (setFragment == null) {
			Bundle args = new Bundle();
			args.putInt(SettingsFragment.CONDITION, condition);
			args.putInt(SettingsFragment.BIG_INTERVAL, bigInterval);
			args.putInt(SettingsFragment.LITTLE_INTERVAL, littleInterval);
			args.putBoolean(SettingsFragment.IS_RUNNING, isRunning);

			setFragment = SettingsFragment.newInstance(args);
			fm.beginTransaction()
					.add(R.id.content_activity_main, setFragment,
							SettingsFragment.TAG).commit();
		}
		
		registerReceiver(mHandleUnregistrationReceiver,new IntentFilter(Receivers.UNREGISTERED));
	}

	@Override
	protected void onStop() {

		super.onStop();
		// Little trick so that activity is not on background when dialog
		// shows up.
		finish();
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mHandleUnregistrationReceiver);
		super.onDestroy();
	}

	@Override
	public void onViewStats() {

		Intent background = new Intent(this, DataSenderService.class);
		startService(background);
		/*
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
		*/
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
	public void onUnregister() {
		GlobalRegistration.unregister(getApplicationContext());
		mDialog = ProgressDialog.show(this, "",
				"Disconnecting. Please wait...", true);
	}
	
	private final BroadcastReceiver mHandleUnregistrationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			SharedPreferences settings = context.getSharedPreferences(
					Common.PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean(Common.PREF_IS_SERVICE_RUNNING, false);
			editor.commit();
			
			AnnoyingApplication.stopService();
			
			Intent i = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(i);
			finish();
			mDialog.dismiss();
		}
	};
}
