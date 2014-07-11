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

package org.neuro4j.flows.custom;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neuro4j.tests.base.BaseFlowTestCase;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;

public class InputParametersTestCase extends BaseFlowTestCase {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMandatoryParametersWithEmptyValue() {

        Map<String, Object> params = new HashMap<String, Object>();

        ExecutionResult result = executeFlowAndReturnResult("org.neuro4j.flows.custom.FlowWithParameters-StartNode1", params);
        Assert.assertTrue(result.getException() instanceof FlowExecutionException);

    }

    @Test
    public void testMandatoryParametersWithMapping() {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("var1", "123");
        FlowContext FlowContext = executeFlowWithoutErrors("org.neuro4j.flows.custom.FlowWithParameters-StartNode1", params);

    }

    @Test
    public void testMandatoryParametersWithDirectValue1() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mandatoryParameter", "123");
        ExecutionResult result = executeFlowAndReturnResult("org.neuro4j.flows.custom.FlowWithParameters-StartNode1", params);
        Assert.assertTrue(result.getException() instanceof FlowExecutionException);
    }

    @Test
    public void testMandatoryParametersWithWrongType() {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mandatoryParameter", new Integer(123));
        ExecutionResult result = executeFlowAndReturnResult("org.neuro4j.flows.custom.FlowWithParameters-StartNode2", params);
        Assert.assertTrue(result.getException() instanceof FlowExecutionException);

    }

    @Test
    public void testMandatoryParametersWithDirectValue2() {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mandatoryParameter", "123");
        FlowContext FlowContext = executeFlowWithoutErrors("org.neuro4j.flows.custom.FlowWithParameters-StartNode2", params);

    }

    /**
     * if put "value1 " + "value2" as input parameter - it must concatenate it
     */
    @Test
    public void testConcatenation2StringForInputparameter() {

        Map<String, Object> params = new HashMap<String, Object>();
        FlowContext FlowContext = executeFlowWithoutErrors("org.neuro4j.flows.custom.FlowWithParameters-StartNode4", params);

        String string2 = (String) FlowContext.get("string2");

        Assert.assertEquals("value1 value2", string2);

    }

    /**
     * put "value1 " + bean.name into input parameter
     */
    @Test
    public void testConcatenationStringAndBeanForInputparameter() {

        Map<String, Object> params = new HashMap<String, Object>();
        TestBean bean = new TestBean();
        bean.setStringVar("name1");
        params.put("var1", bean);

        FlowContext FlowContext = executeFlowWithoutErrors("org.neuro4j.flows.custom.FlowWithParameters-StartNode5", params);

        String string2 = (String) FlowContext.get("string2");

        Assert.assertEquals("value1 name1", string2);

    }
}
