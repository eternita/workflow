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

import org.neuro4j.workflow.loader.f4j.NodeXML;
import org.neuro4j.workflow.loader.f4j.ParameterXML;
import org.neuro4j.workflow.node.KeyMapper;

public class MapBlockBuilder extends AbstractBuilder {

	public MapBlockBuilder(NodeXML node, Map<String, String> names) {
		super(node, names);
	}

	@Override
	public String getImpClassName() {
		return KeyMapper.class.getSimpleName();
	}

	protected void buidNodeSpecificCode(StringBuffer buffer) {
		for (ParameterXML param : node.parameters) {
			buffer.append("  ").append(getVarName()).append(".addParameter(\"")
					.append(param.key).append("\", \"")
					.append(processInputParameter(param.value))
					.append("\"); \n");
		}

	}

}
