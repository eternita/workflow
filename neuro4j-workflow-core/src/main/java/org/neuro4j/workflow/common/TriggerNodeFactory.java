package org.neuro4j.workflow.common;

import java.util.Collection;

import org.neuro4j.workflow.log.Logger;
import org.neuro4j.workflow.node.CustomBlock;
import org.neuro4j.workflow.node.CustomNode;
import org.neuro4j.workflow.node.WorkflowNode;

public class TriggerNodeFactory {

    /**
     * Returns initialized trigger block.
     * @param flow - flow name with trigger node. Ex. org.neuro4j.springframework.timer.example.TimerFlow-Timer
     * @return the initialized trigger block.
     * @throws FlowInitializationException
     */
    public static TriggerBlock initTriggerNode(String flow) throws FlowInitializationException
    {
        String[] flowArray;
        try {
            flowArray = WorkflowEngine.parseFlowName(flow);
        } catch (FlowExecutionException e1) {

            throw new FlowInitializationException(e1);
        }
        String flowName = flowArray[0];
        String triggerName = flowArray[1];

        Workflow workflow = WorkflowMngImpl.getInstance().lookupWorkflow(flowName);
        if (workflow == null)
        {
            Logger.error(TriggerNodeFactory.class, "Flow: {} not found in classpath.", flow);
            throw new FlowInitializationException("Flow: " + flow + " not found in classpath.");
        }

        Collection<WorkflowNode> nodes = workflow.getNodes();
        for (WorkflowNode node : nodes)
        {
            if (triggerName.equals(node.getName()))
            {
                CustomNode customNode = (CustomNode) node;
                CustomBlock customBlock = customNode.getCustomBlock();

                if (customBlock instanceof TriggerBlock)
                {
                    TriggerBlock triggerBlock = (TriggerBlock) customBlock;
                    triggerBlock.setNode(customNode);
                    return triggerBlock;
                }

            }
        }
        
        throw new FlowInitializationException("No trigger node found in " + flow);

    }

}
