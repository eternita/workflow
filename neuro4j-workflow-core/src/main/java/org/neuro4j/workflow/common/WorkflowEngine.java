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

package org.neuro4j.workflow.common;

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.Neuro4jEngine.ConfigBuilder;

/**
 * 
 * Runs flows stored on file system in XML files (.n4j extension)
 * 
 */
@Deprecated
public class WorkflowEngine {

    static Neuro4jEngine engine = new Neuro4jEngine(new ConfigBuilder().withLoader(new ClasspathWorkflowLoader()));

    public static ExecutionResult run(String flow) {
        return run(flow, new HashMap<String, Object>());
    }

    public static ExecutionResult run(String flow, Map<String, Object> params) {
        return engine.execute(flow, params);
    }

    /**
     * 
     * @param flow
     *        - should be like package.name.FlowName-StartNode
     * @param params
     * @return
     * @throws SimpleWorkflowException
     *         - if can't load flow, can't find start node, etc
     */
    public static ExecutionResult run(String flow, WorkflowRequest request) {
      return engine.execute(flow, request);
    }

    

}
