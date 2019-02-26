package com.tts.android.db;

import java.util.Collection;

import com.tts.android.utils.CollectionUtils;
import com.tts.android.utils.NumberUtils;



/**
 * Miscellaneous utility methods for DAO implementations.
 * Useful with any data access technology.

 */
public abstract class DataAccessUtils {

	/**
	 * Return a single result object from the given Collection.
	 * <p>Returns {@code null} if 0 result objects found;
	 * throws an exception if more than 1 element found.
	 * @param results the result Collection (can be {@code null})
	 * @return the single result object, or {@code null} if none
	 * @throws IncorrectResultSizeDataAccessException if more than one
	 * element has been found in the given Collection
	 */
	public static <T> T singleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
		int size = (results != null ? results.size() : 0);
		if (size == 0) {
			return null;
		}
		if (results.size() > 1) {
			throw new IncorrectResultSizeDataAccessException(1, size);
		}
		return results.iterator().next();
	}

	/**
	 * Return a single result object from the given Collection.
	 * <p>Throws an exception if 0 or more than 1 element found.
	 * @param results the result Collection (can be {@code null})
	 * @return the single result object
	 * @throws IncorrectResultSizeDataAccessException if more than one
	 * element has been found in the given Collection
	 * @throws EmptyResultDataAccessException if no element at all
	 * has been found in the given Collection
	 */
	public static <T> T requiredSingleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
		int size = (results != null ? results.size() : 0);
		if (size == 0) {
			throw new EmptyResultDataAccessException(1);
		}
		if (results.size() > 1) {
			throw new IncorrectResultSizeDataAccessException(1, size);
		}
		return results.iterator().next();
	}

	/**
	 * Return a unique result object from the given Collection.
	 * <p>Returns {@code null} if 0 result objects found;
	 * throws an exception if more than 1 instance found.
	 * @param results the result Collection (can be {@code null})
	 * @return the unique result object, or {@code null} if none
	 * @throws IncorrectResultSizeDataAccessException if more than one
	 * result object has been found in the given Collection
	 * @see com.tts.common.util.CollectionUtils#hasUniqueObject
	 */
	public static <T> T uniqueResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
		int size = (results != null ? results.size() : 0);
		if (size == 0) {
			return null;
		}
		if (!CollectionUtils.hasUniqueObject(results)) {
			throw new IncorrectResultSizeDataAccessException(1, size);
		}
		return results.iterator().next();
	}

	/**
	 * Return a unique result object from the given Collection.
	 * <p>Throws an exception if 0 or more than 1 instance found.
	 * @param results the result Collection (can be {@code null})
	 * @return the unique result object
	 * @throws IncorrectResultSizeDataAccessException if more than one
	 * result object has been found in the given Collection
	 * @throws EmptyResultDataAccessException if no result object at all
	 * has been found in the given Collection
	 * @see com.tts.common.util.CollectionUtils#hasUniqueObject
	 */
	public static <T> T requiredUniqueResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
		int size = (results != null ? results.size() : 0);
		if (size == 0) {
			throw new EmptyResultDataAccessException(1);
		}
		if (!CollectionUtils.hasUniqueObject(results)) {
			throw new IncorrectResultSizeDataAccessException(1, size);
		}
		return results.iterator().next();
	}

	/**
	 * Return a unique result object from the given Collection.
	 * Throws an exception if 0 or more than 1 result objects found,
	 * of if the unique result object is not convertable to the
	 * specified required type.
	 * @param results the result Collection (can be {@code null})
	 * @return the unique result object
	 * @throws IncorrectResultSizeDataAccessException if more than one
	 * result object has been found in the given Collection
	 * @throws EmptyResultDataAccessException if no result object
	 * at all has been found in the given Collection
	 * @throws TypeMismatchDataAccessException if the unique object does
	 * not match the specified required type
	 */
	@SuppressWarnings("unchecked")
	public static <T> T objectResult(Collection<?> results, Class<T> requiredType)
			throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException {

		Object result = requiredUniqueResult(results);
		if (requiredType != null && !requiredType.isInstance(result)) {
			if (String.class.equals(requiredType)) {
				result = result.toString();
			}
			else if (Number.class.isAssignableFrom(requiredType) && Number.class.isInstance(result)) {
				try {
					result = NumberUtils.convertNumberToTargetClass(((Number) result), (Class<? extends Number>) requiredType);
				}
				catch (IllegalArgumentException ex) {
					throw new TypeMismatchDataAccessException(ex.getMessage());
				}
			}
			else {
				throw new TypeMismatchDataAccessException(
						"Result object is of type [" + result.getClass().getName() +
						"] and could not be converted to required type [" + requiredType.getName() + "]");
			}
		}
		return (T) result;
	}

	/**
	 * Return a unique int result from the given Collection.
	 * Throws an exception if 0 or more than 1 result objects found,
	 * of if the unique result object is not convertable to an int.
	 * @param results the result Collection (can be {@code null})
	 * @return the unique int result
	 * @throws IncorrectResultSizeDataAccessException if more than one
	 * result object has been found in the given Collection
	 * @throws EmptyResultDataAccessException if no result object
	 * at all has been found in the given Collection
	 * @throws TypeMismatchDataAccessException if the unique object
	 * in the collection is not convertable to an int
	 */
	@SuppressWarnings("rawtypes")
	public static int intResult(Collection results)
			throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException {

		return objectResult(results, Number.class).intValue();
	}

	/**
	 * Return a unique long result from the given Collection.
	 * Throws an exception if 0 or more than 1 result objects found,
	 * of if the unique result object is not convertable to a long.
	 * @param results the result Collection (can be {@code null})
	 * @return the unique long result
	 * @throws IncorrectResultSizeDataAccessException if more than one
	 * result object has been found in the given Collection
	 * @throws EmptyResultDataAccessException if no result object
	 * at all has been found in the given Collection
	 * @throws TypeMismatchDataAccessException if the unique object
	 * in the collection is not convertable to a long
	 */
	@SuppressWarnings("rawtypes")
	public static long longResult(Collection results)
			throws IncorrectResultSizeDataAccessException, TypeMismatchDataAccessException {

		return objectResult(results, Number.class).longValue();
	}
}
