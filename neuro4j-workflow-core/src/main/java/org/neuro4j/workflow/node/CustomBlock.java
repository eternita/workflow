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

import java.util.Set;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.common.SWEUtils;
import org.neuro4j.workflow.loader.n4j.SWFConstants;
import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.xml.CustomNode;
import org.neuro4j.workflow.xml.Transition;
import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * 
 * CustomBlock provides base implementation for user's blocks.
 * Uasers should extend this class during developing own blocks. 
 * 
 */
public abstract class CustomBlock extends LogicBlock {

    private static final String NEXT_EXIT_RELATION = SWFConstants.NEXT_RELATION_NAME;
    private static final String ERROR_EXIT_RELATION = "ERROR";

    private CustomNode node = null;
    private Transition mainExit = null;
    private Transition errorExit = null;

    /**
     * Default constructor.
     */
    public CustomBlock() {
        super();
    }

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#load(org.neuro4j.workflow.xml.WorkflowNode)
     */
    public final void load(WorkflowNode workflowNode) throws FlowInitializationException
    {
        super.load(workflowNode);
        init();
        mainExit = workflowNode.getExitByName(NEXT_EXIT_RELATION);
        errorExit = workflowNode.getExitByName(ERROR_EXIT_RELATION);
        node = (CustomNode) workflowNode;
    }

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#execute(org.neuro4j.workflow.WorkflowRequest)
     */
    public final Transition execute(WorkflowRequest request)
            throws FlowExecutionException {
        FlowContext context = request.getLogicContext();
        int result = execute(context);
        if (result != ERROR)
        {
            doOutputMapping(context);
            request.setNextRelation(mainExit);
        } else {
            if (errorExit == null)
            {
                throw new FlowExecutionException("CustomBlock " + getName() + ": Error connector not defined.");
            }
            request.setNextRelation(errorExit);
        }

        return request.getNextWorkflowNode();

    }

    /**
     * 
     * @param ctx
     * @return NEXT if processor should use "NEXT" transition or ERROR if processor should use "ERROR transition".
     * @throws FlowExecutionException throws exception if processor should stop execution.  
     */
    abstract public int execute(FlowContext ctx) throws FlowExecutionException;

    /* (non-Javadoc)
     * @see org.neuro4j.workflow.node.LogicBlock#validate(org.neuro4j.workflow.FlowContext)
     */
    @Override
    public final void validate(FlowContext ctx) throws FlowExecutionException {
        super.validate(ctx);
        ParameterDefinitionList parameterDefinitionList = getClass().getAnnotation(org.neuro4j.workflow.common.ParameterDefinitionList.class);
        ParameterDefinition[] parameters = parameterDefinitionList.input();
        for (ParameterDefinition parameter : parameters)
        {
            String name = parameter.name();

            Logger.debug(this, "Processing input parameter: name - {} , type - {})", name, parameter.type());

            doInputMapping(ctx, name);

            Object obj = ctx.get(name);
            if (!parameter.isOptional())
            {
                if (obj == null)
                {
                    throw new FlowExecutionException("Parameter " + name + " is mandatory for " + getClass().getName());
                }
            }
            checkPatameterType(parameter, obj);

        }

        if (mainExit == null)
        {
            throw new FlowExecutionException("CustomBlock " + getName() + ": Connector not defined.");
        }
    }

    /**
     * Updates names of output parameters.
     * 
     * @param ctx flow context
     * @throws FlowExecutionException
     */
    private void doOutputMapping(FlowContext ctx) throws FlowExecutionException
    {
        ParameterDefinitionList parameterDefinitionList = getClass().getAnnotation(org.neuro4j.workflow.common.ParameterDefinitionList.class);
        ParameterDefinition[] parameters = parameterDefinitionList.output();
        for (ParameterDefinition parameter : parameters)
        {
            String name = parameter.name();
            String key = doOutMapping(ctx, name);
            Object obj = ctx.get(key);
            if (!parameter.isOptional())
            {
                if (obj == null)
                {
                    throw new FlowExecutionException("Parameter " + name + " is mandatory for " + getClass().getName());
                }

            }
            checkPatameterType(parameter, obj);

        }

    }


    /**
     * Updates names of input parameters.
     * 
     * @param ctx flow context
     * @param originalName name of this parameter will be updated to new.
     */
    private void doInputMapping(FlowContext ctx, String originalName)
    {
        Set<String> parameterKeys = node.getParameters().keySet();

        for (String key : parameterKeys)
        {
            String mappedValue = node.getParameter(key);
            if (mappedValue != null && mappedValue.startsWith(originalName))
            {
                String[] splittedValue = SWEUtils.getMappedParameters(mappedValue);

                Logger.debug(this, "Mapping parameter: {} to  {})", splittedValue[1], originalName);

                evaluateParameterValue(splittedValue[1], splittedValue[0], ctx);

            }
        }

    }

    /**
     * Method processes output parameters.
     * 
     * @param ctx flow context
     * @param originalName
     * @return
     */
    private String doOutMapping(FlowContext ctx, String originalName) {

        Set<String> parameterKeys = node.getOutParameters().keySet();

        for (String key : parameterKeys)
        {
            String mappedValue = node.getParameter(key);
            if (mappedValue != null && mappedValue.startsWith(originalName))
            {
                String[] splittedValue = SWEUtils.getMappedParameters(mappedValue);
                Object obj = ctx.remove(originalName);
                String newName = splittedValue[1];
                ctx.put(newName, obj);
                return newName;
            }

        }

        return originalName;

    }

    /**
     * Checks if type in parameterDefinition is the same with object's type.
     * 
     * @param parameterDefinition
     * @param obj
     * @throws LogicException
     */
    private void checkPatameterType(ParameterDefinition parameterDefinition, Object obj) throws FlowExecutionException
    {
        if (obj == null)
        {
            return;
        }
        String className = parameterDefinition.type();
        if (className == null)
        {
            if (!parameterDefinition.isOptional())
            {
                throw new FlowExecutionException("Type should be not empty for mandatory parameter" + parameterDefinition.name() + "(" + getName() + ")");
            }
            return;

        }
        if (className.equals(obj.getClass().getCanonicalName()))
        {
            return;
        }

        try {
            Class<?> cl = SWEUtils.class.getClassLoader().loadClass(className);

            if (!cl.isAssignableFrom(obj.getClass()))
            {
                StringBuffer message = new StringBuffer("Wrong parameter type for ").append(parameterDefinition.name()).append("( ").append(getName()).append(" ). Expected type: ").append(className).append(" actual type: ").append(obj.getClass().getCanonicalName());
                throw new FlowExecutionException(message.toString());
            }
        } catch (ClassNotFoundException e) {

            if (className.contains(" "))
            {
                Logger.error(this, "Class's name {} contains whitespace - please check your parameter with name: {}.", className, parameterDefinition.name());
            }

            throw new FlowExecutionException(e);
        }

    }

    /**
     * Method initializes custom block.
     * 
     * @throws FlowInitializationException
     */
    protected void init() throws FlowInitializationException {

    }

}
