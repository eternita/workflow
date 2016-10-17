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

package org.neuro4j.flows.nodes.callnode;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.neuro4j.tests.base.BaseFlowTestCase;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;

/**
 *
 */
public class CallNodeTestCase extends BaseFlowTestCase {

    @Test
    public void testCallOtherFlow() {

        FlowContext logicContext = executeFlow("org.neuro4j.flows.nodes.callnode.CallByNameFlow-StartNode1");
        String var2 = (String) logicContext.get("var1");

        assertEquals(var2, "test value");

    }

    @Test
    public void testCallWrongNode() {

        ExecutionResult result = executeFlowAndReturnResult("org.neuro4j.flows.nodes.callnode.CallByNameFlow-StartNode2", null);

        Assert.assertTrue(result.getException() instanceof FlowExecutionException);
        Assert.assertTrue(result.getException().getMessage().equals("org/neuro4j/flows/nodes/CallByNameFlow2 not found."));

    }

    @Test
    public void testCallDynamicNode() {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dynamicNodeName", "org.neuro4j.flows.nodes.callnode.CallByNameFlow2-StartNode2");

        String var2 = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.callnode.CallByNameFlow-StartNode3", params);

        assertEquals(var2, "EndNode3");

    }

    /**
     * Tests if callnode has 2 possible exits but next relation defined just for 1
     */
    @Test
    public void testCallDynamicNodeWith2Exits() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var123", "123");

        String var2 = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.callnode.CallByNameFlow-StartNode4", params);

        assertEquals(var2, "EndNode5");

    }

    @Test
    public void testCallDynamicNodeWith2ExitsA() {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("var123", "123");

        String var2 = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.callnode.CallByNameFlow-StartNode6", params);

        assertEquals(var2, "EndNode8");

    }

    @Test
    public void testCallDynamicNodeWith2ExitsB() {

        Map<String, Object> params = new HashMap<String, Object>();

        String var2 = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.callnode.CallByNameFlow-StartNode6", params);

        assertEquals(var2, "EndNode7");

    }
}
