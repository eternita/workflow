/**
 * 
 */
package org.neuro4j.workflow.common;

import java.util.Map;

import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.node.CustomBlock;
import org.neuro4j.workflow.node.CustomNode;

/**
 *
 *
 */
public abstract class TriggerBlock extends CustomBlock {

    private CustomNode node;
    

    @Override
    public final int execute(FlowContext context) throws FlowExecutionException {
        return 0;
    }
    
    
    protected final ExecutionResult executeFlow(Map<String, Object> parameters)
    {
        WorkflowRequest request = new WorkflowRequest(parameters);
        return  WorkflowEngine.runFromTrigger(this, request);
    }


    public CustomNode getNode() {
        return node;
    }


    public void setNode(CustomNode node) {
        this.node = node;
    }


    @Override
    public final void init() throws FlowInitializationException {
    }
    
    



}
