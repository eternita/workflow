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

import org.apache.commons.beanutils.ConstructorUtils;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.SWEUtils;
import org.neuro4j.workflow.loader.n4j.SWFConstants;
import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.xml.Transition;
import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * Base class for all blocks.
 */
public abstract class LogicBlock implements ActionBlock {

    private static final long serialVersionUID = 1L;

    private String name = null;
    private String uuid = null;

    protected static final int NEXT = 1;
    protected static final int ERROR = 2;

    /**
     * Default constructor.
     */
    protected LogicBlock() {
        super();
        setName(this.getClass().getSimpleName());
    }

    /**
     * @param name
     */
    protected void setName(String name)
    {
        this.name = name;
    }

    /**
     * @param request
     * @return
     * @throws FlowExecutionException
     */
    public final WorkflowNode process(WorkflowRequest request) throws FlowExecutionException {
        validate(request.getLogicContext());
        Transition transition = execute(request);

        if (transition != null)
        {
            return transition.getToNode();
        }

        return null;
    }

    /**
     * @param ctx
     * @throws FlowExecutionException
     */
    public void validate(FlowContext ctx) throws FlowExecutionException
    {
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.ActionBlock#load(org.neuro4j.workflow.xml.WorkflowNode)
     */
    public void load(WorkflowNode node) throws FlowInitializationException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.ActionBlock#execute(org.neuro4j.workflow.WorkflowRequest)
     */
    abstract public Transition execute(WorkflowRequest request) throws FlowExecutionException;

    /**
     * Process parameters from flow context.
     * Ex. if user defines account.name - processor will call getter method for name property.
     * 
     * @param source
     *        name of original object
     * @param target
     *        target name.
     * @param ctx
     *        flow context.
     */
    protected final void evaluateParameterValue(String source, String target, FlowContext ctx)
    {
        Object obj = null;

        // 1) if null
        if (SWFConstants.NULL_VALUE.equalsIgnoreCase(source))
        {
            ctx.put(target, null);
            return;

            // 2) if create new class expression
        } else if (source.startsWith(SWFConstants.NEW_CLASS_SYMBOL_START) && source.endsWith(SWFConstants.NEW_CLASS_SYMBOL_END)) {

            source = source.replace(SWFConstants.QUOTES_SYMBOL, "").replace("(", "").replace(")", "");

            obj = createNewInstance(source);

            ctx.put(target, obj);
            return;
        }

        String[] parts = source.split("\\+");

        // if concatenated string
        if (parts.length > 1)
        {
            String stringValue = "";

            for (String src : parts)
            {
                stringValue += (String) ctx.get(src);
            }
            obj = stringValue;

        } else {
            obj = ctx.get(source);
        }

        ctx.put(target, obj);

    }

    /**
     * Creates new instance of parameter
     * 
     * @param clazzName
     *        class name
     * @return created object.
     */
    private static Object createNewInstance(String clazzName) {
        Class<?> beanClass = null;
        Object beanInstance = null;
        try {
            beanClass = Class.forName(clazzName);
            beanInstance = ConstructorUtils.invokeConstructor(beanClass, null);
        } catch (Exception e) {
            Logger.error(SWEUtils.class, e.getMessage(), e);
        }

        return beanInstance;

    }

    /**
     * Returns name of the block
     * 
     * @return name
     */
    protected String getName() {
        return name;
    }

    /**
     * Returns uuid.
     * 
     * @return uuid.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Set uuid.
     * 
     * @param uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
