package edu.hci.annoyingserver.model;

public class User {

	private int mUid;

	private String mEmail;

	private Long mRegistrationDate;

	private String mGCMId;

	private int mLittleInterval;

	private int mBigInterval;

	private int mDataInterval;

	private boolean mIsRunning;

	private int mCondition;

	private int mTheme;

	private int mPosition;

	private String mDialogText;

	private String mDialogTitle;
	
	private String mToken;

	public User(int uid, String email, Long registrationDate, String GCMId,
			int littleInterval, int bigInterval, boolean isRunning,
			int condition, String dialogText, String dialogTitle,
			int dataInterval, int theme, int position, String token) {
		this.mUid = uid;
		this.mEmail = email;
		this.mRegistrationDate = registrationDate;
		this.mGCMId = GCMId;
		this.mLittleInterval = littleInterval;
		this.mBigInterval = bigInterval;
		this.mIsRunning = isRunning;
		this.mCondition = condition;
		this.mDialogText = dialogText;
		this.mDialogTitle = dialogTitle;
		this.mDataInterval = dataInterval;
		this.mPosition = position;
		this.mTheme = theme;
		this.mToken = token;
	}

	public int getUid() {
		return mUid;
	}

	public String getEmail() {
		return mEmail;
	}

	public Long getRegistrationDate() {
		return mRegistrationDate;
	}

	public String getGCMId() {
		return mGCMId;
	}

	public int getLittleInterval() {
		return mLittleInterval;
	}

	public int getBigInterval() {
		return mBigInterval;
	}

	public boolean isRunning() {
		return mIsRunning;
	}

	public int getCondition() {
		return mCondition;
	}

	public String getDialogText() {
		return mDialogText;
	}

	public int getPosition() {
		return mPosition;
	}
	
	public int getTheme() {
		return mTheme;
	}
	
	public String getTitle() {
		return mDialogTitle;
	}

	public int getDataInterval() {
		return mDataInterval;
	}
	
	public String getToken() {
		return mToken;
	}
}
