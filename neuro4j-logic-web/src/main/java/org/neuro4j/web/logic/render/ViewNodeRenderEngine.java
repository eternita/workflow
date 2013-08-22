package org.neuro4j.web.logic.render;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.neuro4j.logic.LogicContext;

/**
 * Interface for custom ViewNode render engine
 * 
 *
 */
public interface ViewNodeRenderEngine {

	/**
	 * For Studio (design time)
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * 
	 * 
	 * @param response
	 * @param logicContext
	 * @param viewTemplate
	 * @throws IOException
	 */
	public void render(HttpServletResponse response, LogicContext logicContext, String viewTemplate) throws IOException;
	

}
