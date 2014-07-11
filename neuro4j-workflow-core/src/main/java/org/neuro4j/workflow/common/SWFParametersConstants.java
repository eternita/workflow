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

package org.neuro4j.workflow.common;

import org.neuro4j.workflow.loader.n4j.SWFConstants;

public class SWFParametersConstants {

    // StartNode section

    /**
     * Holds type of StartNode.
     * Possible values are:
     * 1) public - this node can be executed from outside
     * 2) private - node can be executed just from other node
     * 
     */
    public static final String START_NODE_TYPE = "SYS_START_NODE_TYPE";

    // end of StartNode section

    // CallNode section
    /*
     * If CallNode has both parameters it uses value CAll_NODE_DYNAMIC_FLOW_NAME first,
     * if there is no flow with such name - it should use CAll_NODE_FLOW_NAME
     */
    /**
     * Holds path to flow.
     * Ex. org.neuro4j.flows.VeiwProduct-Start
     * where "org.neuro4j.flows.VeiwProduct" - is flow file without extension "n4j"
     * "Start" - name of StartNode
     */
    public static final String CAll_NODE_FLOW_NAME = "SYS_FLOW_NAME";

    /**
     * Holds the name of dictionary parameter with flow name
     * 
     * Ex. myNextFlow
     * 
     * myNextFlow - org.neuro4j.flows.VeiwProduct-Start
     */
    public static final String CAll_NODE_DYNAMIC_FLOW_NAME = "SYS_DYNAMIC_FLOW_NAME";

    // end of CallNode section

    // LoopNode section
    /*
     * for(Object LOOP_NODE_ELEMENT: LOOP_NODE_ITERATOR)
     * {
     * 
     * }
     * 
     * or
     * 
     * while(LOOP_NODE_ITERATOR.hasNext())
     * {
     * Object LOOP_NODE_ELEMENT: LOOP_NODE_ITERATOR.next();
     * }
     */

    /**
     * Element which received from iterator, array or collection.
     */
    public static final String LOOP_NODE_ELEMENT = "SYS_ELEMENT_KEY";

    /**
     * Object which implement iterator interface, array or collection
     */
    public static final String LOOP_NODE_ITERATOR = "SYS_ITERATOR_KEY";
    // end of LoopNode section

    // DecisionNode section

    /**
     * Possible values are:
     * EQ_STR - equals operator for string - "= (string)"
     * NEQ_STR - not equals operator for string - "!= (string)"
     * DEFINED - checks if parameter is defined - "defined"
     * UNDEFINED - checks if parameter is not defined - "undefined"
     * EMPTY_STR - checks if parameter is empty string "" - "empty string");
     * 
     */
    public static final String DECISION_NODE_OPERATOR = "SYS_OPERATOR";
    /**
     * If DECISION_NODE_COMP_TYPE=constant field DECISION_NODE_COMP_KEY holds constant value ex. "Paris"
     * and DECISION_NODE_DECISION_KEY = city
     */
    public static final String DECISION_NODE_COMP_KEY = "SYS_COMP_KEY";
    /**
     * Possible values are:
     * 1) constant - value in DECISION_NODE_DECISION_KEY is constant and defined in field DECISION_NODE_COMP_KEY
     * 2) dictionary - value in DECISION_NODE_DECISION_KEY is name of parameter in context
     * 
     */
    public static final String DECISION_NODE_COMP_TYPE = "SYS_COMP_TYPE_KEY";
    /**
     * Holds name of parameter from context.
     */
    public static final String DECISION_NODE_DECISION_KEY = "SYS_DECISION_KEY";
    // end of Decision section

    // FollowByRelationNode section

    /**
     * TODO:
     * Holds name of relation which will be executed next.
     */
    public static final String SWITCH_NODE_ACTION_NAME = "SWF_PARAM_ACTION_NAME";
    public static final String SWITCH_NODE_DEFAULT_ACTION_NAME = SWFConstants.NEXT_RELATION_NAME;
    public static final String SWITCH_NODE_DEFAULT_ACTION_NAME_2 = "default";
    public static final String SWITCH_NODE_DEFAULT_PARAMETER_VALUE = "relationName";
    public static final String SWITCH_NODE_DEFAULT_ACTION = "DEFAULT";

    // end of FollowByRelationNode section

    // LogicNode section
    /**
     * Holds class name with package but without ".class"
     * ex. org.neuro4j.core.SaveToParameter
     */
    public static final String LOGIC_NODE_CUSTOM_CLASS_NAME = "SWF_CUSTOM_CLASS";
    // end of LogicNode section

    // MapperNode section
    /**
     * TODO:
     * MapperNode hold parameter like
     * <item value="newKey:oldKey" key="SWF_PARAM_KEY_1"/>
     * constants like
     * <item value="newKey:\"string constant value\"" key="SWF_PARAM_KEY_1"/>
     */
    public static final String MAPPER_NODE_KEY_PREFIX = "SWF_PARAM_KEY_";
    // end of MapperNode section

    public static final String CUSTOM_BLOCK_PARAMETER_PREFIX = "Parameter";

    public static final String CUSTOM_BLOCK_INPUT_PARAMETER_PREFIX = "IN";

    public static final String CUSTOM_BLOCK_OUTPUT_PARAMETER_PREFIX = "OUT";

    public static final String VIEW_NODE_TEMPLATE_NAME = "SWF_PARAM_VIEW_TEMPLATE";

    public static final String VIEW_NODE_TEMPLATE_DYNAMIC_NAME = "SWF_PARAM_VIEW_DYNAMIC_TEMPLATE";

    public static final String VIEW_NODE_RENDER_TYPE = "SWF_PARAM_VIEW_RENDER_TYPE";

    public static final String NETWORK_VISIBILITY = "KEY_VISIBILITY";
}
