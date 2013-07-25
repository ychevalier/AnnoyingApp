package edu.hci.annoyingserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import edu.hci.annoyingserver.model.User;

public class MySQLAccess {

	private static Connection connect = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;

	/*
	private static final String DB_HOST = "localhost";
	private static final String DB_NAME = "HabitApp";
	private static final String DB_USER = "ychevalier";
	private static final String DB_PWD = "MysqlLocalPassword";

	//*/
	///*
	private static final String DB_HOST = "dbresearch.cs.bham.ac.uk";
	private static final String DB_NAME = "dialog_habit_db"; 
	private static final String DB_USER = "dialog_habit"; 
	private static final String DB_PWD ="nowsewbOy";
	//*/
	public static boolean setNewDefault(boolean running, String title,
			String text, int littleInterval, int bigInterval, int dataInterval,
			int condition, int theme, int position) {

		boolean isSuccess = false;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			int r = 0;
			if (running) {
				r = 1;
			}
			preparedStatement = connect
					.prepareStatement("ALTER TABLE users ALTER users.running SET DEFAULT "
							+ r);
			preparedStatement.execute();

			if (title != null) {
				preparedStatement = connect
						.prepareStatement("ALTER TABLE users ALTER users.dialog_title SET DEFAULT '"
								+ title + "'");
				preparedStatement.execute();
			}

			if (text != null) {
				preparedStatement = connect
						.prepareStatement("ALTER TABLE users ALTER users.dialog_text SET DEFAULT '"
								+ text + "'");
				preparedStatement.execute();
			}

			if (position != -1) {
				preparedStatement = connect
						.prepareStatement("ALTER TABLE users ALTER users.position SET DEFAULT '"
								+ position + "'");
				preparedStatement.execute();
			}

			if (theme != -1) {
				preparedStatement = connect
						.prepareStatement("ALTER TABLE users ALTER users.theme SET DEFAULT '"
								+ theme + "'");
				preparedStatement.execute();
			}

			if (littleInterval != -1) {
				preparedStatement = connect
						.prepareStatement("ALTER TABLE users ALTER users.little_interval SET DEFAULT "
								+ littleInterval);
				preparedStatement.execute();
			}

			if (bigInterval != -1) {
				preparedStatement = connect
						.prepareStatement("ALTER TABLE users ALTER users.big_interval SET DEFAULT "
								+ bigInterval);
				preparedStatement.execute();
			}

			if (dataInterval != -1) {
				preparedStatement = connect
						.prepareStatement("ALTER TABLE users ALTER users.data_interval SET DEFAULT "
								+ dataInterval);
				preparedStatement.execute();
			}

			if (condition != -1) {
				preparedStatement = connect
						.prepareStatement("ALTER TABLE users ALTER users.condition SET DEFAULT "
								+ condition);
				preparedStatement.execute();
			}

			isSuccess = true;
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			// throw e;
		} finally {
			close();
		}

