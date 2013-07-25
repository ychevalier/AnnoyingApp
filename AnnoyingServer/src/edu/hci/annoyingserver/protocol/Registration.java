package edu.hci.annoyingserver.protocol;

public class Registration {
	
	private static final char NEW_LINE = '\n';
	private static final char SEPARATOR = ':';
	
	public static final String UID = "uid";
	
	public static final String CONDITION = "condition";
	
	public static final String LITTLE_INTERVAL = "little_interval";
	
	public static final String BIG_INTERVAL = "big_interval";
	
	public static final String POSITION = "position";
	
	public static final String THEME = "theme";
	
	public static final String DIALOG_TEXT = "dialog_text";
	
	public static final String DATA_INTERVAL = "data_interval";
	
	public static final String RUNNING = "running";
	
	public static final String DIALOG_TITLE = "dialog_title";
	
	public static final String TOKEN = "token";
	
	public static final String FIRST_SURVEY = "first_survey";
	
	public static String addParameter(String name, boolean value) {
		return addParameter(name, String.valueOf(value));
	}
	
	public static String addParameter(String name, int value) {
		return addParameter(name, String.valueOf(value));
	}
	
	public static String addParameter(String name, String value) {
		StringBuilder param = new StringBuilder();
		
		param.append(name);
		param.append(SEPARATOR);
		param.append(value);
		param.append(NEW_LINE);
		
		return param.toString();
	}
}
