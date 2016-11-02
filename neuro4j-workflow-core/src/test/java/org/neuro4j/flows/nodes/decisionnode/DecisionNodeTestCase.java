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

package org.neuro4j.flows.nodes.decisionnode;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.neuro4j.tests.base.BaseFlowTestCase;

/**
 * TODO: add tests
 */
public class DecisionNodeTestCase extends BaseFlowTestCase {

    @Test
    public void testStringEqualsTrue() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", "123");
        params.put("var2", "123");

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEqStr", params);

        assertEquals(lastValue, "EndNode1");

    }

    @Test
    public void testStringEqualsFalse() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", "123");
        params.put("var2", "1234");

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEqStr", params);

        assertEquals(lastValue, "EndNode2");

    }

    @Test
    public void testStringEqualsWithInteger() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", "123");
        params.put("var2", new Integer("123"));

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEqStr", params);

        assertEquals(lastValue, "EndNode2");

    }

    @Test
    public void testDefinedTrue() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", "123");

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeDefined", params);

        assertEquals(lastValue, "EndNode3");

    }

    @Test
    public void testDefinedFalse() {

        Map<String, Object> params = new HashMap<String, Object>();

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeDefined", params);

        assertEquals(lastValue, "EndNode4");

    }

    @Test
    public void testUndefinedTrue() {

        Map<String, Object> params = new HashMap<String, Object>();

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeUndefined", params);

        assertEquals(lastValue, "EndNode5");

    }

    @Test
    public void testUndefinedFalse() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", "123");

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeUndefined", params);

        assertEquals(lastValue, "EndNode6");

    }

    @Test
    public void testEmptyStringTrue() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", "");

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEmptyString", params);

        assertEquals(lastValue, "EndNode7");

    }

    @Test
    public void testEmptyStringFalse() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", "123");

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEmptyString", params);

        assertEquals(lastValue, "EndNode8");

    }

    @Test
    public void testEmptyStringNull() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", null);

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEmptyString", params);

        assertEquals(lastValue, "EndNode8");

    }

    @Test
    public void testEmptyStringNotString() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", new Integer(123));

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeEmptyString", params);

        assertEquals(lastValue, "EndNode8");

    }

    @Test
    public void testHasElementsArrayList() {

        Map<String, Object> params = new HashMap<String, Object>();
        List<String> array = new ArrayList<String>();
        array.add("123");
        params.put("var1", array);

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeHasElements", params);
        assertEquals(lastValue, "EndNode9");

    }

    @Test
    public void testHasElementsArrayListEmpty() {

        Map<String, Object> params = new HashMap<String, Object>();
        List<String> array = new ArrayList<String>();
        params.put("var1", array);

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeHasElements", params);

        assertEquals(lastValue, "EndNode10");

    }

    @Test
    public void testHasElementsStringArray() {

        Map<String, Object> params = new HashMap<String, Object>();
        String[] array = new String[] { "123", "12" };

        params.put("var1", array);

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeHasElements", params);

        assertEquals(lastValue, "EndNode9");

    }

    @Test
    public void testHasElementsStringArrayEmpty() {

        Map<String, Object> params = new HashMap<String, Object>();
        String[] array = new String[] { };

        params.put("var1", array);

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNodeHasElements", params);

        assertEquals(lastValue, "EndNode10");

    }
    
    @Test
    public void testStringNotEqualsTrue() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", "123");
        params.put("var2", "12");

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNotEqString", params);

        assertEquals(lastValue, "EndNode11");

    }
    
    @Test
    public void testStringNotEqualsFalse() {

    	String var1 = UUID.randomUUID().toString();
    	
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", var1);
        params.put("var2", var1);

        String lastValue = (String) executeFlowAndReturnLastNode("org.neuro4j.flows.nodes.decisionnode.DecisionFlow-StartNotEqString", params);

        assertEquals(lastValue, "EndNode12");
    }
}
