package com.tts.android.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tts.android.utils.Assert;
import com.tts.android.utils.DateUtils;
import com.tts.android.utils.StringUtils;

/**
 * 
 * @author Vina.Chiong
 *
 */
public class DbTemplate implements DbOperations {

	private SQLiteDatabase sqliteDatabase;
	
	public DbTemplate(SQLiteDatabase sqliteDatabase){
		Assert.notNull(sqliteDatabase);
		this.sqliteDatabase = sqliteDatabase;
	}
	
	public SQLiteDatabase getSqliteDatabase() {
		return sqliteDatabase;
	}

	public void setSqliteDatabase(SQLiteDatabase sqliteDatabase) {
		this.sqliteDatabase = sqliteDatabase;
	}
	
	@Override
	public Cursor query(String sql) throws DataAccessException {
		try{
			return sqliteDatabase.rawQuery(sql, null);
		}catch(SQLiteException e){
			e.printStackTrace();
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	@Override
	public Cursor query(String sql, String... args) throws DataAccessException {
		try{
			return sqliteDatabase.rawQuery(sql, args);
		}catch(SQLiteException e){
			e.printStackTrace();
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	private <T> List<T> convertCursorToBean(Cursor cursor, RowMapper<T> rowMapper, String sql){
		List<T> list = new ArrayList<T>(cursor.getCount());
		
		try {
			if (cursor.moveToFirst()) {
				do {
					list.add(rowMapper.mapRow(cursor, cursor.getPosition()));

				} while (cursor.moveToNext());
			}
		} catch (SQLiteException e) {
			Log.e(getClass().getSimpleName(), "query database error: " + sql);
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			cursor.close();
		}

		return list;
	}
	
	@Override
	public <T> List<T> query(String sql, RowMapper<T> rowMapper)
			throws DataAccessException {
		Cursor cursor = query(sql);
		return convertCursorToBean(cursor, rowMapper, sql);
	}

	@Override
	public <T> List<T> query(String sql, RowMapper<T> rowMapper, String... args)
			throws DataAccessException {
		Cursor cursor = query(sql, args);
		return convertCursorToBean(cursor, rowMapper, sql);
	}

	@Override
	public <T> List<T> query(String sql, String[] args, RowMapper<T> rowMapper)
			throws DataAccessException {
		Cursor cursor = query(sql, args);
		return convertCursorToBean(cursor, rowMapper, sql);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType)
			throws DataAccessException {
		Cursor cursor = query(sql);
		return convertCursorToBean(cursor, new SingleColumnRowMapper<T>(elementType), sql);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType,
			String... args) throws DataAccessException {
		Cursor cursor = query(sql, args);
		return convertCursorToBean(cursor, new SingleColumnRowMapper<T>(elementType), sql);
	}

	@Override
	public <T> List<T> queryForList(String sql, String[] args,
			Class<T> elementType) throws DataAccessException {
		Cursor cursor = this.query(sql, args);
		return convertCursorToBean(cursor, new SingleColumnRowMapper<T>(elementType), sql);
	}

	@Override
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper)
			throws DataAccessException {
		List<T> results = query(sql, rowMapper);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper,
			String... args) throws DataAccessException {
		List<T> results = query(sql, rowMapper, args);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public <T> T queryForObject(String sql, String[] args,
			RowMapper<T> rowMapper) throws DataAccessException {
		List<T> results = query(sql, args, rowMapper);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType)
			throws DataAccessException {
		List<T> results = query(sql, new SingleColumnRowMapper<T>(requiredType));
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType,
			String... args) throws DataAccessException {
		List<T> results = query(sql, new SingleColumnRowMapper<T>(requiredType), args);
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public <T> T queryForObject(String sql, String[] args, Class<T> requiredType)
			throws DataAccessException {
		List<T> results = query(sql, args, new SingleColumnRowMapper<T>(requiredType));
		return DataAccessUtils.singleResult(results);
	}

	@Override
	public Map<String, Object> queryForMap(String sql)
			throws DataAccessException {
		return queryForObject(sql, new ColumnMapRowMapper());
	}

	@Override
	public Map<String, Object> queryForMap(String sql, String... args)
			throws DataAccessException {
		return queryForObject(sql, new ColumnMapRowMapper(), args);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql)
			throws DataAccessException {
		Cursor cursor = this.query(sql);
		return convertCursorToBean(cursor, new ColumnMapRowMapper(), sql);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, String... args)
			throws DataAccessException {
		Cursor cursor = this.query(sql, args);
		return convertCursorToBean(cursor, new ColumnMapRowMapper(), sql);
	}

	/**
	 * Recommend to use insert(String table, Map<String, Object> values)<br/>
	 * @param the table to insert the row into<br/>
	 * @param values {@link ContentValues} <code>key</code> must be underScore corresponding to column name in DB.<br/>
	 * @return the row ID of the newly inserted row, or -1 if an error occurred <br/>
	 * @throws DataAccessException
	 */
	@Override
	public long insert(String table, ContentValues values)
			throws DataAccessException {
		return sqliteDatabase.insert(table, null, values);
	}

	/**
	 * 
	 * @param table the table to insert the row into<br/>
	 * @param values <code>key</code> can be camelName corresponding to property name of VO.<br/>
	 * @return the row ID of the newly inserted row, or -1 if an error occurred <br/>
	 * @throws DataAccessException
	 */
	@Override
	public long insert(String table, Map<String, Object> values)
			throws DataAccessException {
		ContentValues cv = new ContentValues();
		for (String key : values.keySet()) { 
			String underScoreKey = StringUtils.underscoreName(key);
			Log.d("", "'" + key + "' change to underscore : ' " + underScoreKey + ", value :" + values.get(key));
			cv.put(underScoreKey, (String)values.get(key));
		}
		
		return this.insert(table, cv);
	}

	/**
	 * Recommend to use update(String table, Map<String, Object> values, String whereClause, String... whereArgs)<br/>
	 * @param table table name of DB.<br/>
	 * @param values {@link ContentValues} <code>key</code> must be underScore corresponding to column name in DB.<br/>
	 * @param whereClause the optional WHERE clause to apply when updating. Passing null will update all rows. Example: "id = ? and deleted = ?". 
	 * @param whereArgs values will match ? in whereClause in order. Example: new String[]{"id", "1"};
	 * @return the number of rows affected<br/> 
	 * @throws DataAccessException
	 */
	@Override
	public int update(String table, ContentValues values, String whereClause,
			String... whereArgs) throws DataAccessException {
		
		return sqliteDatabase.update(table, values, whereClause, whereArgs);
	}

	/**
	 * 
	 * @param table table name of DB.<br/>
	 * @param values <code>key</code> can be camelName corresponding to property name of VO.<br/>
	 * @param whereClause the optional WHERE clause to apply when updating. Passing null will update all rows. Example: "id = ? and deleted = ?". 
	 * @param whereArgs values will match ? in whereClause in order. Example: new String[]{"id", "1"};
	 * @return the number of rows affected<br/> 
	 * @throws DataAccessException
	 */
	@Override
	public int update(String table, Map<String, Object> values,
			String whereClause, String... whereArgs) throws DataAccessException {
		ContentValues cvs = new ContentValues();
		for (String key : values.keySet()) { 
			String underScoreKey = StringUtils.underscoreName(key);
			Log.d("", "'" + key + "' change to underscore : ' " + underScoreKey + ", value :" + values.get(key));
			Object obj = values.get(key);
			if(obj instanceof String){
				cvs.put(underScoreKey, (String)obj);
			}else if (obj instanceof Integer){
				cvs.put(underScoreKey, (Integer)obj);
			}else if(obj instanceof Boolean){
				cvs.put(underScoreKey, (Boolean)obj);
			}else if(obj instanceof Long){
				cvs.put(underScoreKey, (Long)obj);
			}else if(obj instanceof Short){
				cvs.put(underScoreKey, (Short)obj);
			}else if(obj instanceof Float){
				cvs.put(underScoreKey, (Float)obj);
			}else if(obj instanceof Double){
				cvs.put(underScoreKey, (Double)obj);
			}else if (obj instanceof Byte){
				cvs.put(underScoreKey, (Byte)obj);
			}else if(obj instanceof byte[]){
				cvs.put(underScoreKey, (byte[])obj);
			}else if(obj instanceof Date){
				cvs.put(underScoreKey, DateUtils.isoFormatter((Date)obj));
			}
		}
		return update(table, cvs, whereClause, whereArgs);
	}

	/**
	 * Convenience method for deleting rows in the database.
	 * @param table the table to delete from <br/>
	 * @param whereClause the optional WHERE clause to apply when deleting. Passing null will delete all rows.<br/>
	 * @param whereArgs
	 * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
	 * @throws DataAccessException
	 */
	@Override
	public int delete(String table, String whereClause, String... whereArgs)
			throws DataAccessException {
		return sqliteDatabase.delete(table, whereClause, whereArgs);
	}
}
