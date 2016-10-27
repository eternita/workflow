{packageFullName}


import org.neuro4j.workflow.common.FlowInitializationException;
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
