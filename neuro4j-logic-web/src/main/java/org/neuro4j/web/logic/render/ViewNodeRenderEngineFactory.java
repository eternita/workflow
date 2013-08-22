package org.neuro4j.web.logic.render;

import java.util.logging.Logger;


/**
 * 
 * Instantiate ViewNodeRenderEngine by class name
 *
 */
public class ViewNodeRenderEngineFactory {

	private final static Logger logger = Logger.getLogger(ViewNodeRenderEngineFactory.class.getName());

	public static ViewNodeRenderEngine getViewNodeRenderEngine(String name) throws ViewNodeRenderEngineNotFoundException
	{
		try {
			Class clazz = Class.forName(name);
			Object fObj = clazz.newInstance();
			if (fObj instanceof ViewNodeRenderEngine)
				return (ViewNodeRenderEngine) fObj;
				
		} catch (ClassNotFoundException e) {
			logger.severe("Can't create ViewNodeRenderEngine " + name + " " + e);
		} catch (InstantiationException e) {
			logger.severe("Can't create ViewNodeRenderEngine " + name + " " + e);
		} catch (IllegalAccessException e) {
			logger.severe("Can't create ViewNodeRenderEngine " + name + " " + e);
		}
		throw new ViewNodeRenderEngineNotFoundException("ViewNodeRenderEngine " + name + " not found");
	}

}
