package edu.hci.annoyingapp.activities;

import java.util.Calendar;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.dialogs.AnnoyingDialog;
import edu.hci.annoyingapp.dialogs.AnnoyingDialog.AnnoyingListener;
import edu.hci.annoyingapp.model.DialogInteraction;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Dialogs;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Interactions;
import edu.hci.annoyingapp.utils.Common;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

public class AnnoyingActivity extends FragmentActivity implements
		AnnoyingListener {

	private static final String TAG = AnnoyingActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	private DialogInteraction mCurrentDialog;

	private boolean mHasStoppedProperly;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_annoying);
	}

	@Override
	protected void onStart() {
		super.onStart();

		mCurrentDialog = new DialogInteraction();
		mHasStoppedProperly = false;

		Calendar cal = Calendar.getInstance();
		mCurrentDialog.setStartTime(cal.getTimeInMillis());

		SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);

		mCurrentDialog.setCondition(settings.getInt(Common.PREF_CONDITION,
				Common.DEFAULT_CONDITION));
		
		mCurrentDialog.setDialogTitle(settings.getString(
				Common.PREF_DIALOG_TITLE, Common.DEFAULT_DIALOG_TITLE));

		mCurrentDialog.setDialogText(settings.getString(
				Common.PREF_DIALOG_TEXT, Common.DEFAULT_DIALOG));

		mCurrentDialog.setPositiveText(settings.getString(
				Common.PREF_POSITIVE_BUTTON, Common.DEFAULT_POSITIVE));

		mCurrentDialog.setNegativeText(settings.getString(
				Common.PREF_NEGATIVE_BUTTON, Common.DEFAULT_NEGATIVE));

		showDialog();

		AnnoyingApplication.startDialog();
	}

	@Override
	protected void onStop() {
		super.onStop();

		Calendar cal = Calendar.getInstance();
		mCurrentDialog.setStopTime(cal.getTimeInMillis());

		ContentValues dialog = new ContentValues();
		dialog.put(Dialogs.DIALOG_START, mCurrentDialog.getStartTime());
		dialog.put(Dialogs.DIALOG_CONDITION, mCurrentDialog.getCondition());
		dialog.put(Dialogs.DIALOG_POSITIVE_TEXT,
				mCurrentDialog.getPositiveText());
		dialog.put(Dialogs.DIALOG_NEGATIVE_TEXT,
				mCurrentDialog.getNegativeText());
		dialog.put(Dialogs.DIALOG_TEXT, mCurrentDialog.getDialogText());
		dialog.put(Dialogs.DIALOG_TITLE, mCurrentDialog.getDialogTitle());

		Uri uri = getContentResolver().insert(Dialogs.CONTENT_URI, dialog);
		String seg = uri.getLastPathSegment();
		Integer id = Integer.valueOf(seg);

		for (Long fail : mCurrentDialog.getFailures()) {
			ContentValues interaction = new ContentValues();
			interaction.put(Interactions.INTERACTION_BUTTON,
					Common.BUTTON_NEGATIVE);
			interaction.put(Interactions.INTERACTION_DATETIME, fail);
			interaction.put(Interactions.INTERACTION_DIALOG_ID, id);
			getContentResolver().insert(Interactions.CONTENT_URI, interaction);
		}

		if (!mHasStoppedProperly) {

			mCurrentDialog.setHasQuitProperly(false);

			ContentValues interaction = new ContentValues();
			interaction.put(Interactions.INTERACTION_BUTTON,
					Common.BUTTON_OTHER);
			interaction.put(Interactions.INTERACTION_DATETIME,
					mCurrentDialog.getStopTime());
			interaction.put(Interactions.INTERACTION_DIALOG_ID, id);
			getContentResolver().insert(Interactions.CONTENT_URI, interaction);

			// AnnoyingApplication.stopDialog(mCurrentData);
			AnnoyingApplication.stopDialog();
			finish();
		} else {
			mCurrentDialog.setHasQuitProperly(true);

			ContentValues interaction = new ContentValues();
			interaction.put(Interactions.INTERACTION_BUTTON,
					Common.BUTTON_POSITIVE);
			interaction.put(Interactions.INTERACTION_DATETIME,
					mCurrentDialog.getStopTime());
			interaction.put(Interactions.INTERACTION_DIALOG_ID, id);
			getContentResolver().insert(Interactions.CONTENT_URI, interaction);

			// AnnoyingApplication.stopDialog(mCurrentData);
			AnnoyingApplication.stopDialog();
		}

		SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
		if(settings.getBoolean(Common.PREF_IS_SERVICE_RUNNING, Common.DEFAULT_IS_RUNNING)) {
			AnnoyingApplication.startService(this, Common.DEFAULT_BIG_INTERVAL);
		}
	}

	private void showDialog() {

		FragmentManager fm = getSupportFragmentManager();
		AnnoyingDialog ad = (AnnoyingDialog) fm
				.findFragmentByTag(AnnoyingDialog.TAG);
		if (ad == null) {
			Bundle args = new Bundle();
			args.putInt(AnnoyingDialog.CONDITION, mCurrentDialog.getCondition());
			args.putString(AnnoyingDialog.POSITIVE_TEXT,
					mCurrentDialog.getPositiveText());
			args.putString(AnnoyingDialog.NEGATIVE_TEXT,
					mCurrentDialog.getNegativeText());
			args.putString(AnnoyingDialog.DIALOG_TEXT,
					mCurrentDialog.getDialogText());
			args.putString(AnnoyingDialog.DIALOG_TITLE,
					mCurrentDialog.getDialogTitle());
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
		mCurrentDialog.addFailure(cal.getTimeInMillis());
	}
}
