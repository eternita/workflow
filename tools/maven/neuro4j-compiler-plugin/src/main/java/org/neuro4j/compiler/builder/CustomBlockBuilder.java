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

import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.SWFParametersConstants;
import org.neuro4j.workflow.loader.f4j.NodeXML;
import org.neuro4j.workflow.loader.f4j.ParameterXML;
import org.neuro4j.workflow.node.CustomNode;

public class CustomBlockBuilder extends AbstractBuilder {

	public CustomBlockBuilder(NodeXML node, Map<String, String> names) {
		super(node, names);
	}
	
	
	public void buildNewStatment(StringBuffer buffer) throws FlowInitializationException
	{
        String executableClass = node.getConfig(SWFParametersConstants.LOGIC_NODE_CUSTOM_CLASS_NAME);
        if (executableClass == null)
        {
            throw new FlowInitializationException("Executable class not defined for node: " + node.getUuid());
        }
		
		buffer.append("  ").append(getImpClassName()).append(" ").append(names.get(this.node.getUuid())).append("  =  new ").append(getImpClassName()).append("(\"").append(executableClass).append("\",").append("\"").append(node.getName());
		buffer.append("\", \"").append(node.getUuid()).append("\", this); \n");

		
        for (ParameterXML param : node.getParameters()) {
            if (param.input == null || param.input) {
                buffer.append("  ").append(names.get(this.node.getUuid())).append(".addParameter(\"").append(param.getKey()).append("\", \"").append(processInputParameter(param.getValue())).append("\"); \n");
            } else {

                buffer.append("  ").append(names.get(this.node.getUuid())).append(".addOutParameter(\"").append(param.getKey()).append("\", \"").append(param.getValue()).append("\"); \n");
            }
        }    

	}

	@Override
	public String getImpClassName() {

		return CustomNode.class.getSimpleName();
	}

}
