

package com.tts.android.db;

import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tts.android.utils.NumberUtils;
import com.tts.android.utils.StringUtils;

/**
 * Generic utility methods for working with Database. Mainly for internal use
 * within the framework, but also useful for custom Database access code.
 *

 */
public abstract class DbUtils {

	/**
	 * Constant that indicates an unknown (or unspecified) SQL type.
	 * @see java.sql.Types
	 */
	public static final int TYPE_UNKNOWN = Integer.MIN_VALUE;

	private static String getLogTag(){
		return DbUtils.class.getSimpleName();
	}

	private static void d(String msg, Throwable tr){
		Log.d(getLogTag(), msg, tr);
	}
	/**
	 * Close the given database and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual database code.
	 * @param con the database to close (may be {@code null})
	 */
	public static void closeDatabase(SQLiteDatabase con) {
		if (con != null) {
			try {
				con.close();
			}
			catch (SQLiteException ex) {
				d("Could not close database", ex);
			}
			catch (Throwable ex) {
				// We don't trust the JDBC driver: It might throw RuntimeException or Error.
				d( "Unexpected exception on closing database", ex);
			}
		}
	}

	/**
	 * Close the given Cursor and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual database code.
	 * @param rs the Cursor to close (may be {@code null})
	 */
	public static void closeCursor(Cursor cursor) {
		if (cursor != null) {
			try {
				cursor.close();
			}
			catch (SQLiteException ex) {
				d("Could not close Cursor", ex);
			}
			catch (Throwable ex) {
				d("Unexpected exception on closing Cursor", ex);
			}
		}
	}

