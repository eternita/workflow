package org.neuro4j.example.web.blocks;

import static org.neuro4j.example.web.blocks.GetAccountsList.IN_ACCOUNTS;
import static org.neuro4j.example.web.blocks.GetAccountsList.OUT_ACCOUNT_LIST;

import java.util.List;
import java.util.Map;

import org.neuro4j.example.web.mng.Account;
import org.neuro4j.example.web.mng.AccountMng;
import org.neuro4j.example.web.mng.impl.AccountMngImpl;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

@ParameterDefinitionList(input={@ParameterDefinition(name=IN_ACCOUNTS, isOptional=false, type= "java.util.Map")},
                         output={ 
		                         @ParameterDefinition(name=OUT_ACCOUNT_LIST, isOptional=false, type= "java.util.List")})	
public class GetAccountsList extends CustomBlock {
    
      
    static final String IN_ACCOUNTS = "accounts"; 
    static final String OUT_ACCOUNT_LIST = "accountList"; 
    
    
    private AccountMng accountMng = null;
    

	public int execute(LogicContext ctx)
			throws FlowExecutionException {
		
		Map accounts = (Map) ctx.get(IN_ACCOUNTS);
		
		List<Account> accountsList = accountMng.getAccountList(accounts);
				
		ctx.put(OUT_ACCOUNT_LIST, accountsList); 
    
		return NEXT;
	}


	@Override
	protected void init() throws FlowInitializationException{
		super.init();
		accountMng = AccountMngImpl.getInstance();
		
	}
	
	
	
	

}
