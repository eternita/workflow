package org.springframework.samples.mvc.form;


import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import static org.springframework.samples.mvc.form.ProcessForm.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_FORMBEAN, isOptional=true, type= "org.springframework.samples.mvc.form.FormBean")},
                         output={
                         	        @ParameterDefinition(name=OUT_MESSAGE, isOptional=true, type= "java.lang.String")})	
public class ProcessForm extends CustomBlock {
    
    static final String IN_FORMBEAN = "formBean";
      
    static final String OUT_MESSAGE = "message"; 
    
    
    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
    	FormBean formBean = (FormBean) ctx.get(IN_FORMBEAN);
        
		
		String message = "Hello: " + formBean.toString();
		
		
		ctx.put(OUT_MESSAGE, message); 
    
		
		return NEXT;
	}
	
	@Override
	public void init() throws FlowInitializationException{
		super.init();
	}
	

}
