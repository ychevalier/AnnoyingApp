package edu.hci.annoyingapp.protocol;

/**
 * Name of the broadcast intent receivers within the app.
 * They are typically used by IntentService to broadcast received messages to the UI.
 */
public class Receivers {

	// ================== Broadcast Receiver =====================

	public static final String NEW_SURVEY = "NEW_SURVEY";

	public static final String UNREGISTERED = "UNREGISTERED";

	public static final String REGISTERED = "REGISTERED";
	public static final String REGISTERED_SUCCESS = "REGISTERED_SUCCESS";

}
