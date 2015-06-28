/*******************************************************************************
 * Copyright (c) 2015 Venkat Reddy
 * All rights reserved.
 *******************************************************************************/
package org.test.login.server.domain;

/**
 * A bean for building JSON response
 * @author vreddy
 */
public class TruncDate implements Comparable<TruncDate>{
	private String date;

	/**
	 * Adds left-padding for the month and day fields. Required for sorting.
	 */
	public TruncDate(String dt) {
		if (dt == null){
			this.date = null;
			return;
		}
		String[] parts = dt.split("-");
		if (parts.length < 3) {
			this.date = dt;
			return;
		}
		if (parts[1].length() < 2) {
			parts[1] = "0" + parts[1]; 
		}
		if (parts[2].length() < 2) {
			parts[2] = "0" + parts[2]; 
		}
		this.date = parts[0] + "-" + parts[1] + "-" + parts[2];
	}
	public String getDate() {
		return date;
	}
	public void setDate(String dt) {
		this.date = dt;
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(TruncDate o) {
		return this.date.compareTo(o.getDate());
	}
}
