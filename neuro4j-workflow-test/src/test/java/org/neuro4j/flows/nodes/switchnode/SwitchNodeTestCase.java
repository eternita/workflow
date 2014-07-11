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

package org.neuro4j.flows.nodes.switchnode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.neuro4j.tests.base.BaseFlowTestCase;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.common.FlowExecutionException;

/**
 *
 */
public class SwitchNodeTestCase extends BaseFlowTestCase {

    @Test
    public void testRelationDynamic1() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("relationName", "rel1");

        String lastNode = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeDynamic", params);

        assertEquals(lastNode, "EndNode1");

    }

    @Test
    public void testRelationDynamic2() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("relationName", "rel2");

        String lastNode = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeDynamic", params);

        assertEquals(lastNode, "EndNode2");

    }

    @Test
    public void testRelationStatic1() {

        Map<String, Object> params = new HashMap<String, Object>();

        String lastNode = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeStatic", params);

        assertEquals(lastNode, "EndNode3");

    }

    @Test
    public void testRelationWrongConfiguration() {

        ExecutionResult lastNode = (ExecutionResult) executeFlowAndReturnResult("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeDynamic", null);

        assertEquals(lastNode.getException().getMessage(), "Switch: NextStep is unknown.");

    }

    @Test
    public void testRelationDefaultExit() {
        try {

            String lastNode = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeDefaultExit");

            assertEquals(lastNode, "EndNode5");

        } catch (FlowExecutionException e) {
            fail(e.toString());
        }
    }
    // @Test
    // public void testRelationEmptyExit() {
    // try {
    //
    // String lastNode = (String )
    // executeFlowAndReturnObject("org.neuro4j.flows.nodes.switchnode.SwitchFlow-StartNodeEmptyExit");
    //
    // assertEquals(lastNode, "EndNode7");
    //
    // } catch (FlowExecutionException e) {
    // fail(e.toString());
    // }
    // }
}
