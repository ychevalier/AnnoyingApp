package edu.hci.annoyingapp.activities;

import java.util.Calendar;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.dialogs.AnnoyingDialog;
import edu.hci.annoyingapp.dialogs.AnnoyingDialog.AnnoyingListener;
import edu.hci.annoyingapp.model.Data;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Window;

public class AnnoyingActivity extends FragmentActivity implements OnDismissListener, OnCancelListener, AnnoyingListener {

	private static final String TAG = AnnoyingActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	
	private Data mCurrentData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_annoying_blank);

		/*
		 * Button yesBt = (Button)
		 * findViewById(R.id.activity_annoying_yes_button); if(yesBt != null) {
		 * yesBt.setOnClickListener(this); }
		 * 
		 * Button noBt = (Button)
		 * findViewById(R.id.activity_annoying_no_button); if(noBt != null) {
		 * noBt.setOnClickListener(this); }
		 */
		/*
		 * getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
		 * | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN |
		 * WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		 */

		// To Use if you want the phone to vibrate!
		// Vibrator myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		// myVib.vibrate(1000);

	}

	@Override
	protected void onStart() {
		super.onStart();

		mCurrentData = new Data();
		
		Calendar cal = Calendar.getInstance();
		mCurrentData.setStartTime(cal.getTimeInMillis());
		
		showDialog();
		
		AnnoyingApplication.startDialog();
	}

	@Override
	protected void onStop() {
		super.onStop();

		Calendar cal = Calendar.getInstance();
		mCurrentData.setStartTime(cal.getTimeInMillis());
		
		AnnoyingApplication.stopDialog(mCurrentData);
	}
	
	private void showDialog() {

	    FragmentManager fm = getSupportFragmentManager();
		AnnoyingDialog ad = (AnnoyingDialog) fm.findFragmentByTag(AnnoyingDialog.TAG);
	    if (ad == null) {
	    	ad = AnnoyingDialog.newInstance();
	    	ad.setAnnoyingListener(this);
	    	ad.setCancelable(false);
	    	ad.show(fm.beginTransaction(), AnnoyingDialog.TAG);
	    }
	}

	/*
	 * @Override public void onClick(View v) { if (v.getId() ==
	 * R.id.activity_annoying_yes_button) { finish(); } else if (v.getId() ==
	 * R.id.activity_annoying_no_button) { // Do stuff } }
	 */

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (DEBUG_MODE) {
			Log.d(TAG, "Dismissed");
		}
		finish();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (DEBUG_MODE) {
			Log.d(TAG, "Cancelled");
		}
	}

	@Override
	public void onPositiveButtonClicked() {
		finish();
	}

	@Override
	public void onNegativeButtonClicked() {
		Calendar cal = Calendar.getInstance();
		mCurrentData.addFailure(cal.getTimeInMillis());
	}
}
