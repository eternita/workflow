package org.neuro4j.flows.custom.blocks;


import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import static org.neuro4j.flows.custom.blocks.CustomWithInOutParam.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_IN1, isOptional=true, type= "java.lang.String")},
                         output={
                         	        @ParameterDefinition(name=OUT_OUT1, isOptional=true, type= "java.lang.String")})	
public class CustomWithInOutParam extends CustomBlock {
    
    static final String IN_IN1 = "in1";
      
    static final String OUT_OUT1 = "out1"; 
    
    
    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
		Object in1 = ctx.get(IN_IN1);
        
		
		ctx.put(OUT_OUT1, in1); 
    
		
		return NEXT;
	}
	
	

}
