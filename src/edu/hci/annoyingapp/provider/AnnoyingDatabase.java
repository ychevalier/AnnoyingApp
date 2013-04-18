package edu.hci.annoyingapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.DialogsColumns;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.InteractionsColumns;

public class AnnoyingDatabase extends SQLiteOpenHelper {
	
	public static final Boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	protected static final String TAG = AnnoyingDatabase.class.getSimpleName();
	
	private static final String DATABASE_NAME = "annoyingapp.db";
	private static final int DATABASE_VERSION = 1;

	interface Tables {
		String DIALOGS = "dialogs";
		String INTERACTIONS = "interactions";
	}

	public AnnoyingDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	private interface References {
		String DIALOG_ID = "REFERENCES " + Tables.DIALOGS + "(" + AnnoyingAppContract.Dialogs.DIALOG_ID + ")";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Tables.DIALOGS + " ("
				+ DialogsColumns.DIALOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DialogsColumns.DIALOG_START + " LONG,"
				+ DialogsColumns.DIALOG_CONDITION + " INTEGER,"
				+ DialogsColumns.DIALOG_POSITIVE_TEXT + " TEXT,"
				+ DialogsColumns.DIALOG_NEGATIVE_TEXT + " TEXT,"
				+ DialogsColumns.DIALOG_TEXT + " TEXT)");
		
		db.execSQL("CREATE TABLE " + Tables.INTERACTIONS + " ("
				+ InteractionsColumns.INTERACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ InteractionsColumns.INTERACTION_DATETIME + " LONG,"
				+ InteractionsColumns.INTERACTION_BUTTON + " INTEGER,"
				+ InteractionsColumns.INTERACTION_DIALOG_ID + " INTEGER " + References.DIALOG_ID + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (DEBUG_MODE)
			Log.d(TAG,
					"Upgrading database from version " + oldVersion
							+ " to " + newVersion
							+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + Tables.DIALOGS);
		db.execSQL("DROP TABLE IF EXISTS " + Tables.INTERACTIONS);
		onCreate(db);
	}
}