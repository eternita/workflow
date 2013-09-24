package org.neuro4j.web.logic.render;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.logic.LogicContext;

/**
 * 
 * Development demo only
 *
 */
public class JspViewNodeRenderEngine implements ViewNodeRenderEngine {


	@Override
	public void render(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, LogicContext logicContext,	String viewTemplate) throws ViewNodeRenderExecutionException {

	}

	@Override
	public void init(ServletConfig config, ServletContext servletContext) throws ViewNodeRenderExecutionException {
		
	}
		
	




}
