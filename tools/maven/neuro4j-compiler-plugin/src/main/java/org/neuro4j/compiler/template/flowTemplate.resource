{packageFullName}


import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.node.CustomNode;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.StartNode;
import org.neuro4j.workflow.node.Transition;
import org.neuro4j.workflow.node.JoinNode;
import org.neuro4j.workflow.node.KeyMapper;
import org.neuro4j.workflow.node.LoopNode;
import org.neuro4j.workflow.node.DecisionNode;
import org.neuro4j.workflow.node.ViewNode;
import org.neuro4j.workflow.node.SwitchNode;
import org.neuro4j.workflow.common.CallNode;
import org.neuro4j.workflow.enums.*;

/**
 * This file has been generated for {flowPackage}/{fileName}.n4j file.
 * Do not edit it manually.
 * @generated
 */
public class {fileName} extends Workflow {

    /**
	 * @generated
	 */
	public {fileName}(String flowName, String flowPackage) {
        super(flowName, flowPackage);
	}
    
    /**
	 * @generated
	 */
    public {fileName}() throws FlowInitializationException {
        this("{fileName}", "{flowPackage}");
        init();
    }
    
    /**
	 * @generated
	 */
	private void init() throws FlowInitializationException {
	
		{initBlock}
		
    }
	

}
