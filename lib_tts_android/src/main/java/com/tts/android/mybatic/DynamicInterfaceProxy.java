package com.tts.android.mybatic;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;

import com.tts.android.db.DatabaseManager;
import com.tts.android.db.DbTemplate;
import com.tts.android.mybatic.bean.BeanPropertyContentValues;
import com.tts.android.mybatic.bean.BeanPropertyRowMapper;
import com.tts.android.utils.DateUtils;
import com.tts.android.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DynamicInterfaceProxy implements InvocationHandler {
    private static final String TAG = DynamicInterfaceProxy.class.getSimpleName();
    private Map mapper;
    private DbTemplate writeDBTemplate;
    private DbTemplate readDBTemplate;

    public DynamicInterfaceProxy(Map xmlDocuments) {
        this.mapper = xmlDocuments;
        this.writeDBTemplate = new DbTemplate(DatabaseManager.getInstance().wdb());
        this.readDBTemplate = new DbTemplate(DatabaseManager.getInstance().rdb());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodKey = generateMethodKey(method);
        if (mapper.containsKey(methodKey)) {
            Class<?> returnClass = method.getReturnType();
            MethodEntity doc = (MethodEntity) mapper.get(methodKey);
            String text = doc.getText();
            String tableForChangeAll = doc.getTableForChangeAll();
            String sqlText = formatSql(text, args);
            if (XmlMappingConfig.TAG_INSERT.equals(doc.getTag())) {
                return executeInsert(sqlText, tableForChangeAll, args);
            } else if (XmlMappingConfig.TAG_UPDATE.equals(doc.getTag())) {
                return executeUpdate(sqlText, tableForChangeAll, args);
            }else if ( XmlMappingConfig.TAG_DELETE.equals(doc.getTag())) {
                return executeDelete(sqlText);
            } else if (XmlMappingConfig.TAG_SELECT.equals(doc.getTag())) {
                if (returnClass.getName().equals(List.class.getName())) {
                    Type returnType = method.getGenericReturnType();
                    if (returnType instanceof ParameterizedType) {
                        ParameterizedType paramType = (ParameterizedType) returnType;
                        Type[] argTypes = paramType.getActualTypeArguments();
                        if (argTypes.length > 0) {
                            returnClass = Class.forName(((Class) argTypes[0]).getName());
                            return executeQuery(sqlText, returnClass);
                        }
                    }
                }
                return executeQueryForObject(sqlText, returnClass);
            }
        } else {
            try {
                return method.invoke(this, args);
            }catch (Exception e){
                throw new RuntimeException("no interface for method \"" + methodKey + "\" in xml.");
            }
        }
        return null;
    }

    private Object executeDelete(String sqlText) {
        return writeDBTemplate.getSqliteDatabase()
                .compileStatement(sqlText).executeUpdateDelete();
    }

    private Object executeUpdate(String sqlText, String tableForChangeAll, Object[] args) {
        if(!TextUtils.isEmpty(tableForChangeAll)){
            if(args[0] instanceof ContentValues){
                return writeDBTemplate.update(tableForChangeAll, (ContentValues)args[0], sqlText);
            }
            return writeDBTemplate.update(tableForChangeAll,
                    BeanPropertyContentValues.newInstance(args[0]), sqlText);
        }
        return writeDBTemplate.getSqliteDatabase()
                .compileStatement(sqlText).executeUpdateDelete();
    }

    private Object executeQueryForObject(String sqlText, Class<?> returnClass) {
        return readDBTemplate.queryForObject(sqlText, BeanPropertyRowMapper.newInstance(returnClass));
    }

    private List<?> executeQuery(String sqlText, Class<?> returnClass) {
        return readDBTemplate.query(sqlText, BeanPropertyRowMapper.newInstance(returnClass));
    }

    private int executeInsert(String sqlText, String tableForChangeAll, Object[] args) {
        if(!TextUtils.isEmpty(tableForChangeAll)){
            return (int) writeDBTemplate.insert(tableForChangeAll,
                    BeanPropertyContentValues.newInstance(args[0]));
        }
        return ((Long) writeDBTemplate.getSqliteDatabase().
                compileStatement(sqlText).executeInsert()).intValue();
    }

    private String formatSql(String text, Object[] args) {
        if (TextUtils.isEmpty(text)||
                !text.contains(XmlMappingConfig.NUMBER_SIGN + XmlMappingConfig.LEFT_SIGN) && !text.contains(XmlMappingConfig.PARAM_SIGN)) {
            return text;
        }
        if (null == args || args.length <= 0) {
            throw new RuntimeException("args is empty!");
        }
        String sqlText = null;

        sqlText = formatSql(text, args[0]);

        if (sqlText.contains(XmlMappingConfig.PARAM_SIGN)) {
            for (int i = 0; i < args.length; i++) {
                sqlText = sqlText.replaceFirst("\\" + XmlMappingConfig.PARAM_SIGN, '\'' + String.valueOf(args[i].toString()) + '\'');
            }
        }
        return sqlText;
    }

    private String formatSql(String text, Object obj) {
        StringBuilder stringBuilder = new StringBuilder();
        int front = -1;
        String temp = "";
        if (!text.contains(XmlMappingConfig.NUMBER_SIGN + XmlMappingConfig.LEFT_SIGN)) {
            return text.replaceAll("\\r|\\n", "");
        }
        for (int i = 0; i < text.length(); i++) {
            if (XmlMappingConfig.NUMBER_SIGN.charAt(0) == text.charAt(i)
                    && XmlMappingConfig.LEFT_SIGN.charAt(0) == text.charAt(i + 1)) {
                front = i;
            }
            if (i < front || front == -1) {
                stringBuilder.append(text.charAt(i));
            }
            if (i > (front + 1) && front != -1
                    && XmlMappingConfig.RIGHT_SIGN.charAt(0) != text.charAt(i)) {
                temp += text.charAt(i);
            }

            if (XmlMappingConfig.RIGHT_SIGN.charAt(0) == text.charAt(i) && front != -1) {
                String value = getFieldValueFormObject(obj, temp);
                if (TextUtils.isEmpty(value)) {
                    value = "";
                }
                stringBuilder.append("'" + value + "'");
                temp = "";
                front = -1;
            }
        }
        return stringBuilder.toString().replaceAll("\\r|\\n", "");
    }

    private String getFieldValueFormObject(Object obj, String temp) {
        String value = "";
        Object rawValue = "";
        Field f = null;
        try {
            if(obj instanceof ContentValues){
                Method getMethod = obj.getClass().getMethod("get", String.class);
                rawValue = getMethod.invoke(obj, StringUtils.underscoreName(temp));
            }else{
                f = obj.getClass().getDeclaredField(StringUtils.camelCaseName(temp));
                f.setAccessible(true);//Very important, this allows the setting to work.
                rawValue = f.get(obj);
            }
            if(null == rawValue){
                return value;
            }
            if(rawValue instanceof Date){
                value = DateUtils.isoFormatter((Date)rawValue);
            }else if(rawValue instanceof Boolean ||
                    boolean.class.getName().equals(rawValue.getClass().getName())){
                value = Boolean.valueOf(String.valueOf(rawValue)) ? "1": "0";
            }
            else {
                value = String.valueOf(rawValue);
            }
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "NoSuchFieldException, cannot get value for '"+temp+"' "+e.getMessage());
        } catch (IllegalAccessException e) {
            Log.w(TAG, "IllegalAccessException, cannot get value for '"+temp+"' "+e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.w(TAG, "NoSuchMethodException, cannot get value for '"+temp+"' "+e.getMessage());
        } catch (InvocationTargetException e) {
            Log.w(TAG, "InvocationTargetException, cannot get value for '"+temp+"' "+e.getMessage());
        }
        return value;
    }

    private String generateMethodKey(Method method) {
        String key = method.getName();
        Class<?>[] paramTypes = method.getParameterTypes();
        for (Class c : paramTypes) {
            key += c.getSimpleName();
        }
        return key;
    }
}