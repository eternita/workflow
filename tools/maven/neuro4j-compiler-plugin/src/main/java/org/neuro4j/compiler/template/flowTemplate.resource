{packageFullName}


import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.node.*;
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
    public {fileName}() throws FlowExecutionException {
        this("{fileName}", "{flowPackage}");
        init();
    }
    
    /**
	 * @generated
	 */
	private void init() throws FlowExecutionException {
	
		{initBlock}
		
    }
	

}
