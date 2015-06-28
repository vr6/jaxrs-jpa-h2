/*******************************************************************************
 * Copyright (c) 2015 Venkat Reddy
 * All rights reserved.
 *******************************************************************************/
package org.test.login.server.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.test.login.server.domain.TruncDate;

/**
 * @author vreddy
 * REST Handler for http://<jboss-node>/test/dates
 */
@Stateless
@Path("/dates")
public class DateResource {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Returns List of truncated date values to be converted into JSON Array.
	 * In reality, it returns ALL the consecutive dates as 
	 * the number of days (~540) is far lesser than number of logins (100K)
	 * So. all the dates have some logins.
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TruncDate> getDates() {

		// The oreder by clause doesn't really work 
		// due to lack left-padding on the date and month values.
		// However it helps the TreeSet to some extent (partially sorted)
		String sql = "SELECT distinct (concat ("
				+ "EXTRACT(YEAR FROM c.loginTime), '-' ,"
				+ "EXTRACT(MONTH FROM c.loginTime), '-' ,"
				+ "EXTRACT(DAY FROM c.loginTime)"
				+ ")) as dt FROM Login c order by dt";

		Query q = em.createQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Object> rows = q.getResultList();

		Set<TruncDate> tset = new TreeSet<TruncDate>();
		if (rows.size() > 0) {
			for (Object row : rows) {
				tset.add(new TruncDate(row.toString()));
			}
		}
		List<TruncDate> response = new ArrayList<TruncDate>(tset);
		return response;
	}
}
