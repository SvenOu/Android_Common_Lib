package com.tts.android.db;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tts.android.common.exception.NullContextException;
import com.tts.android.mybatic.MybatisManager;
import com.tts.android.utils.StringUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DatabaseManager {

	private static final String TAG = "DatabaseManager";

	private volatile static DatabaseManager instance;

	private Map<String, SQLiteOpenHelper> dbOpenHelperMap;
	private Context context;
	private String defaultDatabaseName;
	private boolean printDetailLog = true;

	public static DatabaseManager getInstance() {
		if (instance == null) {
			Log.i(TAG, "DatabaseManager instance is null, create it");
			synchronized (DatabaseManager.class) {
				if (instance == null) {
					instance = new DatabaseManager();
				}
			}
		}
//		else{
//			if(instance.printDetailLog){
//				Log.i(TAG, "DatabaseManager instance is NOT null");
//			}
//		}
		return instance;
	}

	private DatabaseManager(){
		dbOpenHelperMap = Collections.synchronizedMap(new HashMap<String, SQLiteOpenHelper>());
	}

	public synchronized void closeAll(){
		this.context = null;
		for (String key : this.dbOpenHelperMap.keySet()) {
			this.dbOpenHelperMap.get(key).close();
			this.dbOpenHelperMap.put(key, null);
		}
	}

	public synchronized void setContext(Context context, DatabaseConfig dbConfig){
		String databaseName = dbConfig.getDatabaseName();

		if (!StringUtils.hasText(databaseName)) {
            throw new RuntimeException("databaseName of DatabaseConfig cannot be empty");
        }

		this.setContext(context, databaseName, dbConfig);
	}

	public synchronized void setMybatisContext(Application application, DatabaseConfig dbConfig, int mybatisResId){
		setContext(application, dbConfig);
		MybatisManager.getInstance().initConfig(application, mybatisResId);
	}

	//see line 142 DefaultDatabaseUpdater
	/**
	 * you must set the context immediately you get the DatabaseManager instance
	 * @param context
	 * @param dbConfigs
	 * @param defaultDatabaseName
	 */
	public synchronized void setContext(Context context, String defaultDatabaseName, DatabaseConfig... dbConfigs){
		if (null != this.context) {
			Log.i(TAG, "Context is not allowed to change after it has been set");
			return;
		}

		if (context == null) {
            throw new IllegalArgumentException("context is null");
        }

		this.context = context.getApplicationContext();

		this.ensureDbOpenHelpers(dbConfigs);

		if (!StringUtils.hasText(defaultDatabaseName)) {
            throw new NullContextException("defaultDatabaseName is empty");
        }

		if (null == this.dbOpenHelperMap.get(defaultDatabaseName)) {
            throw new RuntimeException("dbOpenHelper for " + defaultDatabaseName + " was not intialized");
        }

		this.defaultDatabaseName = defaultDatabaseName;
	}

	public SQLiteDatabase rdb(String databaseName){
		return this.getReadableDatabase(databaseName);
	}

	public SQLiteDatabase wdb(String databaseName){
		return this.getWritableDatabase(databaseName);
	}

	public SQLiteDatabase rdb(){
		if (!StringUtils.hasText(defaultDatabaseName)) {
            throw new NullContextException("defaultDatabaseName is empty, you must preset the defaultDatabaseName before retrieving the database object");
        }

		return this.getReadableDatabase(defaultDatabaseName);
	}

	public SQLiteDatabase wdb(){
		if (!StringUtils.hasText(defaultDatabaseName)) {
            throw new NullContextException("defaultDatabaseName is empty, you must preset the defaultDatabaseName before retrieving the database object");
        }

		return this.getWritableDatabase(defaultDatabaseName);
	}

	public Context getContext(){
		return this.context;
	}

	private void ensureDbOpenHelpers(DatabaseConfig[] dbConfigs){
		for (DatabaseConfig dbConfig : dbConfigs){
			this.ensureDbOpenHelper(dbConfig);
		}
	}

	private void ensureDbOpenHelper(DatabaseConfig dbConfig){
		SQLiteOpenHelper dbOpenHelper = this.dbOpenHelperMap.get(dbConfig.getDatabaseName());
		if (null != dbOpenHelper) {
            return;
        }

		if (this.context == null) {
            throw new NullContextException("context is null, you must preset the context before retrieving the database object");
        }

		dbOpenHelper = new DefaultDBOpenHelper(this.context, dbConfig);
		dbOpenHelper.getReadableDatabase();

		this.dbOpenHelperMap.put(dbConfig.getDatabaseName(), dbOpenHelper);
	}

	private SQLiteOpenHelper getDbOpenHelperForSure(String databaseName){
		SQLiteOpenHelper dbOpenHelper = this.dbOpenHelperMap.get(databaseName);

		if (null == dbOpenHelper) {
            throw new RuntimeException("dbOpenHelper was not intialized");
        }

		return dbOpenHelper;
	}

	private SQLiteDatabase getReadableDatabase(String databaseName){
		SQLiteOpenHelper dbOpenHelper = getDbOpenHelperForSure(databaseName);

		return dbOpenHelper.getReadableDatabase();
	}

	private SQLiteDatabase getWritableDatabase(String databaseName){
		SQLiteOpenHelper dbOpenHelper = getDbOpenHelperForSure(databaseName);

		return dbOpenHelper.getWritableDatabase();
	}

	private class DefaultDBOpenHelper extends SQLiteOpenHelper {

		private final static String TAG = "DefaultDBOpenHelper";
		private DatabaseConfig dbConfig;
		private DatabaseUpdater dbUpdater;

		public DefaultDBOpenHelper(Context context, DatabaseConfig dbConfig) {
			super(context, dbConfig.getDatabaseName(), null, dbConfig.getLatestVersion());

			this.dbConfig = dbConfig;
			this.dbUpdater = new DefaultDatabaseUpdater(dbConfig);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.w(TAG, "DB " + dbConfig.getDatabaseName() + " Creating from version " + dbConfig.getInitialVersion() + " to " + dbConfig.getLatestVersion());
			updateDatabase(db, dbConfig.getInitialVersion(), dbConfig.getLatestVersion());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
			Log.w(TAG, "DB " + dbConfig.getDatabaseName() + " Upgrading from version " + oldVersion + " to " + newVersion);
			updateDatabase(db, oldVersion, newVersion);
		}

		private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
			if (null != dbConfig.getDbChangeArray()) {
				dbUpdater.updataDatabase(db);
			}
		}
	}

	interface DatabaseUpdater{
		/**
		 * @deprecated
		 * @param db
		 * @param oldVersion
		 * @param newVersion
		 */
		/**
		 *
		 * @param db
		 */
		void updataDatabase(SQLiteDatabase db);
	}


	class DefaultDatabaseUpdater implements DatabaseUpdater{

		private static final String TAG = "DefaultDatabaseUpdater";

		private DatabaseConfig dbConfig;

		DefaultDatabaseUpdater(DatabaseConfig dbConfig){
			this.dbConfig = dbConfig;
		}

		/**
		 * *********************************************************************************************************************
		 */

		@SuppressLint("Recycle")
		@Override
		public void updataDatabase(SQLiteDatabase db) {

			int currentDbVersion = getCurrentDbVersion(db);
			Log.i(TAG, "DB " + dbConfig.getDatabaseName() + " current database version: " + currentDbVersion);

			int maxVersion = dbConfig.getLatestVersion();

			if (currentDbVersion >= maxVersion) {
				Log.i(TAG, "DB " + dbConfig.getDatabaseName() + " is the lastest version");
				return;
			}

			TypedArray array = dbConfig.getDbChangeArray();

			if (null != array) {
				for (int i = 0; i < array.length(); i++) {
					int resId = array.getResourceId(i, 0);
					execSqlFromResId(db, resId, currentDbVersion, maxVersion);
				}
			}
			else {
				Log.w(TAG, "DB " + dbConfig.getDatabaseName() + " No available raw resId input from DBConfig.");
			}

		}

		/**
		 * Query Field "param_value" From conf_general Where "param_name" is "DB_VERSION".
		 * if there is no such table, return 0;
		 * @param db
		 * @return
		 */
		private int getCurrentDbVersion(SQLiteDatabase db) {
			int currentDbVersion = 0;
			try {
				Cursor c = db.query("conf_general", null, "param_name = ?", new String[]{"DB_VERSION"}, null, null, null);
				c.moveToFirst();
				int version = c.getInt(c.getColumnIndex("param_value"));
				currentDbVersion = version;
			} catch(Exception e) {
				Log.i(TAG, "DB " + dbConfig.getDatabaseName() + " will be intialized.");
			}
			return currentDbVersion;
		}



		private void execSqlFromResId(SQLiteDatabase db, int resId, int currentVersion, int toVersion) {
			try {
				//Read dbchang_x.xml File into InputSteam
				InputStream is = context.getResources().openRawResource(resId);

				// Create a DocumentBuilder
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

				// parse a xml file content into a Document Object
				execSqlFromDocument(db, builder.parse(is), currentVersion, toVersion);

			} catch (ParserConfigurationException e) {
				Log.e(TAG, "DB " + dbConfig.getDatabaseName(), e);
			} catch (SAXException e) {
				Log.e(TAG, "DB " + dbConfig.getDatabaseName() + "failed to parse dbchg file for " + context.getResources().getResourceName(resId) , e);
			} catch (IOException e) {
				Log.e(TAG, "DB " + dbConfig.getDatabaseName() + ".execSqlFromResId()", e);
			}
		}

		private void execSqlFromDocument(SQLiteDatabase db, Document document,  int currentVersion, int toVersion) {
			String sql = null;

			Element sqlElement = document.getDocumentElement();

			String sqlVersion = sqlElement.getAttribute("version");
			String msg = "";
			if (!StringUtils.hasText(sqlVersion)) {
				msg = "DB " + dbConfig.getDatabaseName() + " no attribute: 'version' at Node:" + sqlElement.getLocalName();
				Log.e(TAG, msg);
				throw new RuntimeException(msg);
			}

			if (null == Integer.valueOf(sqlVersion)) {
				msg = "DB " + dbConfig.getDatabaseName() + " attribute: 'version' :" + sqlVersion + ", shoulb be number.";
				Log.e(TAG, msg);
				throw new RuntimeException(msg);
			}

			if (Integer.valueOf(sqlVersion) <= currentVersion) {
                return;
            }

			try {
				Log.i(TAG, "DB " + dbConfig.getDatabaseName() + " ---------- Start upgrading database from " + currentVersion + " to version " + sqlVersion);
				db.beginTransaction();

				NodeList statements = sqlElement.getElementsByTagName("statement");

				for (int i = 0; i < statements.getLength(); i++) {
					Node node = statements.item(i);
					sql = node.getTextContent();
					db.execSQL(sql);
				}

				currentVersion = Integer.valueOf(sqlVersion);

				db.setTransactionSuccessful();
				db.endTransaction();
				Log.i(TAG, "DB " + dbConfig.getDatabaseName() + " ---------- Finish upgrading database from " + currentVersion + " to version " + sqlVersion);

			} catch(SQLException e) {
				Log.e(TAG, "DB " + dbConfig.getDatabaseName() + " Exec sql file failed, statement: " + sql, e);
			}
		}
	}
	public boolean isPrintDetailLog() {
		return printDetailLog;
	}
	public void setPrintDetailLog(boolean printDetailLog) {
		this.printDetailLog = printDetailLog;
	}
}