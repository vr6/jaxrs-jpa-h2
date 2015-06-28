/*******************************************************************************
 * Copyright (c) 2015 Venkat Reddy
 * All rights reserved.
 *******************************************************************************/
package org.test.login.server.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * A utility class for formatting and validating date values
 * @author vreddy
 */
public class DateUtil {

	/**
	 * Adds a date parameter (start, end) as filter to the SQL where clause
	 */
	public static final void addDateParam(String dtstr, boolean start,
			List<String> where) {
		if (dtstr != null && isValidDate(dtstr)) {
			where.add("c.loginTime " + (start ? ">= '" : "<= '")
					+ dtstr.substring(0, 4) + "-" + dtstr.substring(4, 6) + "-"
					+ dtstr.substring(6) + " 00:00:00.000'");
		}
	}

	/**
	 * Validates the date string to conform with the format "YYYYMMDD"
	 */
	public static final boolean isValidDate(String inDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
}
