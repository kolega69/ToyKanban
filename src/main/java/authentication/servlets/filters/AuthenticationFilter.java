package authentication.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet Filter implementation class {@code AuthenticationFilter}.
 * Validates user session. 
 */
public class AuthenticationFilter implements Filter {

	private final Logger logger = LogManager.getLogger();

	/**
	 * Validates user session.
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String uri = req.getRequestURI();
		

		HttpSession session = req.getSession(false);
		logger.info("Requested Resource::" + uri + " --and session is " + session);

		if (session == null && !(uri.endsWith("html") || uri.endsWith("css") || uri.endsWith("auth"))) {
			logger.error("Unauthorized access request");
			res.sendRedirect("login.html");
		} else if (session != null && (uri.endsWith("board.jsp") || uri.endsWith("html"))) {
			res.sendRedirect("board");
		} else {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {
	}
	
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
