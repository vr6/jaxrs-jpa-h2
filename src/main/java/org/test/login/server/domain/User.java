/*******************************************************************************
 * Copyright (c) 2015 Venkat Reddy
 * All rights reserved.
 *******************************************************************************/
package org.test.login.server.domain;

/**
 * A bean for building JSON response
 * @author vreddy
 */
public class User {
	private String user;

	public User(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
