package org.neuro4j.web.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.SimpleWorkflowEngine;


public class FlowDispatcherServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_VIEW_DIRECTORY = "/WEB-INF/"; 

	private static final String DEFAULT_VIEW_PAGE = "/WEB-INF/n4j-default-view.jsp";
	
	private static final String ERROR_PAGE = "/WEB-INF/error.jsp";
	
	private static final String LOGIC_CONTEXT_KEY = "LOGIC_CONTEXT";

	private static final String REQUEST_KEY = "REQUEST";

	private static final String RESPONSE_KEY = "RESPONSE";

	private static final String RENDER_VIEW_KEY = "VIEW_TEMPLATE";

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try
		{
			String urlStr = request.getRequestURI(); // /not-root-context/n4j/en_US/Welcome-start  /n4j/en_US/Welcome-start
			String[] urlSplit = urlStr.split("/");
			String flow; 
			String localeStr;
			if (this.getServletConfig().getServletContext().getContextPath().length() == 0 
					|| "/".equals(this.getServletConfig().getServletContext().getContextPath()))
			{
				flow = urlSplit[3]; 
				localeStr = urlSplit[2];
			} else {
				flow = urlSplit[4]; 
				localeStr = urlSplit[3];
			}
			
			String view = null;
			Map<String, Object> params = new HashMap<String, Object>();
			
			// put parameters from request to logicContext
			for (Object param : request.getParameterMap().keySet())
				params.put((String) param, request.getParameter((String) param));
			
			params.put(REQUEST_KEY, request);
			params.put(RESPONSE_KEY, response);

			LogicContext logicContext = SimpleWorkflowEngine.run(flow, params);

			
			// copy data from logicContext to request attributes (for usage in views)
			for (String key : logicContext.keySet())
			{
				request.setAttribute(key, logicContext.get(key));
			}
			
			request.setAttribute(LOGIC_CONTEXT_KEY, logicContext);
				
			view = (String) logicContext.get(RENDER_VIEW_KEY);

			// handle if view is not specified -> go to default page with trace
			if (null == view)
				view = DEFAULT_VIEW_PAGE;
			else
				view = DEFAULT_VIEW_DIRECTORY + view;
			
			getServletContext().getRequestDispatcher(view).forward(request, response);
				
		} catch (Exception e) {
			request.setAttribute("exception", e);
  		    getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
		}
		return;
	}
	
}
