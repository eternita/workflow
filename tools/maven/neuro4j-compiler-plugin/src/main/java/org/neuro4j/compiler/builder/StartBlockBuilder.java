package org.neuro4j.compiler.builder;

/*
 * Copyright (c) 2013-2016, Neuro4j
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
import org.neuro4j.workflow.node.StartNode;
import org.neuro4j.workflow.enums.StartNodeTypes;

public class StartBlockBuilder extends AbstractBuilder {

	public StartBlockBuilder(NodeXML node, Map<String, String> names) {
		super(node, names);
	}

	@Override
	public String getImpClassName() {
		return StartNode.class.getSimpleName();
	}
	
	@Override
	protected void buidNodeSpecificCode(StringBuffer buffer){
		String nodeType = node.getConfig(SWFParametersConstants.START_NODE_TYPE);
        nodeType = (nodeType == null || nodeType.trim().equals("")) ? StartNodeTypes.getDefaultType().name() : nodeType.toUpperCase();
        if (StartNodeTypes.PRIVATE.name().equalsIgnoreCase(nodeType))
        {
        	buffer.append("  ").append(getVarName()).append(".setType(StartNodeTypes.PRIVATE); \n");
        } else {
        	buffer.append("  ").append(getVarName()).append(".setType(StartNodeTypes.PUBLIC); \n");
        }
	}

}
