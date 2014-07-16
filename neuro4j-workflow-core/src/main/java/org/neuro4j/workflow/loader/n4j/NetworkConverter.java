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

package org.neuro4j.workflow.loader.n4j;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.neuro4j.workflow.Workflow;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.SWFParametersConstants;
import org.neuro4j.workflow.enums.DecisionCompTypes;
import org.neuro4j.workflow.enums.DecisionOperators;
import org.neuro4j.workflow.enums.FlowVisibility;
import org.neuro4j.workflow.enums.StartNodeTypes;
import org.neuro4j.workflow.node.CallNode;
import org.neuro4j.workflow.node.CustomNode;
import org.neuro4j.workflow.node.DecisionNode;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.JoinNode;
import org.neuro4j.workflow.node.KeyMapper;
import org.neuro4j.workflow.node.LoopNode;
import org.neuro4j.workflow.node.StartNode;
import org.neuro4j.workflow.node.SwitchNode;
import org.neuro4j.workflow.node.Transition;
import org.neuro4j.workflow.node.ViewNode;
import org.neuro4j.workflow.node.WorkflowNode;

public class NetworkConverter {

    /**
     * In case of exception input stream are closed by JAXB
     * 
     * @param xml
     * @return
     * @throws FlowInitializationException
     */
    public static Workflow xml2workflow(InputStream xml, String flow)
            throws ConvertationException, FlowInitializationException {
        if (null == xml)
            return null;

        NetworkXML net = null;
        try {
            JAXBContext ctx = JAXBContext.newInstance(NetworkXML.class);

            Unmarshaller um = ctx.createUnmarshaller();
            net = (NetworkXML) um.unmarshal(xml);
            if (null == net)
                return null;

            return netXML2net(net, flow);

        } catch (JAXBException e) {
            throw new ConvertationException("Can't convert stream to workflow",
                    e);
        }
    }

    private static String getFlowPackage(String flow)
    {
        String flowPackage = "default";
        int index = flow.lastIndexOf("/");
        if (index > 0)
        {
            flowPackage = flow.substring(0, index);
        } else if (flow.contains(".")) {
            flowPackage = flow;
        }
        return flowPackage;
    }

    private static Workflow netXML2net(NetworkXML net, String flow) throws FlowInitializationException {

        Workflow network = new Workflow(flow, getFlowPackage(flow));

        for (EntityXML e : net.getEntities()) {

            Map<String, String> parameters = new HashMap<String, String>();

            for (PropertyXML rep : e.getRepresentations())
                if (rep.getValue() != null && !rep.getValue().trim().equals(""))
                {
                    parameters.put(rep.getKey(), rep.getValue());
                }

            if (parameters.get(SWFConstants.SWF_BLOCK_CLASS) != null) {
                WorkflowNode entity = createNode(network, e, parameters);
                if (entity != null)
                {
                    entity.registerNodeInWorkflow();
                }

            }

        }

        for (WorkflowNode entity : network.getNodes()) {

            if (null != entity) {
                EntityXML e = net.getById(entity.getUuid());

                for (RelationTailXML rp : e.getRelations()) {
                    EntityXML entityXML = net.getById(rp.getUuid());
                    Transition transition = new Transition(network);
                    transition.setUuid(entityXML.getUuid());
                    transition.setName(entityXML.getName());

                    List<PropertyXML> representation = entityXML
                            .getRepresentations();
                    for (PropertyXML propertyXML : representation) {
                        if (SWFConstants.EndUUID.equals(propertyXML.getKey())) {
                            String targetUuid = propertyXML.getValue();
                            WorkflowNode toNode = network.getById(targetUuid);
                            if (null != toNode)
                                transition.setToNode(toNode);
                            entity.registerExit(transition);
                        }
                    }

                }
            }
            entity.init();
        }

        return network;
    }

