package org.neuro4j.compiler.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.neuro4j.compiler.FileHolder;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.loader.f4j.FlowXML;
import org.neuro4j.workflow.loader.f4j.NodeXML;
import org.neuro4j.workflow.loader.f4j.TransitionXML;

public class WorkflowBuilder {

	public static void buildParameters(FileHolder workflowFileHolder, Map<String, String> parameters,	FlowXML workflow) throws FlowExecutionException {

		String initBlock = getInitBlockValue(workflow);

		parameters.put("{initBlock}", initBlock);

	}



	private static String getInitBlockValue(FlowXML workflow) throws FlowExecutionException {
		StringBuffer str = new StringBuffer(256);

		
		Collection<NodeXML> nodes = workflow.nodes;
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
				names.put(tr.uuid, "tr" + count);
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
		String targetNode = tr.toNode;
		buffer.append(" \n");
		buffer.append("  Transition ").append(names.get(tr.uuid)).append(" = new Transition(); \n");
		buffer.append("  ").append(names.get(tr.uuid)).append(".setUuid(\"").append(tr.uuid).append("\");").append(" \n");
		buffer.append("  ").append(names.get(tr.uuid)).append(".setName(\"").append(tr.name).append("\");").append(" \n");
		buffer.append("  ").append(names.get(tr.uuid)).append(".setFromNode(").append(names.get(sourceNode.getUuid())).append(");").append(" \n");
		buffer.append("  ").append(names.get(tr.uuid)).append(".setToNode(").append(names.get(targetNode)).append(");").append(" \n");
		buffer.append(" \n");
		buffer.append("  ").append(names.get(sourceNode.getUuid())).append(".registerExit(").append(names.get(tr.uuid)).append(");").append(" \n");
		buffer.append(" \n");
		
	}
	
	


}
