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

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.loader.f4j.NodeType;
import org.neuro4j.workflow.loader.f4j.NodeXML;

public class AbstractBuilderFactory {

	public static AbstractBuilder getBuilder(NodeXML node,
			Map<String, String> names) throws FlowExecutionException {

		NodeType type = NodeType.valueOf(node.type);

		switch (type) {
		case JOIN:
			return new JoinBlockBuilder(node, names);

		case START:
			return new StartBlockBuilder(node, names);

		case END:
			return new EndBlockBuilder(node, names);

		case CALL:
			return new CallBlockBuilder(node, names);

		case DECISION:
			return new DecisionBlockBuilder(node, names);

		case LOOP:
			return new LoopBlockBuilder(node, names);

		case CUSTOM:
			return new CustomBlockBuilder(node, names);

		case MAP:
			return new MapBlockBuilder(node, names);

		case SWITCH:
			return new SwitchBlockBuilder(node, names);

		case VIEW:
			return new ViewBlockBuilder(node, names);
		default:
			return null;

		}

	}

}
