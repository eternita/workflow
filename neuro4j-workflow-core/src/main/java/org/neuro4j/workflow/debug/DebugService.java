/**
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
/**
 * 
 */
package org.neuro4j.workflow.debug;

import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.node.WorkflowNode;

/**
 *
 *
 */
public class DebugService {

    private static DebugService service = new DebugService();



    private DebugService()
    {

    }

    public static DebugService getInstance() {
        return service;
    }

    public void onNodeCall(WorkflowNode node, WorkflowRequest request) {
        
    }



}
