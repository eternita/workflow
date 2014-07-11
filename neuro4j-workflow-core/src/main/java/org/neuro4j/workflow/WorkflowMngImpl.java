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

package org.neuro4j.workflow;

import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.loader.WorkflowLoader;

public class WorkflowMngImpl {

    private static WorkflowMngImpl instance = null;

    private static WorkflowSet flowCache = new WorkflowSet();

    private boolean developmentMode = false;

    private WorkflowMngImpl()
    {
        developmentMode = true;
    }

    public static synchronized WorkflowMngImpl getInstance()
    {
        if (instance == null)
        {
            instance = new WorkflowMngImpl();
        }

        return instance;
    }

    public Workflow lookupWorkflow(String flowName) throws FlowInitializationException {

        Workflow workflow = flowCache.getWorkflow(flowName);

        if (null != workflow)
            return workflow;

        workflow = WorkflowLoader.loadWorkFlowFromFile(flowName);

        if (null != workflow) {
            flowCache.addWorkflow(flowName, workflow);
        }
        return workflow;
    }

}
