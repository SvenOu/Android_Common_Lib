package com.tts.android.mybatic;

/**
 * Created by sven-ou on 2017/12/13.
 */

public class MethodEntity {
    private String id;
    private String tag;
    private String text;
    private String parameterType;
    private String resultType;
    private String tableForChangeAll;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTableForChangeAll() {
        return tableForChangeAll;
    }

    public void setTableForChangeAll(String tableForChangeAll) {
        this.tableForChangeAll = tableForChangeAll;
    }
}
