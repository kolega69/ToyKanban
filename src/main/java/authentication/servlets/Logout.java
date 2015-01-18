package authentication.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import board.beans.User;

/**
 * Servlet implementation class @{code Logout}. Provide logout action
 * for the user.
 */
@WebServlet(name = "Logout", urlPatterns = { "/logout" })
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LogManager.getLogger();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * Make logout for the particular user.
	 * Invalidates the users sessions and forward them to login page.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie: cookies) {
				if(cookie.getName().equals("JSESSIONID")) {
					logger.info(cookie.getValue());
				}
			}
		}
		
		HttpSession session = request.getSession(false);
		if(session != null) {
			User user = (User)session.getAttribute("user");
			logger.info("User: " + user.getName() + "did logout");
			session.invalidate();
		}
		response.sendRedirect("login.html");
	}

}
