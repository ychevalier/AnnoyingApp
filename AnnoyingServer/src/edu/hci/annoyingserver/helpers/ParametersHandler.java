package edu.hci.annoyingserver.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParametersHandler {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static String checkEmail(String email) {
		if(email == null) {
			return null;
		}
		
		Pattern p = Pattern.compile(EMAIL_PATTERN);
		Matcher m = p.matcher(email);
		
		return m.matches() ? email : null;
	}

	public static int checkUid(String uid) {
		try {
			return Integer.parseInt(uid);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static String checkRegId(String regId) {
		return regId;
	}

	public static String checkDeviceId(String deviceId) {
		return deviceId;
	}

	public static int checkVersion(String version) {
		try {
			return Integer.parseInt(version);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public static String checkData(String data) {
		return data;
	}

}
