package authentication.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import authentication.handlers.AuthHandler;
import board.beans.User;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;

/**
 * Servlet implementation class {@code Auth}. Works as controller and provide
 * authorization and authentication actions.
 */
@WebServlet(name = "Auth", urlPatterns = { "/auth" })
public class Auth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LogManager.getLogger();

	/**
	 * Provide authorization, authentication and check email actions.
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");
		if (action == null) {
			logger.info("There are no action parameter, redirect to login page");
			response.sendRedirect("login.html");
		}

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();

		String userEmail = request.getParameter("email").trim();
		String userPassword = request.getParameter("password").trim();
		DB db = (DB) getServletContext().getAttribute("db");
		AuthHandler handler = new AuthHandler(db);

		// todo make perfect validations here or in the login page.
		try {
			switch (action) {
				case "sign_in": // login action
					User user = handler.getUserIfExists(userEmail, userPassword);
					if (user == null) {
						out.print("false");
					} else {
						HttpSession session = request.getSession();
						session.setAttribute("user", user);
						session.setAttribute("action", "showBoard");

						String remember = request.getParameter("remember");
						// remember user if checkbox was selected
						if ("yes".equals(remember)) { 
							session.setMaxInactiveInterval(0);
							logger.info("User " + user.getEmail() + " remembered");
						}

						response.sendRedirect("board");
					}
					break;
				case "sign_up": // registration action
					String userName = request.getParameter("name").trim();
					User newUser = handler.createUserIfNotExists(userName,
							userEmail, userPassword);
					if (newUser == null) {
						response.sendRedirect("login.html");
					} else {
						request.setAttribute("user", newUser);
						request.getRequestDispatcher("/welcome.jsp").forward(
								request, response);
					}
					break;
				case "check_email": //check email action
					BasicDBObject dbUser = handler.getUserByEmail(userEmail);
					out.print(dbUser == null);
					break;
			}

		} catch (NoSuchAlgorithmException e) {
			// todo show warning message to user
			e.printStackTrace();
			logger.error("NoSuchAlgorithmException");
		} catch (InvalidKeySpecException e) {
			// todo show warning message to user
			e.printStackTrace();
			logger.error("InvalidKeySpecException");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("board");
	}

}
