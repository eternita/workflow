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

package org.neuro4j.flows.network;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.neuro4j.tests.base.BaseFlowTestCase;
import org.neuro4j.workflow.FlowContext;

public class NetworkTestCase extends BaseFlowTestCase {

    @Test
    public void testPublicNetworkPublicNode() {

        FlowContext logicContext = executeFlow("org.neuro4j.flows.network.NetworkTestFlow-StartNode1");
        String var1 = (String) logicContext.get("value1");

        assertEquals(var1, "test");

    }

    @Test
    public void testPublicNetworkPrivateNode() {

        executeFlowAndCheckExceptioMessage(
                "org.neuro4j.flows.network.NetworkTestFlow-StartNode2", null,
                "Node 'StartNode2' is not public");
    }

    @Test
    public void testCallPrivateNodeByCallNode() {

        FlowContext logicContext = executeFlow("org.neuro4j.flows.network.NetworkTestFlow-StartNode3");
        String var1 = (String) logicContext.get("var1");

        assertEquals(var1, "test");

    }

    @Test
    public void testCallPrivateNodeByCallNodeInOtherPackage() {
        executeFlowAndCheckExceptioMessage(
                "org.neuro4j.flows.network.NetworkTestFlow-StartNode4",
                null,
                "Node StartNode1 in package org.neuro4j.flows.network.p2 is private and can be used just inside package.");
    }

    @Test
    public void testCallPublicNodeByCallNodeInOtherPackage() {

        FlowContext logicContext = executeFlow("org.neuro4j.flows.network.NetworkTestFlow-StartNode5");
        String var1 = (String) logicContext.get("var1");
        assertEquals(var1, "test value");

    }
}
