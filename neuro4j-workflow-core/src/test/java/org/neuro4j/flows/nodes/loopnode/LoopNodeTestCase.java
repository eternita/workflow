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

package org.neuro4j.flows.nodes.loopnode;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.neuro4j.tests.base.BaseFlowTestCase;

/**
 *
 */
public class LoopNodeTestCase extends BaseFlowTestCase {

    @Test
    public void testArrayList() {

        Map<String, Object> params = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>();
        list.add("value1");
        list.add("value2");
        list.add("value3");

        params.put("array1", list);

        String lastValue = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.loopnode.LoopExample-StartNode1", params, "var1");

        assertSame("value3", lastValue);

    }

    @Test
    public void testArray() {

        Map<String, Object> params = new HashMap<String, Object>();
        String[] list = new String[] { "value1", "value2", "value3" };

        params.put("array1", list);

        String lastValue = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.loopnode.LoopExample-StartNode1", params, "var1");

        assertSame("value3", lastValue);

    }

}
