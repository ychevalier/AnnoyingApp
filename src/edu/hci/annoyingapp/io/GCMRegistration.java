package edu.hci.annoyingapp.io;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.utils.Common;

/**
 * GCM registration helper (Second step to Google).
 */
public class GCMRegistration {
	
	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	private static final String TAG = GCMRegistration.class.getSimpleName();
	
	/**
	 * Interface that should be implemented by the caller of GCMRegistration class.
	 */
	public interface GCMRegistrationListener {
		/**
		 * If this app is already registered to google server.
		 * 
		 * @param regId Registration id on google server.
		 */
		void onAlreadyRegistered(String regId);
	}
	
	/**
	 * Local reference to the context.
	 */
	private Context mContext;
	
	/**
	 * Local referene of the listener that should be called when completed.
	 */
	private GCMRegistrationListener mListener;
	
	public GCMRegistration(Context context, GCMRegistrationListener listener) {
		mContext = context;
		mListener = listener;
	}
	
	/**
	 * Use this method to start second step registration.
	 * It verifies stuff and start the registration to google.
	 */
	public void register() {
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(mContext);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(mContext);

		final String regId = GCMRegistrar.getRegistrationId(mContext);
		if (regId.equals("")) {
			if(DEBUG_MODE) {
				Log.d(TAG, "Trying to register...");
			}
			// Automatically registers application on startup.
			GCMRegistrar.register(mContext, Common.SENDER_ID);
		} else {
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(mContext)) {
				// Skips registration.
				//mDisplay.append(mContext.getString(R.string.already_registered) + "\n");
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				mListener.onAlreadyRegistered(regId);
			}
		}
	}
	
	/**
	 * Static method to unregister easily to google servers.
	 * 
	 * @param c Static reference to the context.
	 */
	public static void unregister(Context c) {
		GCMRegistrar.unregister(c);
	}
}
