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

import org.neuro4j.workflow.common.SWFParametersConstants;
import org.neuro4j.workflow.loader.f4j.NodeXML;
import org.neuro4j.workflow.node.DecisionNode;
import org.neuro4j.workflow.enums.DecisionOperators;
import org.neuro4j.workflow.enums.DecisionCompTypes;

public class DecisionBlockBuilder extends AbstractBuilder {

	public DecisionBlockBuilder(NodeXML node, Map<String, String> names) {
		super(node, names);
	}

	@Override
	public String getImpClassName() {
		return DecisionNode.class.getSimpleName();
	}

	
	protected void buidNodeSpecificCode(StringBuffer buffer){
		 String sOperator = node.getConfig(SWFParametersConstants.DECISION_NODE_OPERATOR);

	        if (sOperator != null) {
	            addSetter(buffer, "setOperator",  "DecisionOperators." + DecisionOperators.valueOf(sOperator).toString(), false);
	        }

	        String sCompType = node.getConfig(SWFParametersConstants.DECISION_NODE_COMP_TYPE);

	        if (sCompType != null) {
	            addSetter(buffer, "setCompTypes", "DecisionCompTypes." + DecisionCompTypes.valueOf(sCompType).toString(), false);
	        } else {
	            addSetter(buffer, "setCompTypes", "DecisionCompTypes." +  DecisionCompTypes.context.toString(), false);
	        }

	        String desitionkey = node.getConfig(SWFParametersConstants.DECISION_NODE_DECISION_KEY);
	        addSetter(buffer, "setDecisionKey",  desitionkey, true);
	        
	        String desComType = node.getConfig(SWFParametersConstants.DECISION_NODE_COMP_KEY);
	        addSetter(buffer, "setComparisonKey",  desComType, true);
	}
	
	
	
	
}
