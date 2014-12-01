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

import java.util.HashMap;
import java.util.Map;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.loader.f4j.SWFConstants;
import org.neuro4j.workflow.log.Logger;

public class CustomNode extends WorkflowNode {

    private static final String NEXT_EXIT_RELATION = SWFConstants.NEXT_RELATION_NAME;
    private static final String ERROR_EXIT_RELATION = "ERROR";

    private final String executableClass;

    private Transition mainExit = null;
    private Transition errorExit = null;

    private final Map<String, String> outParameters;

    public CustomNode(String executableClass, String name, String uuid, Workflow workflow) {
        super(name, uuid, workflow);
        this.executableClass = executableClass;
        outParameters = new HashMap<String, String>(3);
    }

    public void addOutParameter(String key, String value)
    {
        outParameters.put(key, value);
    }

    public Map<String, String> getOutParameters() {
        return outParameters;
    }

    public final void init() throws FlowInitializationException
    {
        mainExit = getExitByName(NEXT_EXIT_RELATION);
        errorExit = getExitByName(ERROR_EXIT_RELATION);
    }

    

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.node.LogicBlock#execute(org.neuro4j.workflow.WorkflowRequest)
     */
    public final Transition execute(WorkflowRequest request)
            throws FlowExecutionException {
        FlowContext context = request.getLogicContext();
        
        CustomBlock cBlock = null;
        try {
            cBlock = getCustomBlock(this);
        } catch (FlowInitializationException e) {
            Logger.error(this, e);
            throw new FlowExecutionException(e);
        }
        
        int result = cBlock.execute(context);
        if (result != CustomBlock.ERROR)
        {
            doOutputMapping(cBlock, context);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.neuro4j.workflow.node.LogicBlock#validate(org.neuro4j.workflow.FlowContext)
     */
    @Override
    public final void validate(FlowContext ctx) throws FlowExecutionException {
        super.validate(ctx);
        
        Class<? extends CustomBlock> customClass = null;
        try {
            customClass = getCustomBlockClass(this);
        } catch (FlowInitializationException e) {
           Logger.error(this, e);
           throw new FlowExecutionException(e);
        }
        
        ParameterDefinitionList parameterDefinitionList = customClass.getAnnotation(org.neuro4j.workflow.common.ParameterDefinitionList.class);
        if (parameterDefinitionList == null)
        {
            return;
        }
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
                    throw new FlowExecutionException("Parameter " + name + " is mandatory for " + executableClass);
                }
            }
            checkPatameterType(parameter, obj);

        }

        if (mainExit == null)
        {
            throw new FlowExecutionException("CustomBlock " + executableClass + ": Connector not defined.");
        }
    }

    /**
     * Updates names of output parameters.
     * 
     * @param ctx
     *        flow context
     * @throws FlowExecutionException
     */
    private void doOutputMapping(CustomBlock cBlock, FlowContext ctx) throws FlowExecutionException
    {
        ParameterDefinitionList parameterDefinitionList = cBlock.getClass().getAnnotation(org.neuro4j.workflow.common.ParameterDefinitionList.class);
        if (parameterDefinitionList == null)
        {
            return;
        }
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
     * @param ctx
     *        flow context
     * @param originalName
     *        name of this parameter will be updated to new.
     */
    private void doInputMapping(FlowContext ctx, String originalName)
    {

            String mappedValue = getParameter(originalName);
            if (mappedValue != null && !mappedValue.equalsIgnoreCase(originalName))
            {
                Logger.debug(this, "Mapping parameter: {} to  {})", mappedValue, originalName);

                evaluateParameterValue(mappedValue, originalName, ctx);
            }

    }

    /**
     * Method processes output parameters.
     * 
     * @param ctx
     *        flow context
     * @param originalName
     * @return
     */
    private String doOutMapping(FlowContext ctx, String originalName) {

            String mappedValue = getOutParameters().get(originalName);
            if (mappedValue != null && !mappedValue.equalsIgnoreCase(originalName))
            {
                Object obj = ctx.remove(originalName);
                ctx.put(mappedValue, obj);
                return mappedValue;
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
            Class<?> cl = CustomBlock.class.getClassLoader().loadClass(className);

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
     * @return
     */
    public String getExecutableClass() {
        return executableClass;
    }
    
    
    private CustomBlock getCustomBlock(CustomNode node) throws FlowInitializationException{
        return CustomBlockLoader.getInstance().lookupBlock(node);
    }
    
    private Class<? extends CustomBlock> getCustomBlockClass(CustomNode node) throws FlowInitializationException{
        return CustomBlockLoader.getInstance().getCustomBlockClass(node);
    }

    /**
     * @return
     * @throws FlowInitializationException 
     */
    public CustomBlock getCustomBlock() throws FlowInitializationException {
        return getCustomBlock(this);
    }
}
