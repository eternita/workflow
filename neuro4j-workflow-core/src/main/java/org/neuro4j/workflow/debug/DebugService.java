/**
 * 
 */
package org.neuro4j.workflow.debug;

import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.node.WorkflowNode;

/**
 *
 *
 */
public class DebugService {

    private static DebugService service = new DebugService();



    private DebugService()
    {

    }

    public static DebugService getInstance() {
        return service;
    }

    public void onNodeCall(WorkflowNode node, WorkflowRequest request) {
        
    }



}
