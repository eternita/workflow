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
import org.neuro4j.workflow.loader.f4j.NodeXML;

public abstract class AbstractBuilder {
	protected NodeXML node;
	protected Map<String, String> names;

	public AbstractBuilder(NodeXML node, Map<String, String> names) {
		this.node = node;
		this.names = names;
	}
	
	public void buildNewStatment(StringBuffer buffer) throws FlowInitializationException
	{
		buffer.append("  ").append(getImpClassName()).append(" ").append(names.get(this.node.getUuid())).append("  =  new ").append(getImpClassName()).append("(\"").append(getNodename());
		buffer.append("\", \"").append(node.getUuid()).append("\"); \n");

		buidNodeSpecificCode(buffer);
		
		
	}
	
	protected void buidNodeSpecificCode(StringBuffer buffer) {

	}

	public abstract String getImpClassName();

	protected String processInputParameter(String value) {
		if (value == null) {
			return null;
		}
		return value.replace("\"", "\\\"");
	}

	protected String getVarName() {
		return names.get(node.getUuid());
	}

	protected void addSetter(StringBuffer buffer, String setterName,
			String parameterValue, boolean isString) {
		if (parameterValue == null) {
			return;
		}
		if (parameterValue.contains("\"")) {
			parameterValue = parameterValue.replace("\"", "\\\"");
		}
		buffer.append("  ").append(getVarName()).append(".").append(setterName)
				.append("(");
		if (isString) {
			buffer.append("\"");
		}

		buffer.append(parameterValue);
		if (isString) {
			buffer.append("\"");
		}
		buffer.append("); \n");

	}

	protected String getNodename() {
		return node.getName().replace("\"", "\\\"");
	}

}
