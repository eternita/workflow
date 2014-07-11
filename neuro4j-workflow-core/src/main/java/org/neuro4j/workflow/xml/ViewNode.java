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

public class ViewNode extends WorkflowNode {

    private String staticTemplateName = null;

    private String dynamicTemplateName = null;

    private String renderType = null;

    public ViewNode(String name, String uuid, Workflow workflow)
    {
        super(name, uuid, workflow);
    }

    public String getStaticTemplateName() {
        return staticTemplateName;
    }

    public void setStaticTemplateName(String staticTemplateName) {
        this.staticTemplateName = staticTemplateName;
    }

    public String getDynamicTemplateName() {
        return dynamicTemplateName;
    }

    public void setDynamicTemplateName(String dynamicTemplateName) {
        this.dynamicTemplateName = dynamicTemplateName;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

}
