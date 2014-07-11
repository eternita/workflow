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

package org.neuro4j.flows.nodes.mappernode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;
import org.neuro4j.tests.base.BaseFlowTestCase;
import org.neuro4j.workflow.FlowContext;

/**
 *
 */
public class MapperNodeTestCase extends BaseFlowTestCase {

    @Test
    public void testMap2Values() {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", "123");
        params.put("var3", "Hello");

        FlowContext logicContext = executeFlowWithoutErrors("org.neuro4j.flows.nodes.mappernode.MapperFlow-StartNode1", params);
        String var2 = (String) logicContext.get("var2");
        String var4 = (String) logicContext.get("var4");
        assertEquals(var2, "123");
        assertEquals(var4, "Hello");

    }

    @Test
    public void testMapNull() {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("var1", "123");

        String var1 = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.mappernode.MapperFlow-StartNodeMapNull", params, "var1");

        assertEquals(var1, null);

    }

    @Test
    public void testMapStringConstant() {

        Map<String, Object> params = new HashMap<String, Object>();

        String var1 = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.mappernode.MapperFlow-StartNodeStringConstant", params, "var1");

        assertEquals(var1, "Hello world!");

    }

    /**
     * This method test creation of objects in KeyMapper. like source: "(java.util.HashSet)" target:var1
     */
    @Test
    public void testDynamicObjectCreation() {

        Map<String, Object> params = new HashMap<String, Object>();

        Object var1 = executeFlowAndReturnObject("org.neuro4j.flows.nodes.mappernode.MapperFlow-DynamicCreation1", params, "var1");
        if (var1 == null || !(var1 instanceof HashSet))
        {
            fail("HashSet has not been created.");
        }

    }

}
