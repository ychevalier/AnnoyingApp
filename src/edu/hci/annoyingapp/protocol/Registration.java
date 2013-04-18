package edu.hci.annoyingapp.protocol;

import java.util.HashMap;
import java.util.Map;

public class Registration {
	
	private static final char NEW_LINE = '\n';
	private static final char SEPARATOR = ':';
	
	public static final String UID = "uid";
	
	public static final String CONDITION = "condition";
	
	public static final String LITTLE_INTERVAL = "little_interval";
	
	public static final String BIG_INTERVAL = "big_interval";
	
	public static final String POSITIVE_TEXT = "positive_text";
	
	public static final String NEGATIVE_TEXT = "negative_text";
	
	public static final String DIALOG_TEXT = "dialog_text";
	
	public static final String RUNNING = "running";
	
	public static Map<String, String> getParams(String params) {
		
		Map<String, String> aMap = new HashMap<String, String>();
			
		String[] lines = params.split("\\"+ NEW_LINE);
		
		for(int i=0; i<lines.length; i++) {
			String[] line = lines[i].split("\\" + SEPARATOR);
			
			if(line.length == 2) {
				aMap.put(line[0], line[1]);
			}
		}

		return aMap;
	}

}
