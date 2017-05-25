package org.neuro4j.workflow.loader.f4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.neuro4j.workflow.UUIDMgr;
import org.neuro4j.workflow.common.FlowExecutionException;
import static org.neuro4j.workflow.common.SWFParametersConstants.*;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.enums.DecisionCompTypes;
import org.neuro4j.workflow.enums.DecisionOperators;
import org.neuro4j.workflow.enums.StartNodeTypes;

/**
 * Builder to create Workflow object.
 *
 */
public class WorkflowBuilder {
	
	final FlowXML flowXml;
	
	final String flowName;
	
	private NodeXML currentNode;
	
	
	/**
	 * @param name ex. "org.neuro4j.MyWorkflow"
	 * @param startNodeName name of StartNode ex. "StartNode1"
	 */
	public WorkflowBuilder(String name, String startNodeName){
         this(name);
         addNode(createStartNode(startNodeName, StartNodeTypes.PUBLIC));
	}
	
	private WorkflowBuilder(String name){
		Objects.requireNonNull(name);
		
		this.flowXml = new FlowXML();
		this.flowName = name;
	}
	
	/**
	 * @return
	 */
	public WorkflowBuilder publicFlow(){
		this.flowXml.visibility = "PUBLIC";
		return this;
	}
	/**
	 * @return
	 */
	public WorkflowBuilder privateFlow(){
		this.flowXml.visibility = "PRIVATE";
		return this;
	}
	
	/**
	 * @param name
	 * @param type
	 * @return
	 */
	public static  NodeXML createStartNode(String name, StartNodeTypes type){
		NodeXML node = createNodeXML(name);
		node.type = NodeType.START.name();
		createConfig(node, START_NODE_TYPE, type.name());
		return node;
	}
	
	public  WorkflowBuilder addJoinNode(){
		addNext(createJoinNode());
		return this;
	}
	
	public  WorkflowBuilder addEndNode(){
		addNext(createEndNode());
		return this;
	}
	
	public  WorkflowBuilder addStartNode(String name){
		addNext(createStartNode(name, StartNodeTypes.PUBLIC));
		return this;
	}
	
	public  WorkflowBuilder addCallNode(String flow){
		addNext(createCallNode(flow, null));
		return this;
	}
	
	/**
	 * @param className
	 * @return
	 */
	public  CustomNodeBuilder addCustomNode(String className){
		return new CustomNodeBuilder(className);
	}
	
	public  class CustomNodeBuilder{
		
		private final NodeXML node;
		
		private CustomNodeBuilder(String className){
			node = createNodeXML(className);
			node.type = NodeType.CUSTOM.name();
			createConfig(node, LOGIC_NODE_CUSTOM_CLASS_NAME, className);
			addNext(node);
		}
		
		public CustomNodeBuilder withInputParam(String source, String target){
			createParameter(node, source, target, true);
			return this;
		}
		
		public CustomNodeBuilder withOutputParam(String source, String target){
			createParameter(node, source, target, false);
			return this;
		}
		
		public CustomNodeBuilder withOnError(NodeXML targetNode) {
			createTransitionXML(node, targetNode, TRANSITION_ERROR);
			addNode(targetNode);
			currentNode = node;
			return this;
		}
		
		public WorkflowBuilder done(){
			return WorkflowBuilder.this;
		}
		
	}

	/**
	 * @param name
	 * @return
	 */
	public static NodeXML createEndNode(String name){
		NodeXML node = createNodeXML(name);
		node.type = NodeType.END.name();
		return node;
	}
	
	/**
	 * Creates EndNode
	 * @return
	 */
	public static NodeXML createEndNode(){
		return createEndNode(generateId());
	}
	
	/**
	 * Creates JoinNode
	 * @return
	 */
	public static NodeXML createJoinNode(){
		return createJoinNode(false);
	}
	
	/**
	 * Returns JoinNode
	 * @param name
	 * @param fork
	 * @return
	 */
	public static NodeXML createJoinNode(boolean fork){
		NodeXML node = createNodeXML();
		node.type = NodeType.JOIN.name();
		createConfig(node, FORK, Boolean.toString(fork));
		return node;
	}
	
