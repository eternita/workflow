package org.neuro4j.web.render.velocity;

import org.neuro4j.web.logic.render.ViewNodeRenderEngineDefinition;

public class VelocityViewNodeRenderEngineDefinition extends ViewNodeRenderEngineDefinition{

	@Override
	public String getName() {
		return "velocity";
	}

	@Override
	public String getFileExt() {
		return "vm";
	}

	@Override
	public String getPathFilter() {
		return "*/WEB-INF/*";
	}

}
