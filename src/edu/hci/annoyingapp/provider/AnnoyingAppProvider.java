package edu.hci.annoyingapp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Dialogs;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Dialogs.SpecialQuery;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Interactions;
import edu.hci.annoyingapp.provider.AnnoyingDatabase.Tables;
import edu.hci.annoyingapp.utils.Common;

public class AnnoyingAppProvider extends ContentProvider {

	public static final Boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	protected static final String TAG = AnnoyingAppProvider.class
			.getSimpleName();

	private static final int DIALOGS = 10;
	private static final int DIALOGS_ID = 11;
	private static final int DIALOGS_ID_INTERACTIONS = 12;
	private static final int DIALOGS_SPECIAL = 13;

	private static final int INTERACTIONS = 20;
	private static final int INTERACTIONS_ID = 21;

	static final String SPECIAL = "special";
	static final String UNDERSCORE = "_";
	static final String SLASH = "/";
	static final String STAR = "*";
	static final String ALL = "all";

	private AnnoyingDatabase dbHelper;

	private static final UriMatcher sUriMatcher = buildUriMatcher();

	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = AnnoyingAppContract.CONTENT_AUTHORITY;

		// Order matters!
		matcher.addURI(authority, AnnoyingAppContract.PATH_DIALOGS + SLASH
				+ SPECIAL, DIALOGS_SPECIAL);
		
		matcher.addURI(authority, AnnoyingAppContract.PATH_DIALOGS, DIALOGS);
		matcher.addURI(authority, AnnoyingAppContract.PATH_DIALOGS + SLASH
				+ STAR, DIALOGS_ID);
		matcher.addURI(authority, AnnoyingAppContract.PATH_DIALOGS + SLASH
				+ STAR + SLASH + AnnoyingAppContract.PATH_INTERACTIONS,
				DIALOGS_ID_INTERACTIONS);

		matcher.addURI(authority, AnnoyingAppContract.PATH_INTERACTIONS,
				INTERACTIONS);
		matcher.addURI(authority, AnnoyingAppContract.PATH_INTERACTIONS + SLASH
				+ STAR, INTERACTIONS_ID);

		return matcher;
	}

	@Override
	public String getType(Uri uri) {
		final int match = sUriMatcher.match(uri);
		switch (match) {
		case DIALOGS:
			return AnnoyingAppContract.Dialogs.CONTENT_TYPE;
		case DIALOGS_ID:
			return AnnoyingAppContract.Dialogs.CONTENT_ITEM_TYPE;
		case DIALOGS_ID_INTERACTIONS:
			return AnnoyingAppContract.Interactions.CONTENT_TYPE;
		case INTERACTIONS:
			return AnnoyingAppContract.Interactions.CONTENT_TYPE;
		case INTERACTIONS_ID:
			return AnnoyingAppContract.Interactions.CONTENT_ITEM_TYPE;
		case DIALOGS_SPECIAL:
			return AnnoyingAppContract.Dialogs.CONTENT_TYPE;
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri );
		}
	}

	@Override
	public boolean onCreate() {
		dbHelper = new AnnoyingDatabase(getContext());
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);

		switch (match) {
		case DIALOGS:
		case DIALOGS_ID:
			long id = db.insertOrThrow(Tables.DIALOGS, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return ContentUris.withAppendedId(uri, id);
		case INTERACTIONS:
		case INTERACTIONS_ID:
			long idBis = db.insertOrThrow(Tables.INTERACTIONS, null, values);
			getContext().getContentResolver().notifyChange(uri, null);
			return ContentUris.withAppendedId(uri, idBis);
		case DIALOGS_ID_INTERACTIONS:
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		String seg = uri.getLastPathSegment();
		Integer id = Integer.valueOf(seg);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);

		switch (match) {
		case DIALOGS:
		case DIALOGS_ID:
			try {
				return db.update(Tables.DIALOGS, values,
						Dialogs._ID + "=" + id, null);
			} catch (Exception e) {
				return db.update(Tables.DIALOGS, values, selection,
						selectionArgs);
			}
		case INTERACTIONS:
		case INTERACTIONS_ID:
			try {
				return db.update(Tables.INTERACTIONS, values, Interactions._ID
						+ "=" + id, null);
			} catch (Exception e) {
				return db.update(Tables.INTERACTIONS, values, selection,
						selectionArgs);
			}
		case DIALOGS_ID_INTERACTIONS:
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String seg = uri.getLastPathSegment();
		Integer id = Integer.valueOf(seg);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);

		switch (match) {
		case DIALOGS:
		case DIALOGS_ID:
			try {
				return db.delete(Tables.DIALOGS, Dialogs._ID + "=" + id, null);
			} catch (Exception e) {
				return db.delete(Tables.DIALOGS, selection, selectionArgs);
			}
		case INTERACTIONS:
		case INTERACTIONS_ID:
			try {
				return db.delete(Tables.INTERACTIONS, Interactions._ID + "="
						+ id, null);
			} catch (Exception e) {
				return db.delete(Tables.INTERACTIONS, selection, selectionArgs);
			}
		case DIALOGS_ID_INTERACTIONS:
		default:
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		String groupBy = null;
		String having = null;
		
		int uriType = sUriMatcher.match(uri);
		switch (uriType) {
		case DIALOGS_ID:
			queryBuilder.setTables(Tables.DIALOGS);
			queryBuilder.appendWhere(Dialogs._ID + "="
					+ uri.getLastPathSegment());
			break;
		case DIALOGS:
			queryBuilder.setTables(Tables.DIALOGS);
			break;
		case INTERACTIONS_ID:
			queryBuilder.appendWhere(Interactions._ID + "="
					+ uri.getLastPathSegment());
			break;
		case INTERACTIONS:
			queryBuilder.setTables(Tables.INTERACTIONS);
			break;
		case DIALOGS_ID_INTERACTIONS:
			queryBuilder.setTables(Tables.INTERACTIONS);
			queryBuilder.appendWhere(Interactions.INTERACTION_DIALOG_ID + "="
					+ AnnoyingAppContract.Dialogs.getDialogIdFromDialogInteractions(uri));
			break;
		case DIALOGS_SPECIAL:
			queryBuilder.setTables(Tables.INTERACTIONS + ',' + Tables.DIALOGS);

			// TODO : Bad bad bad to overwrite the arguments....
			
			projection = SpecialQuery.PROJECTION;
			selection = Tables.DIALOGS +'.'+ Dialogs._ID +  '=' +  Tables.INTERACTIONS +'.'+ Interactions.INTERACTION_DIALOG_ID;
			groupBy = Tables.DIALOGS +'.'+ Dialogs._ID;
			having = Tables.INTERACTIONS +'.'+ Interactions.INTERACTION_BUTTON + '=' + Common.BUTTON_POSITIVE 
					+ " OR " + Tables.INTERACTIONS +'.'+ Interactions.INTERACTION_BUTTON + '=' + Common.BUTTON_OTHER;
			sortOrder = Tables.DIALOGS +'.'+ Dialogs.DIALOG_START;
			//queryBuilder.buildQuery(SpecialQuery.PROJECTION, null, null, null, sort, null);//sel, groupBy, having, sort, null);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI : " + uri);
		}

		Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
				projection, selection, selectionArgs, groupBy, having, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}
}
