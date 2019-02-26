package com.tts.android.db;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

public interface DbOperations {
	
	/**
	 * <p>Runs the provided SQL and returns a Cursor over the result set.</p>
	 * @param sql the SQL query. The SQL string must not be ; terminated
	 * @return
	 * @throws DataAccessException
	 */
	Cursor query(String sql) throws DataAccessException;

	/**
	 * 
	 * Runs the provided SQL and returns a Cursor over the result set.
	 * @param sql the SQL query. The SQL string must not be ; terminated 
	 * @param args You may include ?s in where clause in the query, which will be replaced by the values from selectionArgs. The values will be bound as Strings. 
	 * @returnA Cursor object, which is positioned before the first entry. Note that Cursors are not synchronized, see the documentation for more details. 
	 * @throws DataAccessException
	 */
	Cursor query(String sql, String... args) throws DataAccessException;
	
	/**
	 * 
	 * @param sql
	 * @param rowMapper implement {@link RowMapper<T>} to convert each column selected to <T>'s property. <br/>  
	 * @return
	 * @throws DataAccessException
	 */
	<T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException;

	/**
	 * 
	 * @param sql
	 * @param rowMapper implement {@link RowMapper<T>} to convert each column selected to <T>'s property. <br/>  
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	<T> List<T> query(String sql, RowMapper<T> rowMapper, String... args) throws DataAccessException;
	
	/**
	 * 
	 * @param sql
	 * @param args
	 * @param rowMapper implement {@link RowMapper<T>} to convert each column selected to <T>'s property. <br/>  
	 * @return
	 * @throws DataAccessException
	 */
	<T> List<T> query(String sql, String[] args, RowMapper<T> rowMapper) throws DataAccessException;
	
	/**
	 * 
	 * @param sql query data for single colunm, like count(*), sum(*) and so on.
	 * @param elementType basic data type, such as <code>String.class<code/>,<code>Integer.class<code/>,<code>Float.class<code/> and so on.<br/>
	 * @return
	 * @throws DataAccessException
	 */
	<T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException;
	
	/**
	 * 
	 * @param sql SELECT sql for single colunm, like count(*), sum(*) and so on.
	 * @param elementType basic data type, such as <code>String.class<code/>,<code>Integer.class<code/>,<code>Float.class<code/> and so on.<br/>
	 * @return
	 * @throws DataAccessException
	 */
	<T> List<T> queryForList(String sql, Class<T> elementType, String... args) throws DataAccessException;

	/**
	 * 
	 * @param sql
	 * @param args
	 * @param elementType basic data type, such as <code>String.class<code/>,<code>Integer.class<code/>,<code>Float.class<code/> and so on.<br/>
	 * @return
	 * @throws DataAccessException
	 */
	<T> List<T> queryForList(String sql, String[] args, Class<T> elementType) throws DataAccessException;
	
	/**
	 * 
	 * @param sql
	 * @return
	 * @throws DataAccessException
	 */
	List<Map<String, Object>> queryForList(String sql) throws DataAccessException;
	
	/**
	 * 
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	List<Map<String, Object>> queryForList(String sql, String... args) throws DataAccessException;
	
	/**
	 * 
	 * @param sql
	 * @param rowMapper implement {@link RowMapper<T>} to convert each column selected to <T>'s property. <br/>  
	 * @return
	 * @throws DataAccessException
	 */
	<T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException;
	
	/**
	 * 
	 * @param sql
	 * @param rowMapper implement {@link RowMapper<T>} to convert each column selected to <T>'s property. <br/>  
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	<T> T queryForObject(String sql, RowMapper<T> rowMapper, String... args)throws DataAccessException;
	
	/**
	 * 
	 * @param sql
	 * @param args
	 * @param rowMapper implement {@link RowMapper<T>} to convert each column selected to <T>'s property. <br/>  
	 * @return
	 * @throws DataAccessException
	 */
	<T> T queryForObject(String sql, String[] args, RowMapper<T> rowMapper) throws DataAccessException;
	
	/**
	 * 
	 * @param sql
	 * @param requiredType basic data type, such as <code>String.class<code/>,<code>Integer.class<code/>,<code>Float.class<code/> and so on.<br/>
	 * @return
	 * @throws DataAccessException
	 */
	<T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException;
	
	/**
	 * 
	 * @param sql
	 * @param requiredType basic data type, such as <code>String.class<code/>,<code>Integer.class<code/>,<code>Float.class<code/> and so on.<br/>
	 * @param args
	 * @return
	 * @throws DataAccessException
	 */
	<T> T queryForObject(String sql, Class<T> requiredType, String... args) throws DataAccessException;
	
	/**
	 * 
	 * @param sql
	 * @param args
	 * @param requiredType basic data type, such as <code>String.class<code/>,<code>Integer.class<code/>,<code>Float.class<code/> and so on.<br/>
	 * @return
	 * @throws DataAccessException
	 */
	<T> T queryForObject(String sql, String[] args, Class<T> requiredType) throws DataAccessException;
	
	//
	Map<String, Object> queryForMap(String sql) throws DataAccessException;
	
	Map<String, Object> queryForMap(String sql, String... args) throws DataAccessException;
	
	/**
	 * Recommend to use insert(String table, Map<String, Object> values)<br/>
	 * @param the table to insert the row into<br/>
	 * @param values {@link ContentValues} <code>key</code> must be underScore corresponding to column name in DB.<br/>
	 * @return the row ID of the newly inserted row, or -1 if an error occurred <br/>
	 * @throws DataAccessException
	 */
	long insert(String table, ContentValues values) throws DataAccessException;
	
	/**
	 * 
	 * @param table the table to insert the row into<br/>
	 * @param values <code>key</code> can be camelName corresponding to property name of VO.<br/>
	 * @return the row ID of the newly inserted row, or -1 if an error occurred <br/>
	 * @throws DataAccessException
	 */
	long insert(String table, Map<String, Object> values) throws DataAccessException;
	
	/**
	 * Recommend to use update(String table, Map<String, Object> values, String whereClause, String... whereArgs)<br/>
	 * @param table table name of DB.<br/>
	 * @param values {@link ContentValues} <code>key</code> must be underScore corresponding to column name in DB.<br/>
	 * @param whereClause the optional WHERE clause to apply when updating. Passing null will update all rows. Example: "id = ? and deleted = ?". 
	 * @param whereArgs values will match ? in whereClause in order. Example: new String[]{"id", "1"};
	 * @return the number of rows affected<br/> 
	 * @throws DataAccessException
	 */
	int update(String table, ContentValues values, String whereClause, String... whereArgs) throws DataAccessException;
	
	/**
	 * 
	 * @param table table name of DB.<br/>
	 * @param values <code>key</code> can be camelName corresponding to property name of VO.<br/>
	 * @param whereClause the optional WHERE clause to apply when updating. Passing null will update all rows. Example: "id = ? and deleted = ?". 
	 * @param whereArgs values will match ? in whereClause in order. Example: new String[]{"id", "1"};
	 * @return the number of rows affected<br/> 
	 * @throws DataAccessException
	 */
	int update(String table, Map<String, Object> values, String whereClause, String... whereArgs) throws DataAccessException;
	
	/**
	 * Convenience method for deleting rows in the database.
	 * @param table the table to delete from <br/>
	 * @param whereClause the optional WHERE clause to apply when deleting. Passing null will delete all rows.<br/>
	 * @param whereArgs
	 * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
	 * @throws DataAccessException
	 */
	int delete(String table, String whereClause, String... whereArgs) throws DataAccessException;
}
