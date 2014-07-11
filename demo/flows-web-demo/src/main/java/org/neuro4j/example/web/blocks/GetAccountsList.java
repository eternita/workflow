package org.neuro4j.example.web.blocks;

import static org.neuro4j.example.web.blocks.GetAccountsList.IN_ACCOUNTS;
import static org.neuro4j.example.web.blocks.GetAccountsList.OUT_ACCOUNT_LIST;

import java.util.List;
import java.util.Map;

import org.neuro4j.example.web.mng.Account;
import org.neuro4j.example.web.mng.AccountMng;
import org.neuro4j.example.web.mng.impl.AccountMngImpl;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input={@ParameterDefinition(name=IN_ACCOUNTS, isOptional=false, type= "java.util.Map")},
                         output={ 
		                         @ParameterDefinition(name=OUT_ACCOUNT_LIST, isOptional=false, type= "java.util.List")})	
public class GetAccountsList extends CustomBlock {
    
      
    static final String IN_ACCOUNTS = "accounts"; 
    static final String OUT_ACCOUNT_LIST = "accountList"; 
    
    
    private AccountMng accountMng = null;
    

	public int execute(FlowContext ctx)
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
