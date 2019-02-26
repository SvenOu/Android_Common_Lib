package com.tts.android.mybatic;

/**
 * Created by sven-ou on 2017/12/13.
 */

public class XmlMappingConfig {
    public static final String MAPPER = "mapper";
    public static final String NAMESPACE = "namespace";
    public static final String TAG_INSERT = "insert";
    public static final String TAG_UPDATE = "update";
    public static final String TAG_SELECT = "select";
    public static final String TAG_DELETE = "delete";
    public static final String ATTR_ID = "id";
    public static final String ATTR_PARAMETER_TYPE = "parameter_type";
    public static final String ATTR_RESULT_TYPE = "result_type";
    public static final String TABLE_FOR_CHANGE_ALL = "table_for_change_all";

    // #{ string }
    // FIXME: 以下常量只允许一个字符
    public static final String NUMBER_SIGN = "#";
    public static final String LEFT_SIGN = "{";
    public static final String RIGHT_SIGN = "}";
    public static final String PARAM_SIGN = "?";
}
