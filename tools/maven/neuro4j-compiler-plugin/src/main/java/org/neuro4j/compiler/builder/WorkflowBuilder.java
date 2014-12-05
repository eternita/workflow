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

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.neuro4j.compiler.FileHolder;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.loader.f4j.FlowXML;
import org.neuro4j.workflow.loader.f4j.NodeXML;
import org.neuro4j.workflow.loader.f4j.TransitionXML;

public class WorkflowBuilder {

	public static void buildParameters(FileHolder workflowFileHolder, Map<String, String> parameters,	FlowXML workflow) throws FlowInitializationException {

		String initBlock = getInitBlockValue(workflow);

		parameters.put("{initBlock}", initBlock);

	}



	private static String getInitBlockValue(FlowXML workflow) throws FlowInitializationException {
		StringBuffer str = new StringBuffer(256);

		
		Collection<NodeXML> nodes = workflow.getEntities();
		Map<String, String> names = new HashMap<String, String>(nodes.size());
		int count = 0;
		str.append(" \n");
		for (NodeXML node: nodes)
		{
			AbstractBuilder builder =  AbstractBuilderFactory.getBuilder(node, names);
			if (builder != null)
			{
				count++;
				names.put(node.getUuid(), "node" + count);
				builder.buildNewStatment(str);	
			}
			
		}
		count = 0;
		for (NodeXML node: nodes)
		{
			Collection<TransitionXML> transitions =  node.getRelations();
			for(TransitionXML tr: transitions)
			{
				count++;
				names.put(tr.uuid(), "tr" + count);
				buildTransition(node, tr, str, names);
			}
			if (names.get(node.getUuid()) != null)
			{
				str.append("  ").append(names.get(node.getUuid())).append(".init(); \n");				
			}

			
		}
		
		return str.toString();
	}
	
	
	public static void buildTransition(NodeXML sourceNode, TransitionXML tr, StringBuffer buffer, Map<String, String> names)
	{
		String targetNode = tr.toNode();
		buffer.append(" \n");
		buffer.append("  Transition ").append(names.get(tr.uuid())).append(" = new Transition(this); \n");
		buffer.append("  ").append(names.get(tr.uuid())).append(".setUuid(\"").append(tr.uuid()).append("\");").append(" \n");
		buffer.append("  ").append(names.get(tr.uuid())).append(".setName(\"").append(tr.name()).append("\");").append(" \n");
		buffer.append("  ").append(names.get(tr.uuid())).append(".setFromNode(").append(names.get(sourceNode.getUuid())).append(");").append(" \n");
		buffer.append("  ").append(names.get(tr.uuid())).append(".setToNode(").append(names.get(targetNode)).append(");").append(" \n");
		buffer.append(" \n");
		buffer.append("  ").append(names.get(sourceNode.getUuid())).append(".registerExit(").append(names.get(tr.uuid())).append(");").append(" \n");
		buffer.append(" \n");
		
	}
	
	


}
