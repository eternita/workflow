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
package org.neuro4j.workflow.node;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;

public class ViewNode extends WorkflowNode {

    private String staticTemplateName = null;

    private String dynamicView = null;

    private String renderType = null;
    
    private String renderImpl = null;

    public ViewNode(String name, String uuid)
    {
        super(name, uuid);
    }

    public String getStaticTemplateName() {
        return staticTemplateName;
    }

    public void setStaticTemplateName(String staticTemplateName) {
        this.staticTemplateName = staticTemplateName;
    }

    public String getDynamicTemplateName() {
        return dynamicView;
    }

    public void setDynamicTemplateName(String dynamicTemplateName) {
        this.dynamicView = dynamicTemplateName;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }
    
    

    public String getRenderImpl() {
        return renderImpl;
    }

    public void setRenderImpl(String renderImpl) {
        this.renderImpl = renderImpl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.xml.WorkflowNode#execute(org.neuro4j.workflow.WorkflowRequest)
     */
    @Override
    public Transition execute(final WorkflowProcessor processor,  final WorkflowRequest request) throws FlowExecutionException
    {
        FlowContext ctx = request.getLogicContext();
        String view = (String) ctx.get(dynamicView);

        if (view == null)
        {
            view = staticTemplateName;
        }

        if (null != view)
        {
            ctx.setViewTemplate(view);

        }

        ctx.setRenderType(renderType);

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.node.LogicBlock#validate(org.neuro4j.workflow.FlowContext)
     */
    public final void validate(FlowContext fctx) throws FlowExecutionException
    {
        if (staticTemplateName == null && dynamicView == null)
        {
            throw new FlowExecutionException("SetViewTemplate has wrong configuration.");
        }

    }

}
