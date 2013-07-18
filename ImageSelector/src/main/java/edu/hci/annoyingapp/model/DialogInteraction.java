package edu.hci.annoyingapp.model;

import java.util.LinkedList;
import java.util.List;

public class DialogInteraction {
	
	// Time is in millis;
	
	private long mStartTime;
	
	private long mStopTime;
	
	private List<Long> mFailedTime;
	
	private int mTheme;
	
	private int mCondition;
	
	// If condition is answer.
	private String mImage;
		
	// If condition is positional.
	private int mPosition;
	
	private String mTopImage;
	
	private String mBottomImage;
	
	private String mDialogTitle;
	
	private String mDialogText;
	
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
	
	public List<Long> getFailures() {
		return mFailedTime;
	}
	
	public int getNbFailures() {
		return mFailedTime.size();
	}
	
	public int getTheme() {
		return mTheme;
	}
	
	public int getCondition() {
		return mCondition;
	}
	
	public String getImage() {
		return mImage;
	}
	
	public int getPosition() {
		return mPosition;
	}
	
	public String getTopImage() {
		return mTopImage;
	}
	
	public String getBottomImage() {
		return mBottomImage;
	}
	
	public String getDialogTitle() {
		return mDialogTitle;
	}
	
	public String getDialogText() {
		return mDialogText;
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
	
	public void setTheme(int theme) {
		mTheme = theme;
	}
	
	public void setCondition(int condition) {
		mCondition = condition;
	}
	
	public void setImage(String newImage) {
		mImage = newImage;
	}
	
	public void setPosition(int newPosition) {
		mPosition = newPosition;
	}
	
	public void setTopImage(String topImage) {
		mTopImage = topImage;
	}
	
	public void setBottomImage(String bottomImage) {
		mBottomImage = bottomImage;
	}
	
	public void setDialogTitle(String title) {
		mDialogTitle = title;
	}
	
	public void setDialogText(String text) {
		mDialogText = text;
	}
	
	public void setHasQuitProperly(boolean hasQuit) {
		mHasQuitProperly = hasQuit;
	}
}
