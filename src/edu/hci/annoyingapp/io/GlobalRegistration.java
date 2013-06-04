package edu.hci.annoyingapp.io;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;

import com.google.android.gcm.GCMRegistrar;

import edu.hci.annoyingapp.io.FinishRegistration.FinishRegistrationListener;
import edu.hci.annoyingapp.io.GCMRegistration.GCMRegistrationListener;
import edu.hci.annoyingapp.io.StartRegistration.StartRegistrationListener;
import edu.hci.annoyingapp.protocol.Registration;
import edu.hci.annoyingapp.utils.Common;

/**
 * This is a helper class for the activities to register to both Google and 3th
 * party servers for GCM Push Messaging.
 * 
 * The registration is in 3 parts: 
 *    1/ Ask for User ID to 3th party server. 
 *    2/ Ask for Registration ID to Google.
 *    3/ Give Registration ID to 3th party server.
 * 
 * The first part has been added to default GCM process to give more control
 * over devices who wants to register.
 * Usual registration is also per-app whereas this one is per-device, but we could
 * easily adapt it per-user (identified by his email).
 * 
 * The unregistration is only two parts:
 *    1/ Unregister to Google
 *    2/ Tell the 3th party server.
 * 
 * You never fully unregister to the 3th party server, you only reset your google registration id.
 */
