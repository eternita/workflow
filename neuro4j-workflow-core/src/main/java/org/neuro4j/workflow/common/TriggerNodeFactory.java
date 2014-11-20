package org.neuro4j.workflow.common;

import java.util.Collection;

import org.neuro4j.workflow.node.CustomBlock;
import org.neuro4j.workflow.node.CustomNode;
import org.neuro4j.workflow.node.WorkflowNode;

public class TriggerNodeFactory {

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
