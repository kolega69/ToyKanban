package servlets.listeners;

import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mongodb.DB;
import com.mongodb.MongoClient;

@WebListener
public class ToyKanbanContextListener implements ServletContextListener {
	
	MongoClient moClient;
	DB db;

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext secont = sce.getServletContext();
		String dbHost = secont.getInitParameter("host");
		int dbPort = Integer.parseInt(secont.getInitParameter("port")); 
		
		try {
			moClient = new MongoClient(dbHost, dbPort);
			db = moClient.getDB("toyKanban");
			secont.setAttribute("db", db);
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
