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

package org.neuro4j.workflow.node;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.xml.Transition;
import org.neuro4j.workflow.xml.ViewNode;
import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * SetViewTemplate can be used in web application to set view. It can be jsp page, velocity template and etc.
 *
 */
public class SetViewTemplate extends LogicBlock {

    /**
     * 
     */
    private String staticTemplateName = null;

    /**
     * 
     */
    private String dynamicTemplateName = null;

    /**
     * 
     */
    private String renderType = null;

    /**
     *  Default constructor.
     */
    public SetViewTemplate() {
        super();
    }

    /**
     * Constructor.
     * @param name
     */
    public SetViewTemplate(String name) {
        super();
        setName(name);
    }

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#execute(org.neuro4j.workflow.WorkflowRequest)
     */
    public Transition execute(WorkflowRequest request) throws FlowExecutionException
    {
        FlowContext ctx = request.getLogicContext();
        String templateName = (String) ctx.get(dynamicTemplateName);

        if (templateName == null)
        {
            templateName = staticTemplateName;
        }

        if (null != templateName)
        {
            ctx.setViewTemplate(templateName);

        }

        ctx.setRenderType(renderType);

        return null;
    }

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#load(org.neuro4j.workflow.xml.WorkflowNode)
     */
    public final void load(WorkflowNode workflowNode) throws FlowInitializationException
    {
        ViewNode node = (ViewNode) workflowNode;
        staticTemplateName = node.getStaticTemplateName();
        dynamicTemplateName = node.getDynamicTemplateName();
        renderType = node.getRenderType();
    }

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#validate(org.neuro4j.workflow.FlowContext)
     */
    public final void validate(FlowContext fctx) throws FlowExecutionException
    {
        if (staticTemplateName == null && dynamicTemplateName == null)
        {
            throw new FlowExecutionException("SetViewTemplate has wrong configuration.");
        }

    }
}