public class GlobalRegistration implements StartRegistrationListener,
		GCMRegistrationListener, FinishRegistrationListener {

	/**
	 * The caller of a GlobalRegistration should implement this interface.
	 */
	public interface OnRegistrationOverListener {

		/**
		 * Called when 3 step registration succeded.
		 */
		void onRegistrationSuccess();

		/**
		 * Called when one of the 3 steps failed.
		 */
		void onRegistrationFailure();
	}

	/**
	 * Uid given at the first step by the 3th party server. This is the main
	 * identification of a device.
	 */
	private static String mUid;

	/**
	 * A local version of the application context.
	 */
	private Context mContext;

	/**
	 * Success/Failure listener.
	 */
	private OnRegistrationOverListener mListener;

	/**
	 * First registration Step to 3th party server.
	 */
	private StartRegistration mRegisterStartTask;

	/**
	 * Second registration step to Google.
	 */
	private GCMRegistration mRegisterGCM;

	/**
	 * Third and last registration step to 3th party.
	 */
	private FinishRegistration mRegisterFinishTask;

	/**
	 * Default constructor.
	 * 
	 * @param context
	 *            Application context.
	 * @param listener
	 *            The caller should also listen for success or failure.
	 */
	public GlobalRegistration(Context context,
			OnRegistrationOverListener listener) {
		mContext = context;
		mListener = listener;
	}

	/**
	 * This method is called to start the registration process.
	 * 
	 * @param email
	 *            Email entered by the user.
	 */
	public void register(String email) {

		// For now, the 3th party server will recognise a user by its device.
		// If the user resets or changes his device, he will get a new uid.
		String deviceId = Secure.getString(mContext.getContentResolver(),
				Secure.ANDROID_ID);

		String version = String.valueOf(android.os.Build.VERSION.SDK_INT);

		// Launching the first step of the registration.
		mRegisterStartTask = new StartRegistration(this, deviceId, email,
				version);
		mRegisterStartTask.execute();
	}

	/**
	 * Helper so that anyone can unregister... (this is why it is static).
	 * 
	 * @param c You can register from everywhere so that method needs a static context.
	 */
	public static void unregister(Context c) {
		GCMRegistration.unregister(c);
	}

	public void onDestroy() {
		if (mRegisterStartTask != null) {
			mRegisterStartTask.cancel(true);
		}
		if (mRegisterFinishTask != null) {
			mRegisterFinishTask.cancel(true);
		}
	}

	/**
	 * This method is called when the first step is completed and successful.
	 * It saves its UID and register to Google servers.
	 * UID should be null if registration failed.
	 */
	@Override
	public void onStartRegistrationCompleted(Map<String, String> result) {
		mRegisterStartTask = null;
		
		
		if(result == null || result.get(Registration.UID) == null) {
			if (mListener != null) {
				mListener.onRegistrationFailure();
			}
			return;
		}
		
		mUid = result.get(Registration.UID);
		
		boolean running = false;
		int littleInterval = -1;
		int bigInterval = -1;
		int dataInterval = -1;
		int condition = -1;
		int theme = -1;
		int position = -1;
		int image = -1;
		String text = null;
		String title = null;
		
		String tmp = result.get(Registration.RUNNING);
		if(tmp != null) {
			running = Boolean.parseBoolean(tmp);
		}
		
		tmp = result.get(Registration.LITTLE_INTERVAL);
		if(tmp != null) {
			try {
				littleInterval = Integer.parseInt(tmp);
			} catch(NumberFormatException e) {
				// Nothing To do.
			}
		}
		
		tmp = result.get(Registration.BIG_INTERVAL);
		if(tmp != null) {
			try {
				bigInterval = Integer.parseInt(tmp);
			} catch(NumberFormatException e) {
				// Nothing To do.
			}
		}
		
		tmp = result.get(Registration.DATA_INTERVAL);
		if(tmp != null) {
			try {
				dataInterval = Integer.parseInt(tmp);
			} catch(NumberFormatException e) {
				// Nothing To do.
			}
		}
		
		tmp = result.get(Registration.CONDITION);
		if(tmp != null) {
			try {
				condition = Integer.parseInt(tmp);
			} catch(NumberFormatException e) {
				// Nothing To do.
			}
		}
		
		tmp = result.get(Registration.THEME);
		if(tmp != null) {
			try {
				theme = Integer.parseInt(tmp);
			} catch(NumberFormatException e) {
				// Nothing To do.
			}
		}
		
		tmp = result.get(Registration.POSITION);
		if(tmp != null) {
			try {
				position = Integer.parseInt(tmp);
			} catch(NumberFormatException e) {
				// Nothing To do.
			}
		}
		
		tmp = result.get(Registration.DIALOG_TEXT);
		if(tmp != null) {			
			text = tmp;
		}
		
		tmp = result.get(Registration.DIALOG_TITLE);
		if(tmp != null) {			
			title = tmp;
		}
		
		if(condition == Common.CONDITION_ANSWER
				|| condition == Common.CONDITION_BOTH) {
			image = Common.getRandomImage();
		}

		// Save the prefs.
		SharedPreferences settings = mContext.getSharedPreferences(
				Common.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(Common.PREF_UID, mUid);
		editor.putInt(Common.PREF_CONDITION, condition);
		editor.putInt(Common.PREF_THEME, theme);
		editor.putInt(Common.PREF_POSITION, position);
		editor.putInt(Common.PREF_IMAGE, image);
		editor.putInt(Common.PREF_LITTLE_INTERVAL, littleInterval);
		editor.putInt(Common.PREF_BIG_INTERVAL, bigInterval);
		editor.putInt(Common.PREF_DATA_INTERVAL, dataInterval);
		editor.putBoolean(Common.PREF_IS_SERVICE_RUNNING, running);
		editor.putString(Common.PREF_DIALOG_TEXT, text);
		editor.putString(Common.PREF_DIALOG_TITLE, title);
		editor.commit();

		mRegisterGCM = new GCMRegistration(mContext, this);
		mRegisterGCM.register();
	}

	/**
	 * This method is called after the seconds step (to google) is completed.
	 * It launches the last step.
	 */
	@Override
	public void onAlreadyRegistered(String regId) {
		mRegisterFinishTask = new FinishRegistration(this, mUid,
				regId);
		mRegisterFinishTask.execute();
	}

	/**
	 * Last step of registration is completed (success or not).
	 * Call the listener!
	 */
	@Override
	public void onFinishRegistrationCompleted(boolean isSuccess) {
		mRegisterFinishTask = null;
		if (!isSuccess) {
			GCMRegistration.unregister(mContext);
			if (mListener != null) {
				mListener.onRegistrationFailure();
			}
		} else {
			GCMRegistrar.setRegisteredOnServer(mContext, true);
			if (mListener != null) {
				mListener.onRegistrationSuccess();
			}
		}
	}
}
