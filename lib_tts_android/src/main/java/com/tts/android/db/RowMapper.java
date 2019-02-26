package com.tts.android.db;

import java.util.Date;

import android.database.Cursor;
import android.database.SQLException;

import com.tts.android.utils.DateUtils;

/**
 * 
 * @author Colin.Lin
 *
 * @param <T>
 */
public interface RowMapper<T> {
	
	/**
	 * Use {@link DbUtils}.getCursorValue(cursor, {column name in DB}, {@link Class}.class));<br/><br/>
	 * 
	 * if there is {@link Date} for T's property. should convert to {@link String} first, and convert to {@link Date} by use {@link DateFormatter}<br/>
	 * Example: {@link DateUtils}.sqliteToDateParser({@link DateUtils}.getCursorValue(cursor, {column name in DB}, String.class))
	 * 
	 * @param cursor
	 * @param rowNum
	 * @return
	 * @throws SQLException
	 */
	T mapRow(Cursor cursor, int rowNum) throws SQLException;
}
