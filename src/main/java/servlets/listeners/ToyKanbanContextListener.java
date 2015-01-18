package servlets.listeners;

import java.net.UnknownHostException;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@WebListener
public class ToyKanbanContextListener implements ServletContextListener {
	
	MongoClient moClient;
	DB db;

	/**
	 * Initialize mongo database and set it to the servlet context
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext seCxt = sce.getServletContext();
		String dbHost = seCxt.getInitParameter("host");
		int dbPort = Integer.parseInt(seCxt.getInitParameter("port")); 
		
		try {
			String userName = seCxt.getInitParameter("userName");
			String password = seCxt.getInitParameter("password");
			String database = seCxt.getInitParameter("database");
			
			if (userName == null || password == null) {
				moClient = new MongoClient(dbHost, dbPort);
			} else {   
				// database authentication if needed.
				ServerAddress address = new ServerAddress(dbHost, dbPort);
				MongoCredential credential = MongoCredential
						.createMongoCRCredential(userName, database, password.toCharArray());
				moClient = new MongoClient(address, Arrays.asList(credential));
			}
			db = moClient.getDB(database);
			seCxt.setAttribute("db", db);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

	public void contextDestroyed(ServletContextEvent sce) {
		if (null != moClient) {
			moClient.close();
		}
	}
}
