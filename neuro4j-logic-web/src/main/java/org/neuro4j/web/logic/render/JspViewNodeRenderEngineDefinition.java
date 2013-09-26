package org.neuro4j.web.logic.render;

public class JspViewNodeRenderEngineDefinition extends ViewNodeRenderEngineDefinition{

	@Override
	public String getName() {
		return "jsp";
	}

	@Override
	public String getFileExt() {
		return "jsp";
	}

	@Override
	public String getPathFilter() {
		return "*/WEB-INF/*";
	}



}
