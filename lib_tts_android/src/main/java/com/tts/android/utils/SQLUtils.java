package com.tts.android.utils;

public class SQLUtils {

//	public static <T> SqlParams buildSqlFromBean(String preparestatement, T bean) throws ClassNotFoundException,
//			IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
//
//		Class<?> classObject = Class.forName(bean.getClass().getName());
//
//		List<Object> params = new ArrayList<Object>();
//
////		Field[] fields = classObject.getDeclaredFields();
//
////		int insertIndex = 0;
//		
//		String[] sqlFields = preparestatement.split(":");
//		for(int i = 1 ; i < sqlFields.length ; i++){
//			String fieldName = sqlFields[i].split(",")[0].trim();
//			fieldName = fieldName.replace(")", "").trim();
//			preparestatement = preparestatement.replace(":" + fieldName, "?");
//			
//			Object fieldValue = classObject.getDeclaredField(fieldName).get(bean);
//			if (fieldValue instanceof Date){
//				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				fieldValue = format.format(fieldValue);
//			}
//			params.add(fieldValue);
//		}
//		
////		for (int i = 0; i < fields.length; i++) {
////			Field field = fields[i];
////			String fieldName = field.getName();
////			int index = preparestatement.indexOf(fieldName);
////
////			if (-1 != index) {
////				preparestatement = preparestatement.replace(":" + field.getName(), "?");
////				params.add(field.get(bean));
////			}
////
////		}
//
//		Object[] paramsArray =  params.toArray();
//		
//		return new SqlParams(preparestatement, paramsArray);
//	}

}
