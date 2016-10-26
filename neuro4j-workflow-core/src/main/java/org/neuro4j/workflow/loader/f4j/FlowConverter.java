/**
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.neuro4j.workflow.loader.f4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import static org.neuro4j.workflow.common.SWFParametersConstants.*;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.enums.DecisionCompTypes;
import org.neuro4j.workflow.enums.DecisionOperators;
import org.neuro4j.workflow.enums.StartNodeTypes;
import org.neuro4j.workflow.node.CallNode;
import org.neuro4j.workflow.node.CustomNode;
import org.neuro4j.workflow.node.DecisionNode;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.FlowParameter;
import org.neuro4j.workflow.node.JoinNode;
import org.neuro4j.workflow.node.KeyMapper;
import org.neuro4j.workflow.node.LoopNode;
import org.neuro4j.workflow.node.StartNode;
import org.neuro4j.workflow.node.SwitchNode;
import org.neuro4j.workflow.node.Transition;
import org.neuro4j.workflow.node.ViewNode;
import org.neuro4j.workflow.node.WorkflowNode;
import org.neuro4j.workflow.utils.Validation;

/**
 * Loads workflow from file.
 *
 */
public class FlowConverter {
    
	public static Workflow xml2workflow(Reader xml, String flow)
			throws ConvertationException, FlowInitializationException {
		if (null == xml)
			return null;
		try {
			JAXBContext ctx = JAXBContext.newInstance(FlowXML.class);

			Unmarshaller um = ctx.createUnmarshaller();
			FlowXML flowxml = (FlowXML) um.unmarshal(xml);
			if (null == flowxml)
				return null;

			return netXML2net(flowxml, flow);

		} catch (JAXBException e) {
			throw new ConvertationException("Can't convert stream to workflow", e);
		}
	}

	public static Workflow xml2workflow(InputStream xml, String flow)
			throws ConvertationException, FlowInitializationException, UnsupportedEncodingException {
		 return xml2workflow(new InputStreamReader(xml, "UTF-8"), flow);
	}

	private static Workflow netXML2net(FlowXML net, String flow)
			throws FlowInitializationException {

		Workflow network = new Workflow(flow, getFlowPackage(flow));

		for (NodeXML e : net.nodes) {

			createNode(network, e);

		}

		for (NodeXML entity : net.nodes) {

			WorkflowNode node = network.getById(entity.getUuid());
			if (node != null) {
				for (TransitionXML transitionXml : entity.getRelations()) {
					Transition transition = transitionXml.createTransition(node);
					transition.setToNode(network.getById(transitionXml.toNode));
					node.registerExit(transition);
				}

				node.init();
			}

		}

		return network;
	}

    private static String getFlowPackage(final String flow) {
    	String flowPackage = "default";
    	try {
			FlowParameter parameter = FlowParameter.parse(flow);
			flowPackage = parameter.getFlowPackage();
		} catch (FlowExecutionException e) {

		}
    	return flowPackage;
    }

    private static WorkflowNode createNode(Workflow workflow, NodeXML e) throws FlowInitializationException {
        WorkflowNode node = null;

        NodeType type = NodeType.valueOf(e.type);

        switch (type) {
            case JOIN:
                node = createJoinNode(workflow, e);

            case START:
                node = createStartNode(workflow, e);
                workflow.registerStartNode((StartNode)node);
                break;
            case END:
                node = createEndNode(workflow, e);
                break;
            case CALL:
                node = createCallNode(workflow, e);
                break;
            case DECISION:
                node = createDecisionNode(workflow, e);
                break;
            case LOOP:
                node = createLoopNode(workflow, e);
                break;
            case CUSTOM:
                node = createCustomNode(workflow, e);
                break;
            case MAP:
                node = createKeyMapperNode(workflow, e);
                break;
            case SWITCH:
                node = createSwitchNode(workflow, e);
                break;
            case VIEW:
                node = createViewNode(workflow, e);
                break;
            case NOTE:
                break;

            default:

                throw new FlowInitializationException("Executable node is unknown");
        }
        if (node != null){
            workflow.registerNode(node);	
        }

        return node;
    }

