package com.tts.android.utils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.database.Cursor;
public abstract class CursorUtils {
	private static Map<String, String> columnPropertyMap = new HashMap<String, String>();
	
	private static String getPropertyName(String column){
		if (columnPropertyMap.containsKey(column)) {
            return columnPropertyMap.get(column);
        }
		
		String propertyName = StringUtils.camelCaseName(column);
		columnPropertyMap.put(column, propertyName);
		
		return propertyName;
	}
	
	public static List<Map<String, Object>> cursor2List(Cursor c, boolean closeCursor){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(c.getCount());
		
		String[] columnNames = c.getColumnNames();
		
		Map<String, Object> data;

		if (c.moveToFirst()) {
			do {
				data = new HashMap<String, Object>();
				
				for (String column : columnNames){
					data.put(getPropertyName(column), c.getString(c.getColumnIndex(column)));
				}
				list.add(data);
			} while (c.moveToNext());
		}
		
		if (closeCursor) {
            c.close();
        }
		
		return list;
	}
	
	public static Map<String, Object> cursor2Map(Cursor c){
		boolean result = c.moveToFirst();
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		if (!result) {
            return data;
        }
		
		String[] columnNames = c.getColumnNames();
		 
		for (String column : columnNames){
			int columIndex = c.getColumnIndex(column);
			String value = c.getString(columIndex);
			data.put(getPropertyName(column), value);
		}
		c.close();
		return data;
	}
	
	public static <T> List<T> cursor2ObjectList(Cursor c,
			Class<T> clazz, boolean closeCursor)
			throws ClassNotFoundException, SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		
		List<T> list = new ArrayList<T>(c.getCount());
		
		String[] columnNames = c.getColumnNames();
		
		T data = null;
		
		Class<T> classObject = clazz;
		
		if (c.moveToFirst()) {
			do {
				data = classObject.newInstance();
				
				for (String column : columnNames){
					String propertyName = getPropertyName(column);
					Field field = classObject.getDeclaredField(propertyName);
					field.setAccessible(true);
					String typeName = field.getType().getSimpleName();
					
					if("String".equals(typeName)){
						field.set(data, c.getString(c.getColumnIndex(column)));
					}else if("int".equals(typeName)){
						field.set(data, c.getInt(c.getColumnIndex(column)));
					}else if("Date".equals(typeName)){
						DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
						String dateTime = c.getString(c.getColumnIndex(column));
						if(null == dateTime || "".equals(dateTime.trim())){
							field.set(data, new Date());
							continue;
						}
						try {
							field.set(data, simpleDateFormat.parse(dateTime));
						} catch (ParseException e) {
							field.set(data, new Date());
							e.printStackTrace();
						}
					}else if("boolean".equals(typeName)){
						field.set(data, c.getInt(c.getColumnIndex(column)) == 0 ?  false : true);
					}
				}
				list.add(data);
			} while (c.moveToNext());
		}
		
		c.close();
		return list;
	}
}
