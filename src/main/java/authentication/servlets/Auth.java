package authentication.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class Auth
 */
@WebServlet(name = "Auth", urlPatterns = { "/auth" })
public class Auth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LogManager.getLogger();
       
  	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		
		if ("sign_in".equals(action)) {
			//todo do login checking 
		} else if ("sign_up".equals(action)) {
			//todo do registration
		} else {
			logger.info("Unauthorized access request, redirect to login page");
			response.sendRedirect("login.html");
		}
	}
}
