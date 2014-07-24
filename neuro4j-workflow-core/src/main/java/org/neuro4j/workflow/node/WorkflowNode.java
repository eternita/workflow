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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConstructorUtils;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.Workflow;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.loader.f4j.SWFConstants;
import org.neuro4j.workflow.log.Logger;

public class WorkflowNode {

    protected Workflow workflow = null;
    private Map<String, String> parameters = null;
    private String uuid = null;
    private String name;

    Map<String, Transition> exits = null;

    public WorkflowNode() {
        exits = new HashMap<String, Transition>(3);
        parameters = new HashMap<String, String>(4);
    }

    public WorkflowNode(String name, String uuid, Workflow workflow) {
        this();
        setUuid(uuid);
        setName(name);
        this.workflow = workflow;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void addParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    public String getParameter(String key) {

        return this.parameters.get(key);
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public Transition getExitByName(String relationName) {
        return exits.get(relationName);
    }

    private void setUuid(String uuid2) {
        this.uuid = uuid2;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public void registerExit(Transition con) {
        con.setFromNode(this);
        exits.put(con.getName(), con);

    }

    public Collection<Transition> getExits() {
        return exits.values();
    }

    public void registerNodeInWorkflow() {
        workflow.registerNode(this);
    }

    /**
     * @param ctx
     * @throws FlowExecutionException
     */
    public void validate(FlowContext ctx) throws FlowExecutionException
    {
        return;
    }

    /**
     * 
     * @param request
     * @return
     * @throws FlowExecutionException
     */
    public final WorkflowNode executeNode(WorkflowRequest request) throws FlowExecutionException {
        long startTime = System.currentTimeMillis();
        Logger.debug(this, "      Running: {} ({})", this.getName(), this.getClass().getCanonicalName());

        validate(request.getLogicContext());
        Transition transition = execute(request);

        Logger.debug(this, "      Finished: {} in ({} ms.)", this.getName(), (System.currentTimeMillis() - startTime));
        if (transition != null)
        {
            return transition.getToNode();
        }

        return null;
    }

    protected Transition execute(WorkflowRequest request) throws FlowExecutionException
    {
        return null;
    }

    public void init() throws FlowInitializationException
    {

    }

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

    private static Object createNewInstance(String clazzName) {
        Class<?> beanClass = null;
        Object beanInstance = null;
        try {
            beanClass = Class.forName(clazzName);
            beanInstance = ConstructorUtils.invokeConstructor(beanClass, null);
        } catch (Exception e) {
            Logger.error(WorkflowNode.class, e.getMessage(), e);
        }

        return beanInstance;

    }

}