	public static NodeXML createDecisionNode(DecisionOperators operator, DecisionCompTypes type, String decisionKey, String compKey){
		NodeXML node = createNodeXML();
		node.type = NodeType.DECISION.name();
		createConfig(node, DECISION_NODE_OPERATOR, operator.name());
		createConfig(node, DECISION_NODE_COMP_TYPE, type.name());
		createConfig(node, DECISION_NODE_DECISION_KEY, decisionKey);
		createConfig(node, DECISION_NODE_COMP_KEY, compKey);
		return node;
	}
	
	
	public static NodeXML createLoopNode(String iteratorName, String elementName){
		NodeXML node = createNodeXML();
		node.type = NodeType.LOOP.name();
		// add validation
		createConfig(node, LOOP_NODE_ITERATOR, iteratorName);
		createConfig(node, LOOP_NODE_ELEMENT, elementName);
		return node;
	}
	
	public static NodeXML createCallNode(String callFlowName, String dynamicCallFlowName){
		NodeXML node = createNodeXML();
		node.type = NodeType.CALL.name();
		createConfig(node, CAll_NODE_FLOW_NAME, callFlowName);
		createConfig(node, CAll_NODE_DYNAMIC_FLOW_NAME, dynamicCallFlowName);
		return node;
	}
	
	public static NodeXML createSwitchNode(){
		NodeXML node = createNodeXML();
		node.type = NodeType.SWITCH.name();
		return node;
	}
	
	public static NodeXML createMapNode(Map<String, String> parameters){
		NodeXML node = createNodeXML();
		node.type = NodeType.MAP.name();
		Optional.ofNullable(parameters).orElse(Collections.emptyMap()).forEach((k,v)-> createParameter(node, k, v, true));
		return node;
	}
	
	public static NodeXML createNodeNode(String text){
		NodeXML node = createNodeXML();
		node.type = NodeType.NOTE.name();
		node.description = text;
		return node;
	}
	
	private  static ParameterXML createConfig(NodeXML node, String name, String value){
		ParameterXML parameter = new ParameterXML();
		parameter.key = name;
		parameter.value = value;
		node.config.add(parameter);
		return parameter;
	}
	
	private  static ParameterXML createParameter(NodeXML node, String name, String value, boolean isInput){
		ParameterXML parameter = new ParameterXML();
		parameter.key = name;
		parameter.value = value;
		parameter.input = isInput;
		node.parameters.add(parameter);
		return parameter;
	}
	
	private static NodeXML createNodeXML(String name){
		NodeXML node = new NodeXML();
		node.uuid = generateId();
		node.name = name;
		return node;
	}
	
	private static NodeXML createNodeXML(){
		return createNodeXML(generateId());
	}
	
	/**
	 * Adds node to the workflow and marks it as a currentNode
	 * @param node
	 * @return
	 */
	public WorkflowBuilder addNode(NodeXML node){
		this.currentNode = node;
		flowXml.nodes.add(node);
		return this;
	}
	
	/**
	 * Add next connector to currentNode
	 * @param node
	 * @return
	 */
	public WorkflowBuilder addNext(NodeXML node){
		if (this.currentNode != null){
			createTransitionXML(currentNode, node, TRANSITION_NEXT);
			addNode(node);
		} else {
			 // throw exception
		}

		return this;
	}

	
	private  static TransitionXML createTransitionXML(NodeXML sourceNode, NodeXML targetNode, String name){
		TransitionXML transition = new TransitionXML();
		transition.uuid = generateId();
		transition.name = name;
		transition.fromNode = sourceNode.getUuid();
		transition.toNode = targetNode.getUuid();
		sourceNode.transitions.add(transition);
		return transition;
	}
	
	/**
	 * Returns current node
	 * @return
	 */
	public NodeXML getCurrentNode(){
		return currentNode;
	}
	
	private static String generateId() {
		return UUIDMgr.getInstance().createUUIDString();
	}
	
	/**
	 * Converts FlowXML to Workflow object
	 * @return workflow
	 * @throws FlowExecutionException
	 */
	public Workflow build() throws FlowExecutionException{
		if (this.currentNode != null && !NodeType.END.name().equals(this.currentNode.type)){
            // finish with EndNode
			addNext(createEndNode());
		}
		return FlowConverter.netXML2net(flowXml, this.flowName);
	}
	
}
