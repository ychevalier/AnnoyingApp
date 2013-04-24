package edu.hci.annoyingapp.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;
import edu.hci.annoyingapp.AnnoyingApplication;
import edu.hci.annoyingapp.protocol.Queries;
import edu.hci.annoyingapp.utils.Common;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities {

	public static final boolean DEBUG_MODE = AnnoyingApplication.DEBUG_MODE;
	public static final String TAG = ServerUtilities.class.getSimpleName();

	/**
	 * When registering (first and last step), number of attemps when failures.
	 */
	private static final int MAX_ATTEMPTS = 5;

	/**
	 * When registering (first and last step), number of time between trials.
	 */
	private static final int BACKOFF_MILLI_SECONDS = 2000;

	/**
	 * Useful random number generator.
	 */
	private static final Random random = new Random();

	/**
	 * Network operation that realises the registration first step to the 3th
	 * party server. This method should only be called by the associated task.
	 * 
	 * @param deviceId
	 *            Unique device id given by the system.
	 * @param email
	 *            Email entered by the user.
	 * @param version
	 *            Android API version id given by the system.
	 * 
	 * @return UID if successful, null otherwise.
	 */
	public static String startRegister(String deviceId, String email,
			String version) {

		// Creating the server PATH.
		String serverUrl = Common.SERVER_URL + Queries.START_PATH;
		// Creating the query.
		Map<String, String> atts = new HashMap<String, String>();
		atts.put(Queries.DEVICE_ID_ATT, deviceId);
		atts.put(Queries.EMAIL_ATT, email);
		atts.put(Queries.VERSION_ATT, version);

		// Multiple trials if necessary.
		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			try {
				String res = ServerUtilities.post(serverUrl, atts);
				return res;
			} catch (IOException e) {
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
					return null;
				}
				backoff *= 2;
			}
		}
		return null;
	}

	/**
	 * Method that realises that last step of the registration process which is
	 * sending the registration id given by google.
	 * 
	 * @param context
	 *            Reference to the context.
	 * @param regId
	 * @param uid
	 * @return
	 */
	public static boolean finishRegister(String regId, String uid) {
		String serverUrl = Common.SERVER_URL + Queries.FINISH_PATH;
		Map<String, String> atts = new HashMap<String, String>();
		atts.put(Queries.REG_ID_ATT, regId);
		atts.put(Queries.UID_ATT, uid);
		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);

		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			try {
				ServerUtilities.post(serverUrl, atts);
				return true;
			} catch (IOException e) {
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					Thread.currentThread().interrupt();
					return false;
				}
				backoff *= 2;
			}
		}
		return false;
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	public static boolean unregister(String uid) {

		String serverUrl = Common.SERVER_URL + Queries.UNREGISTER_PATH;

		Map<String, String> params = new HashMap<String, String>();
		params.put(Queries.UID_ATT, uid);

		boolean isSuccess = false;
		try {
			post(serverUrl, params);
			isSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
			// At this point the device is unregistered from GCM, but still
			// registered in the server.
			// We could try to unregister again, but it is not necessary:
			// if the server tries to send a message to the device, it will get
			// a "NotRegistered" error message and should unregister the device.
		}

		return isSuccess;
	}

	/**
	 * Helper that connect to a server, sends argument in post, and possibly
	 * return a string.
	 * 
	 * @param endpoint
	 *            Server where to connect.
	 * @param params
	 *            Post parameters.
	 * @return Content sent by the server.
	 * @throws IOException
	 *             If connection failed etc...
	 */
	public static String post(String endpoint, Map<String, String> params)
			throws IOException {
		URL url;

		String content = null;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		// Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();

			if (conn.getContentLength() > 0) {
				InputStream in = (InputStream) conn.getContent();
				byte[] tmpContent = new byte[conn.getContentLength()];
				in.read(tmpContent, 0, conn.getContentLength());
				in.close();
				content = new String(tmpContent);
			}

			if (status != HttpStatus.SC_OK) {
				throw new IOException("Post failed with error code " + status);
			}

		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return content;
	}

	public static boolean postXMLData(String content) {
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
		} catch (Exception e) {
			if (DEBUG_MODE) {
				e.printStackTrace();
			}
		} 
		return success;
	}
}
