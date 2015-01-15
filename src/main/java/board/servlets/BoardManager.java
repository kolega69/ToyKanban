package board.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import board.hendlers.CardRemover;
import board.hendlers.CardUpdater;
import board.hendlers.NewCardCreator;

import com.mongodb.DB;

/**
 * Servlet implementation class BoardManager
 */
@WebServlet(name = "BoardManager", urlPatterns = { "/board" })
public class BoardManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LogManager.getLogger();
       
   
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DB db = (DB) getServletContext().getAttribute("db");
		
		HttpSession session = request.getSession();
		String action = (String)session.getAttribute("action");
		if (action == null) {
			action = (String)request.getParameter("action");
			if (action == null) {
				logger.error("The action parameter is missing");
				action = "showBoard";
			}
		}
		logger.info(action);
		switch(action) {
		case "createCard":
			new NewCardCreator(db, request).create();
			logger.info("Was created a new Card");
			break;
		case "showBoard":
			session.removeAttribute("action");
			break;
		case "removeCard":
			new CardRemover(db, request).remove();
			break;
		default:
			new CardUpdater(db, request).update();
			logger.info("The Card was updated with action: " + action);
		}
		
		
		getServletContext().getRequestDispatcher("/board.jsp").forward(request, response);
		
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	

}
