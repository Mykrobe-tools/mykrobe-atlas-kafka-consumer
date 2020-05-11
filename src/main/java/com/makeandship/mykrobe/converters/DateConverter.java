package com.makeandship.mykrobe.converters;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.makeandship.mykrobe.Constants;

public class DateConverter {

	public static String dateToMysql(Date date) {
		if (date != null) {
			return new SimpleDateFormat(Constants.MYSQL_DATE_FORMAT).format(date);
		}
		return null;
	}

}
