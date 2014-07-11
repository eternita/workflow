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

package org.neuro4j.workflow.xml;

import org.neuro4j.workflow.Workflow;

public class LoopNode extends WorkflowNode {

    private String iteratorKey = null;
    private String elementKey = null;

    public LoopNode(String name, String uuid, Workflow workflow)
    {
        super(name, uuid, workflow);
    }

    public String getIteratorKey() {
        return iteratorKey;
    }

    public void setIteratorKey(String iteratorKey) {
        this.iteratorKey = iteratorKey;
    }

    public String getElementKey() {
        return elementKey;
    }

    public void setElementKey(String elementKey) {
        this.elementKey = elementKey;
    }

}
