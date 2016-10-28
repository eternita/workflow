package org.neuro4j.example.web.json;


import static org.neuro4j.example.web.json.CreateJsonFromAccounts.IN_ACCOUNTS;
import static org.neuro4j.example.web.json.CreateJsonFromAccounts.OUT_JSONSTR;

import java.util.List;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

import com.google.gson.Gson;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_ACCOUNTS, isOptional=true, type= "java.util.List")},
                         output={
                         	        @ParameterDefinition(name=OUT_JSONSTR, isOptional=true, type= "java.lang.String")})	
public class CreateJsonFromAccounts extends CustomBlock {
    
    static final String IN_ACCOUNTS = "accounts";
      
    static final String OUT_JSONSTR = "jsonStr"; 
    
    
    
    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {
		
		List accounts = (List)ctx.get(IN_ACCOUNTS);
        
		
		Gson gson = new Gson();
		
		String json = gson.toJson(accounts);
		
        if (/*error != */false)
        {
        	return ERROR;
        }
		
		ctx.put(OUT_JSONSTR, json); 
    
		
		return NEXT;
	}
	
	

}
