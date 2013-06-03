package edu.hci.annoyingapp.activities;

import java.util.Calendar;
import java.util.Random;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.R;
import edu.hci.annoyingapp.dialogs.AnnoyingDialog;
import edu.hci.annoyingapp.dialogs.AnnoyingDialog.AnnoyingListener;
import edu.hci.annoyingapp.model.DialogInteraction;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Dialogs;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Interactions;
import edu.hci.annoyingapp.utils.Common;

public class AnnoyingActivity extends FragmentActivity implements
		AnnoyingListener {

	private static final String TAG = AnnoyingActivity.class.getSimpleName();
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;

	private DialogInteraction mCurrentDialog;

	private boolean mHasStoppedProperly;
	private boolean mIsTopPositive;

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
		
		int theme = settings.getInt(Common.PREF_THEME, Common.DEFAULT_INT);
		int condition = settings.getInt(Common.PREF_CONDITION, Common.DEFAULT_INT);
		int image = settings.getInt(Common.PREF_IMAGE, Common.DEFAULT_INT);
		int position = settings.getInt(Common.PREF_POSITION, Common.DEFAULT_INT);
		String title = settings.getString(Common.PREF_DIALOG_TITLE, Common.DEFAULT_STRING);
		
		if(theme == Common.DEFAULT_INT
				|| condition == Common.DEFAULT_INT
				|| image == Common.DEFAULT_INT
				|| position == Common.DEFAULT_INT
				|| title == Common.DEFAULT_STRING) {
			// Do Something here?
			return;
		}
		
		int topImage = Common.DEFAULT_INT;
		int bottomImage = Common.DEFAULT_INT;
		String text = Common.DEFAULT_STRING;
		
		Random randomGenerator = new Random();
		
		switch(condition) {
		case Common.CONDITION_RANDOM:
			topImage = Common.getRandomImage();
			bottomImage = Common.getRandomImage(topImage);
			
			if(randomGenerator.nextBoolean()) {
				text = Common.getImageName(topImage);
				mIsTopPositive = true;
			} else {
				text = Common.getImageName(bottomImage);
				mIsTopPositive = false;
			}
			break;
		case Common.CONDITION_POSITION:
			topImage = Common.getRandomImage();
			bottomImage = Common.getRandomImage(topImage);
			
			if(position == Common.POSITION_TOP) {
				text = Common.getImageName(topImage);
				mIsTopPositive = true;
			} else if(position == Common.POSITION_BOTTOM) {
				text = Common.getImageName(bottomImage);
				mIsTopPositive = false;
			}
			break;
		case Common.CONDITION_ANSWER:
			if(randomGenerator.nextBoolean()) {
				topImage = image;
				bottomImage = Common.getRandomImage(topImage);
				mIsTopPositive = true;
			} else {
				bottomImage = image;
				topImage = Common.getRandomImage(bottomImage);
				mIsTopPositive = false;
			}
			text = Common.getImageName(image);
			break;
		case Common.CONDITION_BOTH:
			if(position == Common.POSITION_TOP) {
				topImage = image;
				bottomImage = Common.getRandomImage(topImage);
				mIsTopPositive = true;
			} else if(position == Common.POSITION_BOTTOM) {
				bottomImage = image;
				topImage = Common.getRandomImage(bottomImage);
				mIsTopPositive = false;
			}
			text = Common.getImageName(image);
			break;
		default:
				// Do nothing
				break;
		}
		
		if(topImage == Common.DEFAULT_INT
				|| bottomImage == Common.DEFAULT_INT
				|| text == Common.DEFAULT_STRING) {
			// Do Something here?
			return;
		}
		
		String imgStr = Common.getImageName(image);
		String topStr = Common.getImageName(topImage);
		String botStr = Common.getImageName(bottomImage);
		
		if(imgStr == Common.DEFAULT_STRING
			|| topStr == Common.DEFAULT_STRING
			|| botStr == Common.DEFAULT_STRING) {
			return;
		}
		
		mCurrentDialog.setDialogText(text);
		mCurrentDialog.setTopImage(topStr);
		mCurrentDialog.setBottomImage(botStr);		
		mCurrentDialog.setDialogTitle(title);
		mCurrentDialog.setCondition(condition);
		mCurrentDialog.setImage(imgStr);
		mCurrentDialog.setPosition(position);

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
				mCurrentDialog.getTopImage());
		dialog.put(Dialogs.DIALOG_NEGATIVE_TEXT,
				mCurrentDialog.getBottomImage());
		dialog.put(Dialogs.DIALOG_TEXT, mCurrentDialog.getDialogText());
		dialog.put(Dialogs.DIALOG_TITLE, mCurrentDialog.getDialogTitle());

		Uri uri = getContentResolver().insert(Dialogs.CONTENT_URI, dialog);
		String seg = uri.getLastPathSegment();
		Integer id = Integer.valueOf(seg);

		for (Long fail : mCurrentDialog.getFailures()) {
			ContentValues interaction = new ContentValues();
			interaction.put(Interactions.INTERACTION_BUTTON,
					mIsTopPositive? Common.POSITION_BOTTOM : Common.POSITION_TOP);
			interaction.put(Interactions.INTERACTION_DATETIME, fail);
			interaction.put(Interactions.INTERACTION_DIALOG_ID, id);
			getContentResolver().insert(Interactions.CONTENT_URI, interaction);
		}

		if (!mHasStoppedProperly) {
			mCurrentDialog.setHasQuitProperly(false);

			ContentValues interaction = new ContentValues();
			interaction.put(Interactions.INTERACTION_BUTTON,
					Common.POSITION_OTHER);
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
					!mIsTopPositive? Common.POSITION_BOTTOM : Common.POSITION_TOP);
			interaction.put(Interactions.INTERACTION_DATETIME,
					mCurrentDialog.getStopTime());
			interaction.put(Interactions.INTERACTION_DIALOG_ID, id);
			getContentResolver().insert(Interactions.CONTENT_URI, interaction);

			// AnnoyingApplication.stopDialog(mCurrentData);
			AnnoyingApplication.stopDialog();
		}

		SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
		int interval = settings.getInt(Common.PREF_BIG_INTERVAL, Common.DEFAULT_INT);
		if(settings.getBoolean(Common.PREF_IS_SERVICE_RUNNING, Common.DEFAULT_BOOL)) {
			AnnoyingApplication.startService(this, interval);
		}
	}

	private void showDialog() {

		FragmentManager fm = getSupportFragmentManager();
		AnnoyingDialog ad = (AnnoyingDialog) fm
				.findFragmentByTag(AnnoyingDialog.TAG);
		if (ad == null) {
			Bundle args = new Bundle();
			args.putInt(AnnoyingDialog.THEME, mCurrentDialog.getTheme());
			args.putString(AnnoyingDialog.TOP_IMAGE, mCurrentDialog.getTopImage());
			args.putString(AnnoyingDialog.BOTTOM_IMAGE, mCurrentDialog.getBottomImage());
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
	public void onTopButtonClicked() {
		if(mIsTopPositive) {
			mHasStoppedProperly = true;
			finish();
		} else {
			Calendar cal = Calendar.getInstance();
			mCurrentDialog.addFailure(cal.getTimeInMillis());
		}
	}

	@Override
	public void onBottomButtonClicked() {
		if(!mIsTopPositive) {
			mHasStoppedProperly = true;
			finish();
		} else {
			Calendar cal = Calendar.getInstance();
			mCurrentDialog.addFailure(cal.getTimeInMillis());
		}
	}
}
