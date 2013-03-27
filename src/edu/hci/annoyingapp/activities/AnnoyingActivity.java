package edu.hci.annoyingapp.activities;

import java.util.Calendar;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.dialogs.AnnoyingDialog;
import edu.hci.annoyingapp.dialogs.AnnoyingDialog.AnnoyingListener;
import edu.hci.annoyingapp.model.Stat;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Window;

public class AnnoyingActivity extends FragmentActivity implements
		AnnoyingListener {

	private static final String TAG = AnnoyingActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	public static final int CONFIG_1 = 1;
	public static final int CONFIG_2 = 2;

	private Stat mCurrentData;
	
	private boolean mHasStoppedProperly;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_annoying);

		// To Use if you want the phone to vibrate!
		// Vibrator myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		// myVib.vibrate(1000);

	}

	@Override
	protected void onStart() {
		super.onStart();

		mCurrentData = new Stat();
		mHasStoppedProperly = false;
		
		Calendar cal = Calendar.getInstance();
		mCurrentData.setStartTime(cal.getTimeInMillis());

		showDialog();

		AnnoyingApplication.startDialog();
	}

	@Override
	protected void onStop() {
		super.onStop();

		Calendar cal = Calendar.getInstance();
		mCurrentData.setStopTime(cal.getTimeInMillis());
		if(!mHasStoppedProperly) {
			
			mCurrentData.setHasQuitProperly(false);
			// Temporary hack for when user clicks on home button...
			if(DEBUG_MODE) {
				Log.d(TAG, "Anything could have happened!!!");
			}
			AnnoyingApplication.stopDialog(mCurrentData);
			finish();
		} else {
			mCurrentData.setHasQuitProperly(true);
			AnnoyingApplication.stopDialog(mCurrentData);
		}
	}

	private void showDialog() {

		FragmentManager fm = getSupportFragmentManager();
		AnnoyingDialog ad = (AnnoyingDialog) fm
				.findFragmentByTag(AnnoyingDialog.TAG);
		if (ad == null) {

			SharedPreferences settings = getSharedPreferences(
					AnnoyingApplication.PREFS_NAME, 0);
			mCurrentData.setConfig(settings.getInt(
					AnnoyingApplication.CONFIG_TYPE,
					AnnoyingApplication.DEFAULT_CONFIG));

			Bundle args = new Bundle();
			args.putInt(AnnoyingDialog.CONFIG_TYPE, mCurrentData.getConfig());
			ad = AnnoyingDialog.newInstance(args);
			ad.setAnnoyingListener(this);
			ad.setCancelable(false);
			ad.show(fm.beginTransaction(), AnnoyingDialog.TAG);
		}
	}

	@Override
	public void onPositiveButtonClicked() {
		mHasStoppedProperly = true;
		finish();
	}

	@Override
	public void onNegativeButtonClicked() {
		Calendar cal = Calendar.getInstance();
		mCurrentData.addFailure(cal.getTimeInMillis());
	}
}
