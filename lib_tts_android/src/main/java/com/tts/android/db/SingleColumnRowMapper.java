package com.tts.android.db;

import android.database.Cursor;
import android.database.SQLException;

import com.tts.android.utils.NumberUtils;

public class SingleColumnRowMapper<T> implements RowMapper<T> {
	private Class<T> requiredType;
	
	@Override
	@SuppressWarnings("unchecked")
	public T mapRow(Cursor cursor, int rowNum) throws SQLException {
		int nrOfColumns = cursor.getColumnCount();
		if (nrOfColumns != 1) {
			throw new IncorrectResultSetColumnCountException(1, nrOfColumns);
		}

		// Extract column value from Cursor
		Object result = DbUtils.getCursorValue(cursor, 0, requiredType);
		
		if (result != null && this.requiredType != null && !this.requiredType.isInstance(result)) {
			// Extracted value does not match already: try to convert it.
			try {
				return (T) convertValueToRequiredType(result, this.requiredType);
			}
			catch (IllegalArgumentException ex) {
				throw new TypeMismatchDataAccessException(
						"Type mismatch affecting row number " + rowNum + " and column type '" +
						cursor.getColumnName(0) + "': " + ex.getMessage());
			}
		}
		return (T) result;
	}

	/**
	 * Create a new SingleColumnRowMapper.
	 * @param requiredType the type that each result object is expected to match
	 */
	public SingleColumnRowMapper(Class<T> requiredType) {
		this.requiredType = requiredType;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object convertValueToRequiredType(Object value, Class requiredType) {
		if (String.class.equals(requiredType)) {
			return value.toString();
		}
		else if (Number.class.isAssignableFrom(requiredType)) {
			if (value instanceof Number) {
				// Convert original Number to target Number class.
				return NumberUtils.convertNumberToTargetClass(((Number) value), requiredType);
			}
			else {
				// Convert stringified value to target Number class.
				return NumberUtils.parseNumber(value.toString(), requiredType);
			}
		}
		else {
			throw new IllegalArgumentException(
					"Value [" + value + "] is of type [" + value.getClass().getName() +
					"] and cannot be converted to required type [" + requiredType.getName() + "]");
		}
	}
}
