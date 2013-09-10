package org.neuro4j.jasper.input;


import java.io.InputStream;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;
import static org.neuro4j.jasper.input.LoadTemplate.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_TEMPLATEPATH, isOptional=false, type= "java.lang.String")},
                         output={
                         	        @ParameterDefinition(name=OUT_INPUTSTREAM, isOptional=true, type= "java.io.InputStream")})	
public class LoadTemplate extends CustomBlock {
    
    static final String IN_TEMPLATEPATH = "templatePath";
      
    static final String OUT_INPUTSTREAM = "inputStream"; 
    
    
    @Override
    public int execute(LogicContext ctx) throws FlowExecutionException {
		// reports/report1.jasper
		String templatePath = (String)ctx.get(IN_TEMPLATEPATH);
        
		InputStream inputStream = LoadTemplate.class.getClassLoader().getResourceAsStream(templatePath);
		
		
		//TODO: put your code here
		
        if (inputStream == null)
        {
        	return ERROR;
        }
		
		ctx.put(OUT_INPUTSTREAM, inputStream); 
    
		
		return NEXT;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
	}
	

}
