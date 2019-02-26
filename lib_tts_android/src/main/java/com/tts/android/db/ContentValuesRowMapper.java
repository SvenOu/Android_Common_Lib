package com.tts.android.db;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;

import com.tts.android.utils.StringUtils;

public class ContentValuesRowMapper implements RowMapper<ContentValues> {
	private static Map<String, String> camelCaseNameMap = Collections.synchronizedMap(new HashMap<String, String>());
	
	@Override
	public ContentValues mapRow(Cursor cs, int rowNum) throws SQLException {
		int columnCount = cs.getColumnCount();
		
		ContentValues cvs = new ContentValues(columnCount);
		
		for (int i = 0; i < columnCount; i++) {
			String key = getColumnKey(cs.getColumnName(i));
			Object obj = getColumnValue(cs, i);
			if(obj instanceof String){
				cvs.put(key, (String)obj);
			}else if (obj instanceof Integer){
				cvs.put(key, (Integer)obj);
			}else if(obj instanceof Boolean){
				cvs.put(key, (Boolean)obj);
			}else if(obj instanceof Long){
				cvs.put(key, (Long)obj);
			}else if(obj instanceof Short){
				cvs.put(key, (Short)obj);
			}else if(obj instanceof Float){
				cvs.put(key, (Float)obj);
			}else if(obj instanceof Double){
				cvs.put(key, (Double)obj);
			}else if (obj instanceof Byte){
				cvs.put(key, (Byte)obj);
			}else if(obj instanceof byte[]){
				cvs.put(key, (byte[])obj);
			}
		}
		return cvs;
	}
	
	/**
	 * Determine the key to use for the given column in the column Map.
	 * @param columnName the column name as returned by the ResultSet
	 * @return the column key to use
	 */
	protected String getColumnKey(String columnName) {
		if (camelCaseNameMap.containsKey(columnName)) {
            return camelCaseNameMap.get(columnName);
        }
		
		String result = StringUtils.camelCaseName(columnName);
		camelCaseNameMap.put(columnName, result);

		return result;
	}

	/**
	 * Retrieve a JDBC object value for the specified column.
	 * <p>The default implementation uses the {@code getObject} method.
	 * Additionally, this implementation includes a "hack" to get around Oracle
	 * returning a non standard object for their TIMESTAMP datatype.
	 * @param cs is the ResultSet holding the data
	 * @param index is the column index
	 * @return the Object returned
	 * @see com.tts.common.db.DbUtils#getResultSetValue
	 */
	protected Object getColumnValue(Cursor cs, int index) throws SQLiteException {
		return DbUtils.getCursorValue(cs, index);
	}

}
