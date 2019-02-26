package com.tts.android.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtils {
	public final static String DATE_FORMATTER = "MM-dd-yyyy";
	public final static String SQLITE_DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
	
	
	public static String formateDate(String format, Date date){
		DateFormat df = new SimpleDateFormat(format);
		if (date == null) {
            return "";
        } else {
            return df.format(date);
        }
	}
	
	public static Date parseDate(String format, String date){
		DateFormat df = new SimpleDateFormat(format);
		if (date == null) {
            return null;
        }
		try {
			return df.parse(date);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	public static Date parseToDate(Date date){	
		date = parseDate(DATE_FORMATTER,formateDate(DATE_FORMATTER,date));
		return date;
	}
	
	/**
	 * format {@link Date} to "yyyy-MM-dd HH:mm:ss" {@link String}.
	 * @param date
	 * @return
	 */
	public static String isoFormatter(Date date){
		return formateDate(SQLITE_DATE_FORMATTER, date);
	}
	
	/**
	 * parse {@link String} "yyyy-MM-dd HH:mm:ss" to {@link Date}.
	 * @param sqliteDateStr
	 * @return
	 */
	public static Date isoParser(String sqliteDateStr){
		return parseDate(SQLITE_DATE_FORMATTER, sqliteDateStr);
	}

}
