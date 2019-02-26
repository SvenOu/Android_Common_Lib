package com.tts.android.db;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.tts.android.utils.LinkedCaseInsensitiveMap;
import com.tts.android.utils.StringUtils;


/**
 * {@link RowMapper} implementation that creates a {@code java.util.Map}
 * for each row, representing all columns as key-value pairs: one
 * entry for each column, with the column name as key.
 *
 * <p>The Map implementation to use and the key to use for each column
 * in the column Map can be customized through overriding
 * {@link #createColumnMap} and {@link #getColumnKey}, respectively.
 *
 * <p><b>Note:</b> By default, ColumnMapRowMapper will try to build a linked Map
 * with case-insensitive keys, to preserve column order as well as allow any
 * casing to be used for column names. This requires Commons Collections on the
 * classpath (which will be autodetected). Else, the fallback is a standard linked
 * HashMap, which will still preserve column order but requires the application
 * to specify the column names in the same casing as exposed by the driver.

 */
public class ColumnMapRowMapper implements RowMapper<Map<String, Object>> {
	private static Map<String, String> camelCaseNameMap = Collections.synchronizedMap(new HashMap<String, String>());
	
	@Override
	public Map<String, Object> mapRow(Cursor cs, int rowNum) throws SQLiteException {
		int columnCount = cs.getColumnCount();
		Map<String, Object> mapOfColValues = createColumnMap(columnCount);
		
		for (int i = 0; i < columnCount; i++) {
			String key = getColumnKey(cs.getColumnName(i));
			Object obj = getColumnValue(cs, i);
			mapOfColValues.put(key, obj);
		}
		return mapOfColValues;
	}

	/**
	 * Create a Map instance to be used as column map.
	 * <p>By default, a linked case-insensitive Map will be created.
	 * @param columnCount the column count, to be used as initial
	 * capacity for the Map
	 * @return the new Map instance
	 * @see com.tts.common.util.LinkedCaseInsensitiveMap
	 */
	protected Map<String, Object> createColumnMap(int columnCount) {
		return new LinkedCaseInsensitiveMap<Object>(columnCount);
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
