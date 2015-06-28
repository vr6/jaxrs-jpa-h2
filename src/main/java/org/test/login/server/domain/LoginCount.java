/*******************************************************************************
 * Copyright (c) 2015 Venkat Reddy
 * All rights reserved.
 *******************************************************************************/
package org.test.login.server.domain;

/**
 * A bean for building JSON response
 * @author vreddy
 */
public class LoginCount {
	private String user;
	private int logins;

	public LoginCount() {
	}

	public LoginCount(String user, long logins) {
		this.user = user;
		this.logins = (int) logins;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getLogins() {
		return logins;
	}

	public void setLogins(int logins) {
		this.logins = logins;
	}

	@Override
	public String toString() {
		return "LoginCount [user=" + user + ", logins=" + logins + "]";
	}
}
