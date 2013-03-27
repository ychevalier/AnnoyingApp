package edu.hci.annoyingapp.model;

import java.util.LinkedList;
import java.util.List;

public class Stat {
	
	// Each time is in millis;
	
	private long mStartTime;
	
	private long mStopTime;
	
	private List<Long> mFailedTime;
	
	private int mConfig;
	
	private boolean mHasQuitProperly;
	
	public Stat() {
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
	
	public int getConfig() {
		return mConfig;
	}
	
	public boolean getHasQuitProperly() {
		return mHasQuitProperly;
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
	
	public void setConfig(int config) {
		mConfig = config;
	}
	
	public void setHasQuitProperly(boolean hasQuit) {
		mHasQuitProperly = hasQuit;
	}
}