    private static WorkflowNode createNode(Workflow workflow, EntityXML e, Map<String, String> parameters) throws FlowInitializationException
    {
        String blockClass = parameters.get(SWFConstants.SWF_BLOCK_CLASS);

        WorkflowNode node = null;

        if (blockClass.equals(StartNode.class.getCanonicalName())) {
            node = createStartNode(workflow, e, parameters);
        } else if (blockClass.equals(CallNode.class.getCanonicalName())) {
            node = createCallNode(workflow, e, parameters);
        } else if (blockClass.equals(DecisionNode.class.getCanonicalName())) {
            node = createDecisionNode(workflow, e, parameters);
        } else if (blockClass.equals(LoopNode.class.getCanonicalName())) {
            node = createLoopNode(workflow, e, parameters);
        } else if (blockClass.equals(ViewNode.class.getCanonicalName())) {
            node = createViewNode(workflow, e, parameters);
        } else if (blockClass.equals(SwitchNode.class.getCanonicalName())) {
            node = createSwitchNode(workflow, e, parameters);
        } else if (blockClass.equals(EndNode.class.getCanonicalName())) {
            node = createEndNode(workflow, e, parameters);
        } else if (blockClass.equals(JoinNode.class.getCanonicalName())) {
            node = createJoinNode(workflow, e, parameters);
        } else if (blockClass.equals(KeyMapper.class.getCanonicalName())) {
            node = createKeyMapperNode(workflow, e, parameters);
        } else if (blockClass.equals("org.neuro4j.workflow.node.NetConfig")) {
            loadFlowConfiguration(workflow, e, parameters);
        }

        String className = parameters.get("SWF_CUSTOM_CLASS");

        if (className == null) {
            className = parameters.get(SWFConstants.SWF_BLOCK_CLASS);
        } else {
            node = createCustomNode(workflow, e, parameters);
        }
        if (className == null) {
            throw new FlowInitializationException("Executable node is unknown");
        }
        return node;
    }

    /**
     * Loads flow configuration (like visibility).
     *
     * @param workflow
     * @param e
     * @param parameters
     */
    private static void loadFlowConfiguration(Workflow workflow, EntityXML e, Map<String, String> parameters)
    {
        String visibility =  parameters.get(SWFParametersConstants.NETWORK_VISIBILITY);
        workflow.setVisibility(FlowVisibility.getByName(visibility));
    }

    /**
     * Creates StartNode.
     * 
     * @param workflow
     * @param e
     * @param parameters
     * @return
     */
    private static StartNode createStartNode(Workflow workflow, EntityXML e, Map<String, String> parameters) {
        StartNode startNode = new StartNode(e.getName(), e.getUuid(), workflow);

        String nodeType = parameters.get(SWFParametersConstants.START_NODE_TYPE);
        nodeType = (nodeType == null || nodeType.trim().equals("")) ? StartNodeTypes.getDefaultType().name() : nodeType.toUpperCase();
        startNode.setType(StartNodeTypes.valueOf(nodeType));
        return startNode;
    }

    /**
     * Creates CallNode.
     * 
     * @param workflow
     * @param e
     * @param parameters
     * @return
     */
    private static WorkflowNode createCallNode(Workflow workflow, EntityXML e, Map<String, String> parameters) {
        CallNode node = new CallNode(e.getName(), e.getUuid(), workflow);
        node.setCallFlow(parameters.get(SWFParametersConstants.CAll_NODE_FLOW_NAME));
        node.setDynamicFlownName(parameters.get(SWFParametersConstants.CAll_NODE_DYNAMIC_FLOW_NAME));
        return node;
    }

    /**
     * Creates SwitchNode.
     * 
     * @param workflow
     * @param e
     * @param parameters
     * @return
     */
    private static WorkflowNode createSwitchNode(Workflow workflow, EntityXML e, Map<String, String> parameters) {
        SwitchNode node = new SwitchNode(e.getName(), e.getUuid(), workflow);

        String relationName = parameters.get(SWFParametersConstants.SWITCH_NODE_ACTION_NAME);
        if (relationName == null)
        {
            relationName = SWFParametersConstants.SWITCH_NODE_DEFAULT_PARAMETER_VALUE;
        }

        node.setRelationName(relationName);

        return node;
    }

    /**
     * Creates EndNode.
     * @param workflow
     * @param e
     * @param parameters
     * @return
     */
    private static WorkflowNode createEndNode(Workflow workflow, EntityXML e, Map<String, String> parameters) {
        EndNode node = new EndNode(e.getName(), e.getUuid(), workflow);
        return node;
    }

