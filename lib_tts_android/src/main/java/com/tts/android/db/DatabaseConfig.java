package com.tts.android.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

public class DatabaseConfig {

	private String databaseName;
	private int initialVersion;
	private int latestVersion;
	
	private int dbChangeResId;
	
	private Context context;
	
	private TypedArray dbChangeArray;
	
	@SuppressLint("Recycle")
	public DatabaseConfig(Context context, String databaseName, 
			int dbChangeResId) {
		super();
		this.databaseName = databaseName;
		this.initialVersion = 0;
		this.context = context;
		if (null == this.context) {
            throw new IllegalArgumentException("context is null");
        }
		
		try {
			dbChangeArray = this.context.getResources().obtainTypedArray(dbChangeResId);
		} catch(Resources.NotFoundException e) {
			throw new Resources.NotFoundException("DB " + databaseName + " Can not find Array by Resource Id");
		}
		
		this.latestVersion = dbChangeArray.length();
		this.dbChangeResId = dbChangeResId;
	}
	
	@SuppressLint("Recycle")
	public DatabaseConfig(Context context) {
		super();
		this.initialVersion = 0;
		this.context = context;
		if (null == this.context) {
            throw new IllegalArgumentException("context is null");
        }
		
		try {
			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			int dbChangeResId = applicationInfo.metaData.getInt("DB_CHANGES");
			databaseName = applicationInfo.metaData.getString("DB_NAME");
			
			dbChangeArray = this.context.getResources().obtainTypedArray(dbChangeResId);
			
			this.dbChangeResId = dbChangeResId;
			this.latestVersion = dbChangeArray.length();
			
		} catch (NameNotFoundException e) {
			Log.e("", e.getMessage());
		}catch(Resources.NotFoundException e) {
			throw new Resources.NotFoundException("DB " 
					+ databaseName + " Can not find Array by Resource Id. " +
					"Please confirm there meta-data name 'DB_CHANGES' and 'DB_NAME' under <Application> tag");
		}
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public int getInitialVersion() {
		return initialVersion;
	}

	public int getLatestVersion() {
		return latestVersion;
	}

	public int getDbChangeResId() {
		return dbChangeResId;
	}

	public TypedArray getDbChangeArray() {
		return dbChangeArray;
	}

	@Override
	public String toString() {
		return "DatabaseConfig [databaseName=" + databaseName
				+ ", initialVersion=" + initialVersion + ", latestVersion="
				+ latestVersion + ", dbChangeResId=" + dbChangeResId
				+ ", context=" + context + ", dbChangeArray=" + dbChangeArray
				+ "]";
	}
}
