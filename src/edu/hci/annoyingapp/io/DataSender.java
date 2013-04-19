package edu.hci.annoyingapp.io;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.model.XMLNode;
import edu.hci.annoyingapp.protocol.Queries;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Dialogs;
import edu.hci.annoyingapp.provider.AnnoyingAppContract.Interactions;
import edu.hci.annoyingapp.utils.Common;

public class DataSender {

	private static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	private static final String TAG = DataSender.class.getSimpleName();

	private static final String TAG_DIALOGS = "dialogs";
	private static final String ATT_UID = "uid";

	private static final String TAG_DIALOG = "dialog";
	private static final String ATT_START = "start";
	private static final String ATT_CONDITION = "condition";

	private static final String TAG_INTERACTION = "interaction";
	private static final String ATT_BUTTON = "button";
	private static final String ATT_DATETIME = "datetime";

	private static final String FAKE_CONTENT = "<?xml version='1.0' encoding='utf-8'?>"
			+ "<dialogs uid='26'>"
			+ "<dialog start='123456789' condition='1'>"
			+ "	<interaction button='1' datetime='127654321'/>"
			+ "	<interaction button='2' datetime='129865451'/>"
			+ "	<interaction button='1' datetime='127798756'/>"
			+ "</dialog>"
			+ "<dialog start='123457644' condition='1'>"
			+ "	<interaction button='1' datetime='128765421'/>"
			+ "	<interaction button='2' datetime='127986451'/>"
			+ "	<interaction button='1' datetime='128778756'/>"
			+ "</dialog>"
			+ "<dialog start='123975469' condition='2'>"
			+ "	<interaction button='1' datetime='128087991'/>"
			+ "	<interaction button='2' datetime='127767671'/>"
			+ "	<interaction button='1' datetime='120980986'/>"
			+ "</dialog>"
			+ "</dialogs>";

	private Context mContext;

	public DataSender(Context context) {
		mContext = context;
	}

	public boolean sendData(long last, String uid) {
		// return postData(FAKE_CONTENT);

		XMLNode root = new XMLNode(TAG_DIALOGS);
		Cursor dialogs = mContext.getContentResolver().query(
				Dialogs.CONTENT_URI, null, Dialogs.DIALOG_START + ">=?", new String[]{String.valueOf(last)}, null);

		if (DEBUG_MODE) {
			Log.d(TAG, "Date :  " + last);
		}

		while (dialogs.moveToNext()) {
			XMLNode dialog = new XMLNode(TAG_DIALOG);
			root.addChild(dialog);

			dialog.addAttribute(ATT_CONDITION, String.valueOf(dialogs
					.getInt(dialogs.getColumnIndex(Dialogs.DIALOG_CONDITION))));
			dialog.addAttribute(ATT_START, String.valueOf(dialogs
					.getLong(dialogs.getColumnIndex(Dialogs.DIALOG_START))));
			root.addAttribute(ATT_UID, uid);

			Cursor interactions = mContext.getContentResolver()
					.query(Dialogs.buildInteractionDirUri(String
							.valueOf(dialogs.getInt(dialogs
									.getColumnIndex(Dialogs.DIALOG_ID)))),
							null, null, null, null);

			if (DEBUG_MODE) {
				Log.d(TAG, "Interaction size : " + interactions.getCount());

				for (int i = 0; i < interactions.getColumnNames().length; i++) {
					Log.d(TAG, "Interactions Column " + i + " : "
							+ interactions.getColumnNames()[i]);
				}
			}

			while (interactions.moveToNext()) {
				XMLNode interaction = new XMLNode(TAG_INTERACTION);

				dialog.addChild(interaction);

				interaction
						.addAttribute(
								ATT_BUTTON,
								String.valueOf(interactions.getInt(interactions
										.getColumnIndex(Interactions.INTERACTION_BUTTON))));
				interaction
						.addAttribute(
								ATT_DATETIME,
								String.valueOf(interactions.getLong(interactions
										.getColumnIndex(Interactions.INTERACTION_DATETIME))));

			}

			interactions.close();
		}

		dialogs.close();

		if (DEBUG_MODE) {
			Log.d(TAG, root.toString());
		}

		return postData(root.toString());
	}

	private boolean postData(String content) {
		HttpPost httpPost = new HttpPost(Common.SERVER_URL
				+ Queries.SEND_DATA_PATH);
		httpPost.setHeader("Content-Type", "application/xml;charset=UTF-8");

		StringEntity entity;
		boolean success = false;
		try {

			entity = new StringEntity(content, HTTP.UTF_8);

			entity.setContentType("application/xml");
			httpPost.setEntity(entity);

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = (HttpResponse) httpclient
					.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());

				if (DEBUG_MODE) {
					Log.d(TAG, strResult);
				}
				success = true;
			}
		} catch (UnsupportedEncodingException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
			}
		}

		return success;
	}
}