    /**
     * Creates ViewNode
     * 
     * @param workflow
     * @param e
     * @param parameters
     * @return
     */
    private static WorkflowNode createViewNode(Workflow workflow, EntityXML e, Map<String, String> parameters) {
        ViewNode node = new ViewNode(e.getName(), e.getUuid(), workflow);

        node.setStaticTemplateName(parameters.get(SWFParametersConstants.VIEW_NODE_TEMPLATE_NAME));
        node.setDynamicTemplateName(parameters.get(SWFParametersConstants.VIEW_NODE_TEMPLATE_DYNAMIC_NAME));

        String renderType = parameters.get(SWFParametersConstants.VIEW_NODE_RENDER_TYPE);
        if (renderType == null)
        {
            renderType = "jsp";
        }

        node.setRenderType(renderType);

        return node;
    }

    /**
     * Creates DecisionNode.
     * 
     * @param workflow
     * @param e
     * @param parameters
     * @return
     */
    private static WorkflowNode createDecisionNode(Workflow workflow,
            EntityXML e, Map<String, String> parameters) {
        DecisionNode node = new DecisionNode(e.getName(), e.getUuid(), workflow);
        String sOperator = parameters
                .get(SWFParametersConstants.DECISION_NODE_OPERATOR);

        if (sOperator != null) {
            node.setOperator(DecisionOperators.valueOf(sOperator));
        }

        String sCompType = parameters
                .get(SWFParametersConstants.DECISION_NODE_COMP_TYPE);

        if (sCompType != null) {
            node.setCompTypes(DecisionCompTypes.valueOf(sCompType));
        } else {
            node.setCompTypes(DecisionCompTypes.context);
        }

        node.setDecisionKey(parameters
                .get(SWFParametersConstants.DECISION_NODE_DECISION_KEY));

        node.setComparisonKey(parameters
                .get(SWFParametersConstants.DECISION_NODE_COMP_KEY));

        return node;
    }

    /**
     * Creates LoopNode.
     * 
     * @param workflow
     * @param e
     * @param parameters
     * @return
     */
    private static WorkflowNode createLoopNode(Workflow workflow, EntityXML e, Map<String, String> parameters) {
        LoopNode node = new LoopNode(e.getName(), e.getUuid(), workflow);

        node.setIteratorKey(parameters.get(SWFParametersConstants.LOOP_NODE_ITERATOR));
        node.setElementKey(parameters.get(SWFParametersConstants.LOOP_NODE_ELEMENT));
        return node;
    }

    /**
     * Creates JoinNode.
     * 
     * @param workflow
     * @param e
     * @param parameters
     * @return
     */
    private static WorkflowNode createJoinNode(Workflow workflow, EntityXML e, Map<String, String> parameters) {
        JoinNode node = new JoinNode(e.getName(), e.getUuid(), workflow);
        return node;
    }

    /**
     * 
     * Creates and loads KeyMapper node.
     * 
     * @param workflow
     * @param e
     * @param parameters
     * @return
     */
    private static WorkflowNode createKeyMapperNode(Workflow workflow, EntityXML e, Map<String, String> parameters) {
        KeyMapper node = new KeyMapper(e.getName(), e.getUuid(), workflow);
        Set<String> keys = parameters.keySet();
        for (String key : keys)
        {
            if (key.startsWith(SWFParametersConstants.MAPPER_NODE_KEY_PREFIX))
            {
                node.addParameter(key, parameters.get(key));
            }
        }
        return node;
    }

    /**
     * Creates and loads CustomNode and parameters.
     * 
     * @param workflow
     * @param e
     * @param parameters
     * @return
     */
    private static WorkflowNode createCustomNode(Workflow workflow, EntityXML e, Map<String, String> parameters) {
        CustomNode node = new CustomNode(e.getName(), e.getUuid(), workflow);
        Set<String> keys = parameters.keySet();
        for (String key : keys)
        {
            if (key.startsWith(SWFParametersConstants.CUSTOM_BLOCK_PARAMETER_PREFIX) && key.endsWith(":" + SWFParametersConstants.CUSTOM_BLOCK_INPUT_PARAMETER_PREFIX))
            {
                node.addParameter(key, parameters.get(key));
            }
            if (key.startsWith(SWFParametersConstants.CUSTOM_BLOCK_PARAMETER_PREFIX) && key.endsWith(":" + SWFParametersConstants.CUSTOM_BLOCK_OUTPUT_PARAMETER_PREFIX))
            {
                node.addOutParameter(key, parameters.get(key));
            }

        }
        String className = parameters.get("SWF_CUSTOM_CLASS");
        node.setExecutableClass(className);

        return node;
    }

}
