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

package org.neuro4j.tests.base;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.WorkflowEngine;

public class BaseFlowTestCase {


    protected FlowContext executeFlowWithoutErrors(String flowName, Map<String, Object> parameters)
    {
        ExecutionResult result = WorkflowEngine.run(flowName, parameters);
        if (result.getException() != null)
        {
            result.getException().printStackTrace();
        }
        Assert.assertTrue(result.getException() == null);

        return result.getFlowContext();
    }

    protected FlowContext executeFlow(String flowName)
    {
        return executeFlowWithoutErrors(flowName, new HashMap<String, Object>());
    }

    protected Object executeFlowAndReturnObject(String flowName) throws FlowExecutionException
    {

        return executeFlowAndReturnLastNode(flowName, new HashMap<String, Object>());
    }

    protected String executeFlowAndReturnLastNode(String flowName, Map<String, Object> parameters)
    {
        ExecutionResult result = WorkflowEngine.run(flowName, parameters);
        Assert.assertTrue(result.getException() == null);
        return result.getLastSuccessfulNodeName();
    }

    protected ExecutionResult executeFlowAndReturnResult(String flowName, Map<String, Object> parameters)
    {
        ExecutionResult result = WorkflowEngine.run(flowName, parameters);

        return result;
    }

    protected Object executeFlowAndReturnObject(String flowName, Map<String, Object> parameters, String name)
    {
        ExecutionResult result = WorkflowEngine.run(flowName, parameters);

        return result.getFlowContext().get(name);
    }

    protected void executeFlowAndCheckExceptioMessage(String flowName, Map<String, Object> parameters, String message)
    {
        ExecutionResult result = WorkflowEngine.run(flowName, parameters);

        Assert.assertTrue(result.getException() instanceof FlowExecutionException);
        Assert.assertEquals(result.getException().getMessage(), message);
    }

}
