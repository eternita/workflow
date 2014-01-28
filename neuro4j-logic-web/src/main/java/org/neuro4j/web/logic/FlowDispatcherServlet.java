package org.neuro4j.web.logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.core.log.Logger;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.SWFConstants;
import org.neuro4j.logic.swf.SimpleWorkflowEngine;
import org.neuro4j.web.logic.render.ViewNodeRenderEngine;
import org.neuro4j.web.logic.render.ViewNodeRenderEngineFactory;


public class FlowDispatcherServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	private static final String RENDER_ENGINE_KEY = SWFConstants.RENDER_ENGINE_KEY;
	

	private static final String LOCALE_KEY = "LOCALE";
	
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try
		{			
			request.setCharacterEncoding("utf-8");
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
				
				Logger.debug(this, "Using '{}' locale", locale);
				
				params.put(LOCALE_KEY, locale);
			}
			
			// put parameters from request to logicContext
			for (Object param : request.getParameterMap().keySet())
				params.put((String) param, request.getParameter((String) param));
			
			params.put(WebFlowConstants.REQUEST_KEY, request);
			params.put(WebFlowConstants.RESPONSE_KEY, response);

			LogicContext logicContext = SimpleWorkflowEngine.run(flow, params);

			
			// copy data from logicContext to request attributes (for usage in views)
			for (String key : logicContext.keySet())
			{
				if (key.equals(WebFlowConstants.REQUEST_KEY) || key.equals(WebFlowConstants.RESPONSE_KEY))
					continue;
				
				request.setAttribute(key, logicContext.get(key));
			}
			
			request.setAttribute(WebFlowConstants.LOGIC_CONTEXT_KEY, logicContext);
				
			view = (String) logicContext.get(WebFlowConstants.RENDER_VIEW_KEY);

			// handle if view is not specified -> go to default page with trace
			if (null == view){
				view = WebFlowConstants.DEFAULT_VIEW_PAGE;				
			} else if (view.startsWith("/")){
				view = WebFlowConstants.DEFAULT_VIEW_DIRECTORY + view.replaceFirst("/", "");
			} else{
				view = WebFlowConstants.DEFAULT_VIEW_DIRECTORY + view;
			}
			
			String renderEngineImpl = (String) logicContext.get(RENDER_ENGINE_KEY);
			
			Logger.debug(this, "Using '{}' web render", renderEngineImpl);
			
		    if (null != renderEngineImpl && renderEngineImpl.trim().length() > 0 && !renderEngineImpl.trim().equals("jsp"))
			{
				// get render engine
				ViewNodeRenderEngine renderEngine = ViewNodeRenderEngineFactory.getViewNodeRenderEngine(getServletConfig(), getServletContext(), renderEngineImpl);
				// custom rendering
				renderEngine.render(request, response, getServletContext(), logicContext, view);
			
			} else {
				
				// default rendering with JSP
				getServletContext().getRequestDispatcher(view).forward(request, response);
			}
				
		} catch (Exception e) {
			request.setAttribute("exception", e);
			Logger.error(this, e.getMessage(), e);
  		    getServletContext().getRequestDispatcher(WebFlowConstants.ERROR_PAGE).forward(request, response);
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
