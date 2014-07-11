package org.neuro4j.example.web.blocks;

import static org.neuro4j.example.web.blocks.GetAccountsFromSession.OUT_ACCOUNTS;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.neuro4j.example.web.mng.AccountMng;
import org.neuro4j.example.web.mng.impl.AccountMngImpl;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input={},
                         output={ 
		                         @ParameterDefinition(name=OUT_ACCOUNTS, isOptional=false, type= "java.util.Map")})	
public class GetAccountsFromSession extends CustomBlock {
    
      
    static final String OUT_ACCOUNTS = "accounts"; 
    
    private AccountMng accountMng = null;
    

	public int execute(FlowContext ctx)
			throws FlowExecutionException {
		
		HttpServletRequest request = (HttpServletRequest)ctx.get("REQUEST");
		Map accountsMap = (Map)request.getSession().getAttribute("accounts");
		if (accountsMap == null)
		{
			accountsMap = accountMng.init();
			request.getSession().setAttribute("accounts", accountsMap);
		}
		

				
		ctx.put(OUT_ACCOUNTS, accountsMap); 
    
		return NEXT;
	}


	@Override
	protected void init() throws FlowInitializationException{
		super.init();
		accountMng = AccountMngImpl.getInstance();
		
	}
	
	
	
	

}
