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

package org.neuro4j.flows.common;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.node.WorkflowProcessor;

public class WorkflowEngineTestCase {


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParseFlow() {

        try {
            WorkflowProcessor.parseFlowName("org.neuro4j.Flow1-StartNode1");
        } catch (FlowExecutionException e) {
              fail();
        }
        try {
        	WorkflowProcessor.parseFlowName("Flow1-StartNode1");
        } catch (FlowExecutionException e) {
              fail();
        }
        try {
        	WorkflowProcessor.parseFlowName("org.neuro4j.Flow1");
            fail();
        } catch (FlowExecutionException e) {

        }
        try {
        	WorkflowProcessor.parseFlowName("org.neuro4j.Flow1-Start-Start");
            fail();
        } catch (FlowExecutionException e) {

        }
        try {
        	WorkflowProcessor.parseFlowName(null);
            fail();
        } catch (FlowExecutionException e) {

        }

        
    }


}