		return isSuccess;
	}

	public static String getDefaultsReport() {

		StringBuilder out = new StringBuilder();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect.prepareStatement("DESCRIBE users");

			resultSet = preparedStatement.executeQuery();

			out.append("Field, Value\n");

			while (resultSet != null && resultSet.next()) {
				String s = resultSet.getString("Default");
				if (s != null) {
					out.append('\'');
					out.append(resultSet.getString("Field"));
					out.append('\'');
					out.append(',');
					out.append('\'');
					out.append(s);
					out.append('\'');
					out.append('\n');
				}
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			// throw e;
		} finally {
			close();
		}

		return out.toString();
	}

	public static String getConditionsReport() {

		StringBuilder out = new StringBuilder();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("SELECT id, name FROM conditions");

			resultSet = preparedStatement.executeQuery();

			out.append("id");
			out.append(',');
			out.append("name");

			while (resultSet != null && resultSet.next()) {
				out.append('\n');
				out.append(resultSet.getInt(1));
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(2));
				out.append('\'');
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			// throw e;
		} finally {
			close();
		}

		return out.toString();
	}

	public static String getButtonsReport() {

		StringBuilder out = new StringBuilder();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("SELECT id, name FROM buttons");

			resultSet = preparedStatement.executeQuery();

			out.append("id");
			out.append(',');
			out.append("name");

			while (resultSet != null && resultSet.next()) {
				out.append('\n');
				out.append(resultSet.getInt(1));
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(2));
				out.append('\'');
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			// throw e;
		} finally {
			close();
		}

		return out.toString();
	}
	
	public static String getTokensReport() {

		StringBuilder out = new StringBuilder();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("SELECT user_id, token FROM tokens ORDER BY user_id DESC");

			resultSet = preparedStatement.executeQuery();

			out.append("uid");
			out.append(',');
			out.append("token");

			while (resultSet != null && resultSet.next()) {
				out.append('\n');
				out.append(resultSet.getInt(1));
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(2));
				out.append('\'');
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			// throw e;
		} finally {
			close();
		}

		return out.toString();
	}


	public static String getUsersReport() {

		StringBuilder out = new StringBuilder();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("SELECT uid, mail, version, registration_datetime, running, current_condition, dialog_title, dialog_text, position, theme, token FROM users, tokens WHERE user_id = uid");

			resultSet = preparedStatement.executeQuery();

			out.append("uid");
			out.append(',');
			out.append("email");
			out.append(',');
			out.append("android_version");
			out.append(',');
			out.append("registration_datetime");
			out.append(',');
			out.append("running");
			out.append(',');
			out.append("current_condition");
			out.append(',');
			out.append("title");
			out.append(',');
			out.append("text");
			out.append(',');
			out.append("position");
			out.append(',');
			out.append("theme");
			out.append(',');
			out.append("token");

			while (resultSet != null && resultSet.next()) {
				out.append('\n');
				out.append(resultSet.getInt(1));
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(2));
				out.append('\'');
				out.append(',');
				out.append(resultSet.getInt(3));
				out.append(',');
				out.append(resultSet.getLong(4));
				out.append(',');
				out.append(resultSet.getBoolean(5));
				out.append(',');
				out.append(resultSet.getInt(6));
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(7));
				out.append('\'');
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(8));
				out.append('\'');
				out.append(',');
				out.append(resultSet.getInt(9));
				out.append(',');
				out.append(resultSet.getInt(10));
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(11));
				out.append('\'');
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			// throw e;
		} finally {
			close();
		}

		return out.toString();
	}

	public static String getDropoutsReport(int time) {

		StringBuilder out = new StringBuilder();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			Calendar cal = Calendar.getInstance();
			long last = cal.getTimeInMillis() - time * 60 * 60 * 1000;

			preparedStatement = connect
					.prepareStatement("SELECT dialogs.uid, mail, registration_datetime, running, MAX(start) AS last, gcm_registration_id FROM users, dialogs WHERE users.uid = dialogs.uid GROUP BY dialogs.uid HAVING last <=? UNION SELECT uid, mail, registration_datetime, running, 0, gcm_registration_id FROM users WHERE uid NOT IN (SELECT dialogs.uid FROM dialogs)");
			preparedStatement.setLong(1, last);

			resultSet = preparedStatement.executeQuery();

			out.append("uid");
			out.append(',');
			out.append("email");
			out.append(',');
			out.append("registration_datetime");
			out.append(',');
			out.append("running");
			out.append(',');
			out.append("last");
			out.append(',');
			out.append("is_google_registered");

			while (resultSet != null && resultSet.next()) {
				out.append('\n');
				out.append(resultSet.getInt(1));
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(2));
				out.append('\'');
				out.append(',');
				out.append(resultSet.getLong(3));
				out.append(',');
				out.append(resultSet.getBoolean(4));
				out.append(',');
				out.append(resultSet.getLong(5));
				out.append(',');
				out.append(resultSet.getString(6) == null ? false : true);
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			// throw e;
		} finally {
			close();
		}

		return out.toString();
	}

	public static String getDialogsReport() {

		StringBuilder out = new StringBuilder();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("SELECT id, uid, start, dialogs.condition, title, text, theme, position, image, top_image, bottom_image FROM dialogs");

			resultSet = preparedStatement.executeQuery();

			out.append("id");
			out.append(',');
			out.append("uid");
			out.append(',');
			out.append("start");
			out.append(',');
			out.append("condition");
			out.append(',');
			out.append("title");
			out.append(',');
			out.append("text");
			out.append(',');
			out.append("theme");
			out.append(',');
			out.append("position");
			out.append(',');
			out.append("image");
			out.append(',');
			out.append("top_image");
			out.append(',');
			out.append("bottom_image");

			while (resultSet != null && resultSet.next()) {
				out.append('\n');
				out.append(resultSet.getInt(1));
				out.append(',');
				out.append(resultSet.getInt(2));
				out.append(',');
				out.append(resultSet.getLong(3));
				out.append(',');
				out.append(resultSet.getLong(4));
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(5));
				out.append('\'');
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(6));
				out.append('\'');
				out.append(',');
				out.append(resultSet.getInt(7));
				out.append(',');
				out.append(resultSet.getInt(8));
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(9));
				out.append('\'');
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(10));
				out.append('\'');
				out.append(',');
				out.append('\'');
				out.append(resultSet.getString(11));
				out.append('\'');
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			// throw e;
		} finally {
			close();
		}

		return out.toString();
	}

	public static String getInteractionsReport() {

		StringBuilder out = new StringBuilder();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("SELECT id, dialog_id, datetime, button FROM interactions");

			resultSet = preparedStatement.executeQuery();

			out.append("id");
			out.append(',');
			out.append("dialog_id");
			out.append(',');
			out.append("datetime");
			out.append(',');
			out.append("button");

			while (resultSet != null && resultSet.next()) {
				out.append('\n');
				out.append(resultSet.getInt(1));
				out.append(',');
				out.append(resultSet.getInt(2));
				out.append(',');
				out.append(resultSet.getLong(3));
				out.append(',');
				out.append(resultSet.getInt(4));
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			// throw e;
		} finally {
			close();
		}

		return out.toString();
	}
	
	public static String getConfig(String key) {

		String value = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("SELECT value FROM config WHERE config.key LIKE ?");
			preparedStatement.setString(1, key);
			resultSet = preparedStatement.executeQuery();

			if(resultSet != null && resultSet.next()) {
				value = resultSet.getString(1);
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			// throw e;
		} finally {
			close();
		}

		return value;
	}

	public static int insertDialog(int uid, long date, int condition,
			String title, String text, int position, int theme, String image,
			String topImage, String bottomImage) {
		int id = -1;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			// Check if dialog exists
			preparedStatement = connect
					.prepareStatement("SELECT * FROM dialogs WHERE start=? AND uid=?");
			preparedStatement.setLong(1, date);
			preparedStatement.setInt(2, uid);

			resultSet = preparedStatement.executeQuery();

			if (resultSet != null && resultSet.next()) {
				id = -1;
			} else {
				preparedStatement = connect
						.prepareStatement(
								"INSERT INTO dialogs (uid, start, dialogs.condition, title, text, position, theme, image, top_image, bottom_image) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
								Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setInt(1, uid);
				preparedStatement.setLong(2, date);
				preparedStatement.setInt(3, condition);
				preparedStatement.setString(4, title);
				preparedStatement.setString(5, text);
				preparedStatement.setInt(6, position);
				preparedStatement.setInt(7, theme);
				preparedStatement.setString(8, image);
				preparedStatement.setString(9, topImage);
				preparedStatement.setString(10, bottomImage);

				preparedStatement.executeUpdate();
				resultSet = preparedStatement.getGeneratedKeys();

				if (resultSet != null && resultSet.next()) {
					id = resultSet.getInt(1);
				}
			}

		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
			id = -1;
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			id = -1;
		} catch (Exception e) {
			// Probably a problem of constraints, i.e dialogs is already in
			// database.
			// e.printStackTrace();
			id = -1;
		} finally {
			close();
		}

		return id;
	}

	public static int insertInteraction(int dialogId, long date, int button) {
		int id = -1;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement(
							"INSERT INTO interactions (dialog_id, datetime, button) VALUES (?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, dialogId);
			preparedStatement.setLong(2, date);
			preparedStatement.setInt(3, button);

			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();

			if (resultSet != null && resultSet.next()) {
				id = resultSet.getInt(1);
			}

		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
			id = -1;
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			// e.printStackTrace();
			id = -1;
		} finally {
			close();
		}

		return id;
	}

	public static List<String> getRegId(List<Integer> listUid) {
		List<String> regIdList = new LinkedList<String>();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			StringBuilder query = new StringBuilder(
					"SELECT gcm_registration_id FROM users WHERE uid IN (");
			for (int i = 0; i < listUid.size(); i++) {
				if (i == 0) {
					query.append('?');
				} else {
					query.append(", ?");
				}
			}

			query.append(')');
			query.append(" AND gcm_registration_id IS NOT NULL");

			preparedStatement = connect.prepareStatement(query.toString());

			for (int i = 0; i < listUid.size(); i++) {
				preparedStatement.setInt(i + 1, listUid.get(i));
			}

			resultSet = preparedStatement.executeQuery();

			while (resultSet != null && resultSet.next()) {
				regIdList.add(resultSet.getString(1));
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
			regIdList = null;
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			// e.printStackTrace();
			// throw e;
			regIdList = null;
		} finally {
			close();
		}

		return regIdList;
	}

	public static List<User> getAvailableUsers() {

		List<User> users = new LinkedList<User>();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("SELECT uid, mail, registration_datetime, gcm_registration_id, little_interval, big_interval, running, current_condition, dialog_text, dialog_title, data_interval, position, theme, token FROM users, tokens WHERE gcm_registration_id IS NOT NULL AND user_id = uid");
			resultSet = preparedStatement.executeQuery();

			while (resultSet != null && resultSet.next()) {
				int uid = resultSet.getInt(1);
				String mail = resultSet.getString(2);
				Long datetime = resultSet.getLong(3);
				String regId = resultSet.getString(4);
				int littleInterval = resultSet.getInt(5);
				int bigInterval = resultSet.getInt(6);
				boolean isRunning = resultSet.getBoolean(7);
				int condition = resultSet.getInt(8);
				String dialogText = resultSet.getString(9);
				String dialogTitle = resultSet.getString(10);
				int dataInterval = resultSet.getInt(11);
				int theme = resultSet.getInt(12);
				int position = resultSet.getInt(13);
				String token = resultSet.getString(14);
				User u = new User(uid, mail, datetime, regId, littleInterval,
						bigInterval, isRunning, condition, dialogText,
						dialogTitle, dataInterval, theme, position, token);

				users.add(u);
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
			users = null;
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			// e.printStackTrace();
			// throw e;
			users = null;
		} finally {
			close();
		}

		return users;
	}

	public static User start_register(String deviceId, String email, int version) {
		// int uid = -1;
		User user = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("SELECT uid, mail, current_condition, little_interval, big_interval, dialog_text, running, data_interval, dialog_title, position, theme, token FROM users, tokens WHERE device_id=? AND uid = user_id");
			preparedStatement.setString(1, deviceId);
			resultSet = preparedStatement.executeQuery();

			// If the user is already registered (depending on the device ID).
			if (resultSet != null && resultSet.next()) {
				int uid = resultSet.getInt(1);
				String oldEmail = resultSet.getString(2);
				int condition = resultSet.getInt(3);
				int littleInterval = resultSet.getInt(4);
				int bigInterval = resultSet.getInt(5);
				String dialogText = resultSet.getString(6);
				boolean running = resultSet.getBoolean(7);
				int dataInterval = resultSet.getInt(8);
				String dialogTitle = resultSet.getString(9);
				int position = resultSet.getInt(10);
				int theme = resultSet.getInt(11);
				String token = resultSet.getString(12);

				user = new User(uid, oldEmail, Long.valueOf(0), null,
						littleInterval, bigInterval, running, condition,
						dialogText, dialogTitle, dataInterval, theme, position, token);

				if (!email.equals(oldEmail)) {
					preparedStatement = connect
							.prepareStatement("UPDATE users SET  mail=? WHERE uid=?");
					preparedStatement.setString(1, email);
					preparedStatement.setInt(2, uid);
					preparedStatement.executeUpdate();
				}
				
			// If it is the first time we see this device.
			} else {
				preparedStatement = connect
						.prepareStatement(
								"INSERT INTO users (device_id, mail, version, registration_datetime) VALUES (?, ?, ?, ?)",
								Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, deviceId);
				preparedStatement.setString(2, email);
				preparedStatement.setInt(3, version);
				Calendar cal = Calendar.getInstance();
				preparedStatement.setLong(4, cal.getTimeInMillis());

				preparedStatement.executeUpdate();
				resultSet = preparedStatement.getGeneratedKeys();

				// If the user has been registered.
				if (resultSet != null && resultSet.next()) {
					int uid = resultSet.getInt(1);
					
					preparedStatement = connect
							.prepareStatement("SELECT token FROM tokens WHERE user_id IS NULL");
					resultSet = preparedStatement.executeQuery();
					
					String token = null;
					if (resultSet != null && resultSet.next()) {
						token = resultSet.getString(1);
					}
					
					preparedStatement = connect
							.prepareStatement("UPDATE tokens SET user_id=? WHERE token=?");
					preparedStatement.setInt(1, uid);
					preparedStatement.setString(2, token);
					preparedStatement.executeUpdate();
					
					preparedStatement = connect
							.prepareStatement("SELECT uid, mail, current_condition, little_interval, big_interval, dialog_text, running, dialog_title, data_interval, position, theme, token FROM users, tokens WHERE device_id=? AND user_id = uid");
					preparedStatement.setString(1, deviceId);
					resultSet = preparedStatement.executeQuery();

					if (resultSet != null && resultSet.next()) {
						uid = resultSet.getInt(1);
						String oldEmail = resultSet.getString(2);
						int condition = resultSet.getInt(3);
						int littleInterval = resultSet.getInt(4);
						int bigInterval = resultSet.getInt(5);
						String dialogText = resultSet.getString(6);
						boolean running = resultSet.getBoolean(7);
						String dialogTitle = resultSet.getString(8);
						int dataInterval = resultSet.getInt(9);
						int position = resultSet.getInt(10);
						int theme = resultSet.getInt(11);
						token = resultSet.getString(12);

						user = new User(uid, oldEmail, Long.valueOf(0), null,
								littleInterval, bigInterval, running,
								condition, dialogText, dialogTitle,
								dataInterval, theme, position, token);
					}
				}
			}

		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
			user = null;
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			user = null;
		} finally {
			close();
		}

		return user;
	}

	public static boolean finish_register(int uid, String regId) {

		boolean isSuccess = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("UPDATE users SET  gcm_registration_id=?, gcm_registration_datetime=? WHERE uid=?");
			preparedStatement.setString(1, regId);
			Calendar cal = Calendar.getInstance();
			preparedStatement.setLong(2, cal.getTimeInMillis());
			preparedStatement.setInt(3, uid);
			int rows = preparedStatement.executeUpdate();

			if (rows == 1) {
				isSuccess = true;
			}

		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
			isSuccess = false;
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			// e.printStackTrace();
			isSuccess = false;
		} finally {
			close();
		}

		return isSuccess;
	}

	public static boolean update(String regId, boolean run, int condition,
			String dialog, int littleInterval, int bigInterval, String survey,
			String title, int dataInterval, int position, int theme) {

		boolean isSuccess = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			StringBuilder query = new StringBuilder();
			query.append("UPDATE users SET ");

			query.append("running=");
			query.append(run ? '1' : '0');

			if (condition != -1) {
				query.append(", ");
				query.append("current_condition=");
				query.append(condition);
			}

			if (position != -1) {
				query.append(", ");
				query.append("position=");
				query.append(position);
			}

			if (theme != -1) {
				query.append(", ");
				query.append("theme=");
				query.append(theme);
			}

			if (dialog != null) {
				query.append(", ");
				query.append("dialog_text=");
				query.append("'");
				query.append(dialog);
				query.append("'");
			}

			if (littleInterval != -1) {
				query.append(", ");
				query.append("little_interval=");
				query.append(littleInterval);
			}

			if (bigInterval != -1) {
				query.append(", ");
				query.append("big_interval=");
				query.append(bigInterval);
			}

			if (title != null) {
				query.append(", ");
				query.append("dialog_title=");
				query.append("'");
				query.append(title);
				query.append("'");
			}

			if (dataInterval != -1) {
				query.append(", ");
				query.append("data_interval=");
				query.append(dataInterval);
			}

			query.append(" WHERE gcm_registration_id='");
			query.append(regId);
			query.append("'");

			preparedStatement = connect.prepareStatement(query.toString());
			int row = preparedStatement.executeUpdate();

			if (row == 1) {
				isSuccess = true;
			}

			// TODO INSERT INTO SURVEYS
			// query.setLength(0);
			// query.append("INSERT INTO surveys")
			// preparedStatement = connect
			// .prepareStatement(query);
			// int row = preparedStatement.executeUpdate();

		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
			isSuccess = false;
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			e.printStackTrace();
			isSuccess = false;
		} finally {
			close();
		}

		return isSuccess;
	}

	public static boolean update(String oldRegId, String newRegId) {
		boolean isSuccess = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("UPDATE users SET gcm_registration_id=? WHERE gcm_registration_id=?");
			preparedStatement.setString(1, newRegId);
			preparedStatement.setString(2, oldRegId);
			int row = preparedStatement.executeUpdate();

			if (row == 1) {
				isSuccess = true;
			}

		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
			isSuccess = false;
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			// e.printStackTrace();
			isSuccess = false;
		} finally {
			close();
		}

		return isSuccess;
	}

	public static boolean unregister(String regId) {
		return update(regId, null);
	}

	public static boolean unregister(int uid) {
		boolean isSuccess = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST
					+ "/" + DB_NAME + "?" + "user=" + DB_USER + "&password="
					+ DB_PWD);

			preparedStatement = connect
					.prepareStatement("UPDATE users SET gcm_registration_id=null WHERE uid=?");
			preparedStatement.setInt(1, uid);
			int row = preparedStatement.executeUpdate();

			if (row == 1) {
				isSuccess = true;
			}
		} catch (ClassNotFoundException e) {
			// This should never happened.
			e.printStackTrace();
			isSuccess = false;
		} catch (SQLException e) {
			// If the parameters are wrong, or database is not available...
			// e.printStackTrace();
			isSuccess = false;
		} finally {
			close();
		}

		return isSuccess;
	}

	// You need to close the resultSet
	private static void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (connect != null) {
				connect.close();
			}

			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			resultSet = null;
			connect = null;
			preparedStatement = null;
		}
	}

}
