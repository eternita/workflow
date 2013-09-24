package org.neuro4j.web.logic.render;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neuro4j.logic.LogicContext;

/**
 * Interface for custom ViewNode render engine
 * 
 *
 */
public interface ViewNodeRenderEngine {

	/**
	 * 
	 * 
	 * @param response
	 * @param servletContext 
	 * @param logicContext
	 * @param viewTemplate
	 * @throws IOException
	 */
	public void render(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, LogicContext logicContext,	String viewTemplate) throws ViewNodeRenderExecutionException;
	
	public void init(ServletConfig config, ServletContext servletContext) throws ViewNodeRenderExecutionException;

}
