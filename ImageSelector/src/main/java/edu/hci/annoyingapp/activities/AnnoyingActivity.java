package edu.hci.annoyingapp.activities;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import java.util.Calendar;
import java.util.Random;

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
		
		int theme = settings.getInt(Common.PREF_THEME, Common.THEME_LIGHT);
		int condition = settings.getInt(Common.PREF_CONDITION, Common.CONDITION_RANDOM);
		int image = settings.getInt(Common.PREF_IMAGE, -1);
		int position = settings.getInt(Common.PREF_POSITION, -1);
		String title = settings.getString(Common.PREF_DIALOG_TITLE, Common.DEFAULT_TITLE);
		String text = settings.getString(Common.PREF_DIALOG_TEXT, Common.DEFAULT_MESSAGE);

		SharedPreferences.Editor editor = settings.edit();
		if((condition == Common.CONDITION_POSITION
				|| condition == Common.CONDITION_BOTH)
				&& position == -1) {
			// We are in trouble, put default position.
			position = Common.POSITION_TOP;
			editor.putInt(Common.PREF_POSITION, position);
			editor.commit();
		}
		if((condition == Common.CONDITION_ANSWER
				|| condition == Common.CONDITION_BOTH)
				&& image == -1) {
			// We are in trouble, get new image!
			int current = settings.getInt(Common.PREF_IMAGE, -1);
			editor.putInt(Common.PREF_IMAGE, Common.getRandomImage(current));
			editor.commit();
		}

		int topImage = -1;
		int bottomImage = -1;
		String textComp = null;
		
		Random randomGenerator = new Random();
		
		switch(condition) {
		case Common.CONDITION_RANDOM:
			topImage = Common.getRandomImage();
			bottomImage = Common.getRandomImage(topImage);
			
			if(randomGenerator.nextBoolean()) {
				image = topImage;
				position = Common.POSITION_TOP;
				textComp = Common.getImageName(topImage);
				mIsTopPositive = true;
			} else {
				image = bottomImage;
				position = Common.POSITION_BOTTOM;
				textComp = Common.getImageName(bottomImage);
				mIsTopPositive = false;
			}
			break;
		case Common.CONDITION_POSITION:
			topImage = Common.getRandomImage();
			bottomImage = Common.getRandomImage(topImage);
			
			if(position == Common.POSITION_TOP) {
				image = topImage;
				textComp = Common.getImageName(topImage);
				mIsTopPositive = true;
			} else if(position == Common.POSITION_BOTTOM) {
				image = bottomImage;
				textComp = Common.getImageName(bottomImage);
				mIsTopPositive = false;
			}
			break;
		case Common.CONDITION_ANSWER:
			if(randomGenerator.nextBoolean()) {
				position = Common.POSITION_TOP;
				topImage = image;
				bottomImage = Common.getRandomImage(topImage);
				mIsTopPositive = true;
			} else {
				position = Common.POSITION_BOTTOM;
				bottomImage = image;
				topImage = Common.getRandomImage(bottomImage);
				mIsTopPositive = false;
			}
			textComp = Common.getImageName(image);
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
			textComp = Common.getImageName(image);
			break;
		}
		
		if(topImage == -1
				|| bottomImage == -1
				|| textComp == null) {
			// Big big big trouble.
			finish();
			return;
		}
		
		// If we have no defined image (position or random).
		String imgStr = null;
		if(image != -1) {
			imgStr = Common.getImageName(image);
		}
		String topStr = Common.getImageName(topImage);
		String botStr = Common.getImageName(bottomImage);
		
		if(topStr == null
			|| botStr == null) {
			// Big big big trouble.
			finish();
			return;
		}
		
		mCurrentDialog.setTheme(theme);
		mCurrentDialog.setDialogText(text + ' ' + textComp);
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
		dialog.put(Dialogs.DIALOG_THEME, mCurrentDialog.getTheme());
		dialog.put(Dialogs.DIALOG_CONDITION, mCurrentDialog.getCondition());
		dialog.put(Dialogs.DIALOG_TOP_IMAGE,
				mCurrentDialog.getTopImage());
		dialog.put(Dialogs.DIALOG_BOTTOM_IMAGE,
				mCurrentDialog.getBottomImage());
		dialog.put(Dialogs.DIALOG_IMAGE, mCurrentDialog.getImage());
		dialog.put(Dialogs.DIALOG_POSITION, mCurrentDialog.getPosition());
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
					mIsTopPositive? Common.POSITION_TOP : Common.POSITION_BOTTOM);
			interaction.put(Interactions.INTERACTION_DATETIME,
					mCurrentDialog.getStopTime());
			interaction.put(Interactions.INTERACTION_DIALOG_ID, id);
			getContentResolver().insert(Interactions.CONTENT_URI, interaction);

			// AnnoyingApplication.stopDialog(mCurrentData);
			AnnoyingApplication.stopDialog();
		}

		SharedPreferences settings = getSharedPreferences(Common.PREFS_NAME, 0);
		int interval = settings.getInt(Common.PREF_BIG_INTERVAL, -1);
		if(interval != -1 
				&& settings.getBoolean(Common.PREF_IS_SERVICE_RUNNING, false)) {
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
		if(mIsTopPositive) {
			Calendar cal = Calendar.getInstance();
			mCurrentDialog.addFailure(cal.getTimeInMillis());
		} else {
			mHasStoppedProperly = true;
			finish();
		}
	}
}
