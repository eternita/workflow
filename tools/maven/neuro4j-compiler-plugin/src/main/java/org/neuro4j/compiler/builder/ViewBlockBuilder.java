package org.neuro4j.compiler.builder;

/*
 * Copyright (c) 2013-2014, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Map;

import org.neuro4j.workflow.common.SWFParametersConstants;
import org.neuro4j.workflow.loader.f4j.NodeXML;
import org.neuro4j.workflow.node.ViewNode;

public class ViewBlockBuilder extends AbstractBuilder {

	public ViewBlockBuilder(NodeXML node, Map<String, String> names) {
		super(node, names);
	}

	protected void buidNodeSpecificCode(StringBuffer buffer) {

		String staticTemplateName = node
				.getConfig(SWFParametersConstants.VIEW_NODE_TEMPLATE_NAME);
		addSetter(buffer, "setStaticTemplateName", staticTemplateName, true);

		String dynamicTemplateName = node
				.getConfig(SWFParametersConstants.VIEW_NODE_TEMPLATE_DYNAMIC_NAME);
		addSetter(buffer, "setDynamicTemplateName", dynamicTemplateName, true);

		String renderImpl = node.getConfig(SWFParametersConstants.RENDER_IMP);
		addSetter(buffer, "setRenderImpl", renderImpl, true);

		String renderType = node
				.getConfig(SWFParametersConstants.VIEW_NODE_RENDER_TYPE);
		if (renderType == null) {
			renderType = "jsp";
		}

		addSetter(buffer, "setRenderType", renderType, true);

	}

	@Override
	public String getImpClassName() {
		return ViewNode.class.getSimpleName();
	}

}
