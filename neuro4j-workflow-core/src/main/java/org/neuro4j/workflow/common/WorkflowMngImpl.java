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

package org.neuro4j.workflow.common;

import java.io.IOException;
import java.io.InputStream;

import org.neuro4j.workflow.WorkflowSet;
import org.neuro4j.workflow.loader.f4j.ConvertationException;
import org.neuro4j.workflow.loader.f4j.FlowConverter;
import org.neuro4j.workflow.log.Logger;

public class WorkflowMngImpl {
    
    public static final String WORKFLOW_FILE_EXT = ".n4j";

    private static WorkflowMngImpl instance = null;

    private static WorkflowSet flowCache = new WorkflowSet();

    private boolean developmentMode = false;

    private WorkflowMngImpl()
    {

    }
    
    public void setDevMode(boolean mode)
    {
        this.developmentMode = mode;
    }

    public static synchronized WorkflowMngImpl getInstance()
    {
        if (instance == null)
        {
            instance = new WorkflowMngImpl();
        }

        return instance;
    }

    Workflow lookupWorkflow(String flowName) throws FlowInitializationException {

        Workflow workflow = flowCache.getWorkflow(flowName);

        if (null != workflow)
            return workflow;

        workflow = loadWorkFlowFromFile(flowName);

        if (null != workflow && !developmentMode) {
            flowCache.addWorkflow(flowName, workflow);
        }
        return workflow;
    }
    
    
    /**
     * Loads workflow from file.
     * @param file
     * @return
     * @throws FlowInitializationException
     */
    static Workflow loadWorkFlowFromFile(String file) throws FlowInitializationException
    {
        Workflow flow = null;
        InputStream fis = WorkflowMngImpl.class.getClassLoader().getResourceAsStream(file + WORKFLOW_FILE_EXT);
        if (null != fis) {
            flow = loadFlowFromFS(fis, file);
        }

        return flow;
    }

    /**
     * Loads Workflow from input stream.
     * @param inputStream the input stream
     * @param flow flow name
     * @return Workflow object
     * @throws FlowInitializationException
     */
    static Workflow loadFlowFromFS(InputStream inputStream, String flow) throws FlowInitializationException
    {
        Workflow net = null;
        try {
            if (null != inputStream)
                try {
                    net = FlowConverter.xml2workflow(inputStream, flow);
                } catch (ConvertationException e) {
                    Logger.error(WorkflowMngImpl.class, e);
                }
        } finally {
            try {
                if (null != inputStream)
                    inputStream.close();
            } catch (IOException e) {
                Logger.error(WorkflowMngImpl.class, e);
            }
        }
        return net;
    }

}
