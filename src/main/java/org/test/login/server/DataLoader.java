/*******************************************************************************
 * Copyright (c) 2015 Venkat Reddy
 * All rights reserved.
 *******************************************************************************/
package org.test.login.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

/**
 * The startup class for loading the 100K rows in to the data table.
 * @author vreddy
 *
 */
public class DataLoader implements ServletContextListener {

	private static final Logger log = Logger.getLogger(DataLoader.class);
	@PersistenceContext
	private EntityManager em;

	@Resource(mappedName = "java:jboss/datasources/ExampleDS")
	private DataSource ds;

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		log.info("Starting data loading");
		loadData();
		log.info("Finished data loading");
	}

	/**
	 * Inserts about 100K rows (logins) of data.
	 * Total time range is from (2005-01-01 to about 2015-06-25).
	 * Spreads the logins randomly across the time range
	 *
	 * Attrib values are prepared randomly using the format "???"
	 * where each "?" represents a capital alphabet (same for all three)
	 *
	 * User names are prepared using the format "u-?" where "?" represents
	 * a number between 0 and 100. Accommodates about 100 users.
	 */
	private void loadData() {
		try {
			Connection conn = ds.getConnection();
			Random rand = new Random();

			String sql = "insert into login (id, logintime, user, attribute1, attribute2, attribute3, attribute4) values "
					+ "(?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement st = conn.prepareCall(sql);

			Calendar cal = Calendar.getInstance();
			long now = cal.getTimeInMillis();
			cal.clear();
			cal.set(2005, 0, 1);
			long loginTime = cal.getTimeInMillis();
			int logins = 100000;

			// Average time elapsed between adjacent logins.
			int avgGap = (int) ((now - loginTime) / logins);
			
			List<String> users = createUsers();

			for (int i = 1; i <= logins; i++) {
				st.setInt(1, i);
				loginTime += (long) (rand.nextDouble() * avgGap * 2);
				st.setTimestamp(2, new Timestamp(loginTime));

				int userNum = rand.nextInt(users.size());
				st.setString(3, users.get(userNum));

				int c1 = 'A' + rand.nextInt(26);
				int c2 = 'A' + rand.nextInt(26);
				int c3 = 'A' + rand.nextInt(26);
				int c4 = 'A' + rand.nextInt(26);
				st.setString(4, "" + (char) c1 + (char) c1 + (char) c1);
				st.setString(5, "" + (char) c2 + (char) c2 + (char) c2);
				st.setString(6, "" + (char) c3 + (char) c3 + (char) c3);
				st.setString(7, "" + (char) c4 + (char) c4 + (char) c4);
				st.executeUpdate();
			}
			st.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private List<String> createUsers() {
		// some good mix of vowels and consonants to make a bit pronouncible user names
		final String[] vowels = {"a","e","i","o","u","y"};
		final String[] conso = {"b","c","d","m","p","r","s","t","v"};
		Random r = new Random();
		HashSet<String> users = new HashSet<String>();
		
		// about a hundred user names (some duplicates get eliminated by Set)
		for (int k=0; k<100; k++) {
			StringBuilder user = new StringBuilder();
			for (int i = 0; i < 7; i++) { // 7-character long user names
				user.append( i % 2 == 0 ? conso [r.nextInt(conso.length)]
						: vowels [r.nextInt(vowels.length)]);
			}
			users.add(user.toString());
		}
		return new ArrayList<String>(users);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
	}
}