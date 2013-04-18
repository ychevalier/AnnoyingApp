package edu.hci.annoyingapp.utils;

public class Common {

	public static final String WS_Server = "http://www.cs.bham.ac.uk/research/projects/hci/dialog_habits/WS.php";

	// ================== Server properties ====================

	/**
	 * Base URL of the Server.
	 */
	public static final String SERVER_URL = "http://10.2.128.34:8080/annoying-server";

	/**
	 * Google API project id registered to use GCM.
	 */
	public static final String SENDER_ID = "454161920705";

	// ================== Shared Preferences ====================

	public static final String PREFS_NAME = "AnnoyingPrefs";

	public static final String PREF_UID = "uid";
	public static final String PREF_IS_SERVICE_RUNNING = "IsServiceRunning";
	public static final String PREF_CONDITION = "Condition";
	public static final String PREF_BIG_INTERVAL = "BigInterval";
	public static final String PREF_LITTLE_INTERVAL = "LittleInterval";
	public static final String PREF_POSITIVE_BUTTON = "PositiveButton";
	public static final String PREF_NEGATIVE_BUTTON = "NegativeButton";
	public static final String PREF_DIALOG_TEXT = "DialogText";

	// This is the distant database, maybe put it in local db later...
	public static final int BUTTON_POSITIVE = 0;
	public static final int BUTTON_NEGATIVE = 1;
	public static final int BUTTON_OTHER = 2;

	// Yes/No before honeycomb and No/Yes later
	public static final int CONDITION_DEFAULT = 0;
	public static final int CONDITION_ALT = 1;
	public static final int CONDITION_OTHER = 2;

	// Default are not relevant anymore since those value are given
	// at registration.
	public static final boolean DEFAULT_IS_RUNNING = false;
	public static final int DEFAULT_BIG_INTERVAL = 20;
	public static final int DEFAULT_LITTLE_INTERVAL = 20;
	public static final int DEFAULT_CONDITION = CONDITION_DEFAULT;
	public static final String DEFAULT_POSITIVE = "Yes";
	public static final String DEFAULT_NEGATIVE = "No";
	public static final String DEFAULT_DIALOG = "Would you like to close this dialog?";
}
