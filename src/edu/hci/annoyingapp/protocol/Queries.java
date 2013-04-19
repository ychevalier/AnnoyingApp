package edu.hci.annoyingapp.protocol;

/**
 * Webservices paths and parameters.
 * Here unregister uses also UID but does not duplicate it.
 */
public class Queries {
	
	// ================== Start Registration ====================

	public static final String START_PATH = "/start_register";

	public static final String DEVICE_ID_ATT = "deviceId";
	public static final String EMAIL_ATT = "email";
	public static final String VERSION_ATT = "version";

	// ================== Finish Registration ====================

	public static final String FINISH_PATH = "/finish_register";

	public static final String UID_ATT = "uid";
	public static final String REG_ID_ATT = "regId";

	// ================== Unregister ====================

	public static final String UNREGISTER_PATH = "/unregister";
	
	// ================== Send Data =====================
	
	public static final String SEND_DATA_PATH = "/data_receiver";
	
	public static final String DATA_ATT = "input";

}
