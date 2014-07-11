package org.neuro4j.example.web.blocks;


import static org.neuro4j.example.web.blocks.RemoveAccount.*;

import java.util.Map;

import org.neuro4j.example.web.mng.AccountMng;
import org.neuro4j.example.web.mng.impl.AccountMngImpl;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input={
		                         @ParameterDefinition(name=IN_UUID, isOptional=false, type= "java.lang.String"), @ParameterDefinition(name=IN_ACCOUNTS, isOptional=false, type= "java.util.Map")},
                         output={ })	
public class RemoveAccount extends CustomBlock {
    
    static final String IN_UUID = "uuid";
    static final String IN_ACCOUNTS = "accounts";
      
    private AccountMng accountMng = null;

	public int execute(FlowContext ctx)
			throws FlowExecutionException {
		
		String uuid = (String)ctx.get(IN_UUID);
		Map accounts = (Map) ctx.get(IN_ACCOUNTS);
		
		accountMng.removeAccount(accounts, uuid);
			
		return NEXT;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
		accountMng = AccountMngImpl.getInstance();
	}

}
