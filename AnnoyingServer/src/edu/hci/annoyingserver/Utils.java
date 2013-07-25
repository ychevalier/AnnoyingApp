package edu.hci.annoyingserver;

import java.util.LinkedList;
import java.util.List;

public class Utils {

	public static final boolean DEBUG_MODE = true;
	
	public static List<Integer> valueOfArray(String[] tab) {
		
		if(tab == null) {
			return null;
		}
		
		LinkedList<Integer> result = new LinkedList<Integer>();
		
		for (int i = 0; i < tab.length; i++) {
			result.add(Integer.valueOf(tab[i]));
		}
		
		return result;
	}
	
}
