package edu.hci.annoyingapp.model;

import java.util.LinkedList;
import java.util.List;

public class DialogInteraction {
	
	// Each time is in millis;
	
	private long mStartTime;
	
	private long mStopTime;
	
	private List<Long> mFailedTime;
	
	private int mCondition;
	
	private String mPositiveText;
	
	private String mNegativeText;
	
	private String mDialogText;
	
	private String mDialogTitle;
	
	private boolean mHasQuitProperly;
	
	public DialogInteraction() {
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
	
	public int getCondition() {
		return mCondition;
	}
	
	public String getPositiveText() {
		return mPositiveText;
	}
	
	public String getNegativeText() {
		return mNegativeText;
	}
	
	public String getDialogText() {
		return mDialogText;
	}
	
	public String getDialogTitle() {
		return mDialogTitle;
	}
	
	public boolean getHasQuitProperly() {
		return mHasQuitProperly;
	}
	
	public List<Long> getFailures() {
		return mFailedTime;
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
	
	public void setCondition(int condition) {
		mCondition = condition;
	}
	
	public void setPositiveText(String text) {
		mPositiveText = text;
	}
	
	public void setNegativeText(String text) {
		mNegativeText = text;
	}
	
	public void setDialogText(String text) {
		mDialogText = text;
	}
	
	public void setDialogTitle(String title) {
		mDialogTitle = title;
	}
	
	public void setHasQuitProperly(boolean hasQuit) {
		mHasQuitProperly = hasQuit;
	}
}
