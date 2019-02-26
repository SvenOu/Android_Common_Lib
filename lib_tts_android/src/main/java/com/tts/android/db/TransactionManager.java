package com.tts.android.db;

public class TransactionManager {

	public static void begin() {
		DatabaseManager.getInstance().wdb().beginTransaction();
	}
	
	public static void setSuccessful() {
		DatabaseManager.getInstance().wdb().setTransactionSuccessful();
	}
	
	public static void end() {
		DatabaseManager.getInstance().wdb().endTransaction();
	}
}
