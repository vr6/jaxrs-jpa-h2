/*******************************************************************************
 * Copyright (c) 2015 Venkat Reddy
 * All rights reserved.
 *******************************************************************************/
package org.test.login.server.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;
import org.test.login.server.domain.User;

/**
 * @author vreddy
 * REST Handler for http://<jboss-node>/test/users
 */
@Stateless
@Path("/users")
public class UserResource {

	@PersistenceContext
	private EntityManager em;

	private static final Logger log = Logger.getLogger(UserResource.class);

	/**
	 * @param info
	 * @return List of unique users to be converted into JSON Array.
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers(@Context UriInfo info) { // for JSON array

		MultivaluedMap<String, String> params = info.getQueryParameters();
		List<String> where = new ArrayList<String>();

		String start = params.getFirst("start");
		String end = params.getFirst("end");
		DateUtil.addDateParam(start, true, where);
		DateUtil.addDateParam(end, false, where);

		StringBuilder sql = new StringBuilder(
				"SELECT distinct(c.user) FROM Login c");
		if (where.size() > 0) {
			sql.append(" where ");
			for (String val : where) {
				sql.append(val + " and ");
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
		}
		sql.append(" order by c.user");
		log.debug("SQL = " + sql.toString());

		Query q = em.createQuery(sql.toString());
		@SuppressWarnings("unchecked")
		List<Object> rows = q.getResultList();

		List<User> response = new ArrayList<User>();
		if (rows.size() > 0) {
			for (Object row : rows) {
				response.add(new User((String) row));
			}
		}
		return response;
	}
}
