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

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.xml.Transition;
import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * Base interface for executable blocks
 *
 */
public interface ActionBlock {

    /**
     * @param entity
     * @throws FlowInitializationException
     */
    void load(WorkflowNode entity) throws FlowInitializationException;

    /**
     * @param request
     * @return
     * @throws FlowExecutionException
     */
    public Transition execute(WorkflowRequest request) throws FlowExecutionException;

}
