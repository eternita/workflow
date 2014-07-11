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

package org.neuro4j.workflow.xml;

import org.neuro4j.workflow.Workflow;
import org.neuro4j.workflow.enums.DecisionCompTypes;
import org.neuro4j.workflow.enums.DecisionOperators;

public class DecisionNode extends WorkflowNode {

    private DecisionOperators operator = null;
    private DecisionCompTypes compTypes = null;
    private String decisionKey = null;
    private String comparisonKey = null;

    public DecisionNode(String name, String uuid, Workflow workflow)
    {
        super(name, uuid, workflow);
    }

    public DecisionOperators getOperator() {
        return operator;
    }

    public void setOperator(DecisionOperators operator) {
        this.operator = operator;
    }

    public DecisionCompTypes getCompTypes() {
        return compTypes;
    }

    public void setCompTypes(DecisionCompTypes compTypes) {
        this.compTypes = compTypes;
    }

    public String getDecisionKey() {
        return decisionKey;
    }

    public void setDecisionKey(String decisionKey) {
        this.decisionKey = decisionKey;
    }

    public String getComparisonKey() {
        return comparisonKey;
    }

    public void setComparisonKey(String comparisonKey) {
        this.comparisonKey = comparisonKey;
    }

}
