package edu.hci.annoyingapp.utils;

public class Common {
	
	public static final String WS_Server = "http://www.cs.bham.ac.uk/research/projects/hci/dialog_habits/WS.php";

	public static final String GCM_SERVER_URL = "http://127.0.0.1:8888/gcmdemo4/home";
	// This is project-specific
	public static final String GCM_SENDER_ID = "454161920705";

	// Shared Global Stuff...
	public static final String PREFS_NAME = "AnnoyingPrefs";

	public static final String IS_SERVICE_RUNNING = "IsServiceRunning";
	public static final String CONFIG_TYPE = "ConfigType";
	public static final String BIG_INTERVAL = "BigInterval";
	public static final String LITTLE_INTERVAL = "LittleInterval";

	public static final int BUTTON_YES = 0;
	public static final int BUTTON_NO = 1;
	public static final int BUTTON_OTHER = 2;

	// Yes/No before honeycomb and No/Yes later
	public static final int CONFIG_DEFAULT = 0;
	public static final int CONFIG_ALT = 1;
	public static final int CONFIG_OTHER = 2;

	public static final boolean DEFAULT_IS_RUNNING = false;
	public static final int DEFAULT_BIG_INTERVAL = 20;
	public static final int DEFAULT_LITTLE_INTERVAL = 20;
	public static final int DEFAULT_CONFIG = CONFIG_DEFAULT;

	public static final int UID = 42;

}