    private static StartNode createStartNode(Workflow workflow, NodeXML e) {
        StartNode startNode = new StartNode(e.getName(), e.getUuid());

        String nodeType = e.getConfig(START_NODE_TYPE);

        nodeType = (nodeType == null || nodeType.trim().equals("")) ? StartNodeTypes
                .getDefaultType().name() : nodeType.toUpperCase();
        startNode.setType(StartNodeTypes.valueOf(nodeType));
        return startNode;
    }

    private static WorkflowNode createCallNode(Workflow workflow, NodeXML e) {
        CallNode node = new CallNode(e.getName(), e.getUuid());
        node.setCallFlow(e.getConfig(CAll_NODE_FLOW_NAME));
        node.setDynamicFlownName(e.getConfig(CAll_NODE_DYNAMIC_FLOW_NAME));
        return node;
    }

    private static WorkflowNode createSwitchNode(Workflow workflow, NodeXML e) {
        String relationName = e.getConfig(SWITCH_NODE_ACTION_NAME);
        if (relationName == null) {
            relationName = SWITCH_NODE_DEFAULT_PARAMETER_VALUE;
        }
        SwitchNode node = new SwitchNode(e.getName(), e.getUuid(), relationName);

        return node;
    }

    private static WorkflowNode createEndNode(Workflow workflow, NodeXML e) {
        EndNode node = new EndNode(e.getName(), e.getUuid());
        return node;
    }

    private static WorkflowNode createViewNode(Workflow workflow, NodeXML e) {
        ViewNode node = new ViewNode(e.getName(), e.getUuid());

        node.setStaticTemplateName(e.getConfig(VIEW_NODE_TEMPLATE_NAME));
        node.setDynamicTemplateName(e.getParameter(VIEW_NODE_TEMPLATE_DYNAMIC_NAME));
        node.setRenderImpl(e.getConfig(RENDER_IMP));
        

        String renderType = e.getConfig(VIEW_NODE_RENDER_TYPE);
        if (renderType == null) {
            renderType = "jsp";
        }

        node.setRenderType(renderType);

        return node;
    }

    private static WorkflowNode createDecisionNode(Workflow workflow, NodeXML e) {
        DecisionNode node = new DecisionNode(e.getName(), e.getUuid());
        String sOperator = e.getConfig(DECISION_NODE_OPERATOR);

        if (sOperator != null) {
            node.setOperator(DecisionOperators.valueOf(sOperator));
        }

        String sCompType = e.getConfig(DECISION_NODE_COMP_TYPE);

        if (sCompType != null) {
            node.setCompTypes(DecisionCompTypes.valueOf(sCompType));
        } else {
            node.setCompTypes(DecisionCompTypes.context);
        }

        node.setDecisionKey(e.getConfig(DECISION_NODE_DECISION_KEY));

        node.setComparisonKey(e.getConfig(DECISION_NODE_COMP_KEY));

        return node;
    }

    private static WorkflowNode createLoopNode(Workflow workflow, NodeXML e) {
        LoopNode node = new LoopNode(e.getName(), e.getUuid());

        node.setIteratorKey(e.getConfig(LOOP_NODE_ITERATOR));
        node.setElementKey(e.getConfig(LOOP_NODE_ELEMENT));
        return node;
    }

    private static WorkflowNode createJoinNode(Workflow workflow, NodeXML e) {
        JoinNode node = new JoinNode(e.getName(), e.getUuid());
        return node;
    }

    private static WorkflowNode createKeyMapperNode(Workflow workflow, NodeXML e) {
        KeyMapper node = new KeyMapper(e.getName(), e.getUuid());
        for (ParameterXML key : e.parameters) {
            node.addParameter(key.key, key.value);
        }
        return node;
    }

    private static WorkflowNode createCustomNode(Workflow workflow, NodeXML e) throws FlowInitializationException {
        String executableClass = e.getConfig("SWF_CUSTOM_CLASS");
        Validation.requireNonNull(executableClass, () -> new FlowInitializationException("Executable class not defined for node: " + e.getUuid() + " flow: " + workflow.getPackage() + workflow.getFlowName()));

        CustomNode node = new CustomNode(executableClass, e.getName(), e.getUuid());
        for (ParameterXML param : e.parameters) {
            if (param.input == null || param.input) {
                node.addParameter(param.key, param.value);
            } else {
                node.addOutParameter(param.key, param.value);
            }
        }       

        return node;
    }

}
