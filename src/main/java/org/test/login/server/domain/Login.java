/*******************************************************************************
 * Copyright (c) 2015 Venkat Reddy
 * All rights reserved.
 *******************************************************************************/
package org.test.login.server.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The Entity bean for the login data
 * @author vreddy
 */
@Entity
public class Login {

	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "login_time")
	private Calendar loginTime;

	private String user;
	private String attribute1;
	private String attribute2;
	private String attribute3;
	private String attribute4;

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAttribute1() {
		return attribute1;
	}

	public void setAttribute1(String attrib1) {
		this.attribute1 = attrib1;
	}

	public Calendar getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Calendar loginTime) {
		this.loginTime = loginTime;
	}

	public String getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	public String getAttribute3() {
		return attribute3;
	}

	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	public String getAttribute4() {
		return attribute4;
	}

	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}

	@Override
	public String toString() {
		return id + " " + getUser() + ":" + getAttribute1() + ":"
				+ getAttribute2() + ":" + getAttribute3() + ":"
				+ getAttribute4();
	}
}