	/**
	 * Retrieve a column value from a Cursor, using the specified value type.
	 * <p>Uses the specifically typed Cursor accessor methods, falling back to
	 * for unknown types.
	 * <p>Note that the returned value may not be assignable to the specified
	 * required type, in case of an unknown type. Calling code needs to deal
	 * with this case appropriately, e.g. throwing a corresponding exception.
	 * @param cs is the Cursor holding the data
	 * @param index is the column index
	 * @param requiredType the required value type (may be {@code null})
	 * @return the value object
	 * @throws SQLiteException if thrown by the Android API
	 */
	@SuppressWarnings("rawtypes")
	public static Object getCursorValue(Cursor cs, int index, Class requiredType) throws SQLiteException {
		if (cs.isNull(index)) {
            return null;
        }
		
		Object value = null;
		@SuppressWarnings("unused")
		boolean wasNullCheck = false;

		// Explicitly extract typed value, as far as possible.
		if (String.class.equals(requiredType)) {
			value = cs.getString(index);
		}
		else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
			value = cs.getInt(index) == 1 ? Boolean.TRUE : Boolean.FALSE;
			wasNullCheck = true;
		}
		else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
			value = cs.getBlob(index)[0];
			wasNullCheck = true;
		}
		else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
			value = cs.getShort(index);
			wasNullCheck = true;
		}
		else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
			value = cs.getInt(index);
			wasNullCheck = true;
		}
		else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
			value = cs.getLong(index);
			wasNullCheck = true;
		}
		else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
			value = cs.getFloat(index);
			wasNullCheck = true;
		}
		else if (double.class.equals(requiredType) || Double.class.equals(requiredType) ||
				Number.class.equals(requiredType)) {
			value = cs.getDouble(index);
			wasNullCheck = true;
		}
		else if (byte[].class.equals(requiredType)) {
			value = cs.getBlob(index);
		}
		else if (Date.class.equals(requiredType)) {
			String s = cs.getString(index);
			if (!StringUtils.hasText(s)) {
                value = null;
            }
			try {
				value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
				value = null;
			}
		}
		else if (Blob.class.equals(requiredType)) {
			value = cs.getBlob(index);
		}
		else {
			// Some unknown type desired -> rely on String.
			value = cs.getString(index);
		}

		return value;
	}
	
	/**
	 * Retrieve a JDBC column value from a Cursor, using the most appropriate
	 * value type. The returned value should be a detached value object, not having
	 * any ties to the active Cursor: in particular, it should not be a Blob or
	 * Clob object but rather a byte array respectively String representation.
	 * <p>Uses the {@code getObject(index)} method, but includes additional "hacks"
	 * to get around Oracle 10g returning a non-standard object for its TIMESTAMP
	 * datatype and a {@code java.sql.Date} for DATE columns leaving out the
	 * time portion: These columns will explicitly be extracted as standard
	 * {@code java.sql.Timestamp} object.
	 * @param cs is the Cursor holding the data
	 * @param index is the column index
	 * @return the value object
	 * @throws SQLiteException if thrown by the JDBC API
	 * @see Blob
	 * @see java.sql.Clob
	 * @see java.sql.Timestamp
	 */
	@SuppressLint("NewApi")
	public static Object getCursorValue(Cursor cs, int index) throws SQLiteException {

		if (android.os.Build.VERSION.SDK_INT >= 11){
			int type = cs.getType(index);
			if (Cursor.FIELD_TYPE_STRING == type){
				return cs.getString(index);
			}else if(Cursor.FIELD_TYPE_INTEGER == type){
				return cs.getInt(index);
			}else if (Cursor.FIELD_TYPE_FLOAT == type){
				return cs.getFloat(index);
			}else if (Cursor.FIELD_TYPE_BLOB == type){
				return cs.getBlob(index);
			}else if (Cursor.FIELD_TYPE_NULL == type){
				return null;
			}else{
				return cs.getString(index);
			}
		}else{
			String s = cs.getString(index);
			
			if (!StringUtils.hasText(s)) {
                return null;
            }
			
			Object value = null;
			
			try{
				value = NumberUtils.parseNumber(s, Integer.class);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if (value == null){
				try{
					value = NumberUtils.parseNumber(s, Float.class);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			return value == null ? s : value; 
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getCursorValue(Cursor cs, String columnName, Class<T> requiredType){
		int index = cs.getColumnIndex(columnName);
		
		if (index == -1 || cs.isNull(index)) {
            return null;
        }
		
		T value = null;
		@SuppressWarnings("unused")
		boolean wasNullCheck = false;

		// Explicitly extract typed value, as far as possible.
		if (String.class.equals(requiredType)) {
			value = (T) cs.getString(index);
		}
		else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
			value = (T) (cs.getInt(index) == 1 ? Boolean.TRUE : Boolean.FALSE);
		}
		else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
			value = (T) Byte.valueOf((cs.getBlob(index)[0]));
		}
		else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
			value = (T) Short.valueOf(cs.getShort(index));
		}
		else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
			value = (T) Integer.valueOf(cs.getInt(index));
		}
		else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
			value = (T) Long.valueOf(cs.getLong(index));
		}
		else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
			value = (T) Float.valueOf(cs.getFloat(index));
		}
		else if (double.class.equals(requiredType) || Double.class.equals(requiredType) ||
				Number.class.equals(requiredType)) {
			value = (T) Double.valueOf(cs.getDouble(index));
		}
		else if (byte[].class.equals(requiredType)) {
			value = (T) cs.getBlob(index);
		}
		else if (Date.class.equals(requiredType)) {
			String s = cs.getString(index);
			if (!StringUtils.hasText(s)) {
				value = null;	
			} else {
				try {
					value = (T) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).parse(s);
				} catch (ParseException e) {
					e.printStackTrace();
					value = null;
				}
			}
		}
		else if (Blob.class.equals(requiredType)) {
			value = (T) cs.getBlob(index);
		}
		else {
			// Some unknown type desired -> rely on String.
			value = (T) cs.getString(index);
		}

		return value;
	}

	/**
	 * Convert a column name with underscores to the corresponding property name using "camel case".  A name
	 * like "customer_number" would match a "customerNumber" property name.
	 * @param name the column name to be converted
	 * @return the name using "camel case"
	 */
	public static String convertUnderscoreNameToPropertyName(String name) {
		StringBuilder result = new StringBuilder();
		boolean nextIsUpper = false;
		if (name != null && name.length() > 0) {
			if (name.length() > 1 && "_".equals(name.substring(1, 2))) {
				result.append(name.substring(0, 1).toUpperCase());
			}
			else {
				result.append(name.substring(0, 1).toLowerCase());
			}
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if ("_".equals(s)) {
					nextIsUpper = true;
				}
				else {
					if (nextIsUpper) {
						result.append(s.toUpperCase());
						nextIsUpper = false;
					}
					else {
						result.append(s.toLowerCase());
					}
				}
			}
		}
		return result.toString();
	}
	
	

}
