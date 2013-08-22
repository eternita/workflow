package org.neuro4j.web.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.SimpleWorkflowEngine;
import org.neuro4j.web.logic.render.ViewNodeRenderEngine;
import org.neuro4j.web.logic.render.ViewNodeRenderEngineFactory;


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
	
	private static final String RENDER_ENGINE_KEY = "RENDER_ENGINE_KEY";
	

	private static final String LOCALE_KEY = "LOCALE";
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try
		{
			String urlStr = request.getRequestURI(); // /not-root-context/n4j/en_US/Welcome-start  /n4j/en_US/Welcome-start
			String[] urlSplit = urlStr.split("/");
			String flow = urlSplit[urlSplit.length - 1]; // the last part
			
			String view = null;
			Map<String, Object> params = new HashMap<String, Object>();
			
			{ // get locale
				String localeString = null;
				// check if locale defined in URL
				// locale can be right before flow (if any)
				if (urlSplit.length > 2)
					localeString = urlSplit[urlSplit.length - 2];
				
				Locale locale = getLocale(localeString);
				if (null == locale)
					locale = Locale.getDefault();
				
				params.put(LOCALE_KEY, locale);
			}
			
			// put parameters from request to logicContext
			for (Object param : request.getParameterMap().keySet())
				params.put((String) param, request.getParameter((String) param));
			
			params.put(REQUEST_KEY, request);
			params.put(RESPONSE_KEY, response);

			LogicContext logicContext = SimpleWorkflowEngine.run(flow, params);

			
			// copy data from logicContext to request attributes (for usage in views)
			for (String key : logicContext.keySet())
			{
				if (key.equals(REQUEST_KEY) || key.equals(RESPONSE_KEY))
					continue;
				
				request.setAttribute(key, logicContext.get(key));
			}
			
			request.setAttribute(LOGIC_CONTEXT_KEY, logicContext);
				
			view = (String) logicContext.get(RENDER_VIEW_KEY);

			// handle if view is not specified -> go to default page with trace
			if (null == view)
				view = DEFAULT_VIEW_PAGE;
			else
				view = DEFAULT_VIEW_DIRECTORY + view;
			
			String renderEngineImpl = (String) logicContext.get(RENDER_ENGINE_KEY);
//			renderEngineImpl = "org.neuro4j.web.logic.render.jasper.JasperViewNodeRenderEngine";
//			renderEngineImpl = "org.neuro4j.web.logic.render.DummyViewNodeRenderEngine";
			
			if (null != renderEngineImpl && renderEngineImpl.trim().length() > 0)
			{
				// get render engine
				ViewNodeRenderEngine renderEngine = ViewNodeRenderEngineFactory.getViewNodeRenderer(renderEngineImpl);
				// custom rendering
				renderEngine.render(response, logicContext, view);
			
			} else {
				
				// default rendering with JSP
				getServletContext().getRequestDispatcher(view).forward(request, response);
			}
				
		} catch (Exception e) {
			request.setAttribute("exception", e);
  		    getServletContext().getRequestDispatcher(ERROR_PAGE).forward(request, response);
		}
		return;
	}
	
	private Locale getLocale(String localeString)
	{
		if (null == localeString)
	        return Locale.getDefault();
			
        // Extract language
        int languageIndex = localeString.indexOf('_');
        String language = null;
        if (languageIndex == -1)
        {
            // No further "_" so is "{language}" only
            return new Locale(localeString, "");
        }
        else
        {
            language = localeString.substring(0, languageIndex);
        }

        // Extract country
        int countryIndex = localeString.indexOf('_', languageIndex + 1);
        String country = null;
        if (countryIndex == -1)
        {
            // No further "_" so is "{language}_{country}"
            country = localeString.substring(languageIndex+1);
            return new Locale(language, country);
        }
        else
        {
            // Assume all remaining is the variant so is "{language}_{country}_{variant}"
            country = localeString.substring(languageIndex+1, countryIndex);
            String variant = localeString.substring(countryIndex+1);
            return new Locale(language, country, variant);
        }
        
	}
	
}
