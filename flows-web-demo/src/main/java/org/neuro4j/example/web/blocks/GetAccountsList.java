package org.neuro4j.example.web.blocks;

import static org.neuro4j.example.web.blocks.GetAccountsList.OUT_ACCOUNTS;

import java.util.List;

import org.neuro4j.example.web.mng.Account;
import org.neuro4j.example.web.mng.AccountMng;
import org.neuro4j.example.web.mng.impl.AccountMngImpl;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

@ParameterDefinitionList(input={},
                         output={ 
		                         @ParameterDefinition(name=OUT_ACCOUNTS, isOptional=false, type= "java.util.List")})	
public class GetAccountsList extends CustomBlock {
    
      
    static final String OUT_ACCOUNTS = "accounts"; 
    
    private AccountMng accountMng = null;
    

	public int execute(LogicContext ctx)
			throws FlowExecutionException {
		
		List<Account> accounts = accountMng.getAccountList();
				
		ctx.put(OUT_ACCOUNTS, accounts); 
    
		return NEXT;
	}


	@Override
	protected void init() throws FlowInitializationException{
		super.init();
		accountMng = AccountMngImpl.getInstance();
		
	}
	
	
	
	

}
