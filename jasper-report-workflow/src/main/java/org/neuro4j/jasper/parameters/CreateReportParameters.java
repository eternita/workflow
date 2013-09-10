package org.neuro4j.jasper.parameters;


import java.util.HashMap;
import java.util.Map;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

import static org.neuro4j.jasper.parameters.CreateReportParameters.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_INPUTMAP, isOptional=true, type= "java.util.Map")},
                         output={
                         	        @ParameterDefinition(name=OUT_PARAMETERS, isOptional=false, type= "java.util.Map")})	
public class CreateReportParameters extends CustomBlock {
    
    static final String IN_INPUTMAP = "inputMap";
      
    static final String OUT_PARAMETERS = "parameters"; 
    
    
    @Override
    public int execute(LogicContext ctx) throws FlowExecutionException {
		
		Map inputMap = (Map)ctx.get(IN_INPUTMAP);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
        
		if (inputMap != null)
		{
			parameters.putAll(inputMap);
		}
		
		
		ctx.put(OUT_PARAMETERS, parameters); 
    
		
		return NEXT;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
	}
	

}
