package org.neuro4j.compiler.builder;

import java.util.Map;


import org.neuro4j.workflow.common.SWFParametersConstants;
import org.neuro4j.workflow.loader.f4j.NodeXML;
import org.neuro4j.workflow.node.CallNode;

public class CallBlockBuilder extends AbstractBuilder {

	public CallBlockBuilder(NodeXML node, Map<String, String> names) {
		super(node, names);
	}

	protected void buidNodeSpecificCode(StringBuffer buffer) {
		String callFlowname = node
				.getConfig(SWFParametersConstants.CAll_NODE_FLOW_NAME);
		addSetter(buffer, "setCallFlow", callFlowname, true);

		String dynFlowName = node
				.getConfig(SWFParametersConstants.CAll_NODE_DYNAMIC_FLOW_NAME);
		addSetter(buffer, "setDynamicFlownName", dynFlowName, true);

	}

	@Override
	public String getImpClassName() {
		return CallNode.class.getSimpleName();
	}

}
