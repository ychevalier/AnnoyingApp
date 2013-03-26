package edu.hci.annoyingapp.model;

import java.util.LinkedList;
import java.util.List;

public class Data {
	
	// Each time is in millis;
	
	private long mStartTime;
	
	private long mStopTime;
	
	private List<Long> mFailedTime;
	
	public Data() {
		mFailedTime = new LinkedList<Long>();
	}
	
	public long getStartTime() {
		return mStartTime;
	}
	
	public long getStopTime() {
		return mStopTime;
	}
	
	public int getNbFailures() {
		return mFailedTime.size();
	}
	
	public void setStartTime(long start) {
		mStartTime = start;
	}
	
	public void setStopTime(long stop) {
		mStopTime = stop;
	}
	
	public void addFailure(long time) {
		mFailedTime.add(Long.valueOf(time));
	}
}
