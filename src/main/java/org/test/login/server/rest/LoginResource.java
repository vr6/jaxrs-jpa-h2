/*******************************************************************************
 * Copyright (c) 2015 Venkat Reddy
 * All rights reserved.
 *******************************************************************************/
package org.test.login.server.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * @author vreddy
 * REST Handler for http://<jboss-node>/test/logins
 */
@Stateless
@Path("/logins")
public class LoginResource {

	@PersistenceContext
	private EntityManager em;

	private static final Logger log = Logger.getLogger(LoginResource.class);

	/**
	 * Returns a Map of (user, login_count) to be converted into JSON Object.
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Long> getCount(@Context UriInfo info) {
		MultivaluedMap<String, String> params = info.getQueryParameters();
		List<String> where = new ArrayList<String>();

		String start = params.getFirst("start");
		String end = params.getFirst("end");
		DateUtil.addDateParam(start, true, where);
		DateUtil.addDateParam(end, false, where);

		int pos = 1;
		pos = addAttrib(params.get("attribute1"), "attribute1", where, pos);
		pos = addAttrib(params.get("attribute2"), "attribute2", where, pos);
		pos = addAttrib(params.get("attribute3"), "attribute3", where, pos);
		pos = addAttrib(params.get("attribute4"), "attribute4", where, pos);

		StringBuilder sql = new StringBuilder(
				"SELECT c.user, count(c) FROM Login c");
		if (where.size() > 0) {
			sql.append(" where ");
			for (String val : where) {
				sql.append(val + " and ");
			}
			sql.delete(sql.length() - 5, sql.length() - 1);
		}
		sql.append(" group by c.user");
		log.info("SQL = " + sql.toString());

		Query q = em.createQuery(sql.toString());
		pos = 1;
		pos = setParameter (params.get("attribute1"), pos, q);
		pos = setParameter (params.get("attribute2"), pos, q);
		pos = setParameter (params.get("attribute3"), pos, q);
		pos = setParameter (params.get("attribute4"), pos, q);
		
		@SuppressWarnings("unchecked")
		List<Object[]> rows = q.getResultList();

		Map<String, Long> response = new HashMap<String, Long>();
		if (rows.size() > 0) {
			for (Object[] row : rows) {
				response.put((String) row[0], (Long) row[1]);
			}
		}
		return response;
	}

	/**
	 * Sets an attribute parameter to the query
	 */
	private int setParameter(List<String> attrib, int pos, Query q) {
		int idx = pos;
		if (attrib != null) {
			for (String val : attrib) {
				q.setParameter(idx++, val);
			}
		}
		return idx;
	}

	/**
	 * Adds an attribute parameter to SQL where clause
	 */
	private int addAttrib(List<String> attrib, String attribName,
			List<String> where, int pos) {
		int idx = pos;
		if (attrib != null) {
			if (attrib.size() < 2) {
				where.add("c." + attribName + " = ?" + idx++);
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i=0; i< attrib.size(); i++) {
					sb.append("c." + attribName + " = ?" + idx++ + " or ");
				}
				where.add("(" + sb.substring(0, sb.length() - 4) + ")");
			}
		}
		return idx;
	}
}
