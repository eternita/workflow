package org.neuro4j.example.web.blocks;

import static org.neuro4j.example.web.blocks.CreateAccount.*;

import java.util.Map;


import org.neuro4j.example.web.common.CreateException;
import org.neuro4j.example.web.mng.Account;
import org.neuro4j.example.web.mng.AccountMng;
import org.neuro4j.example.web.mng.impl.AccountMngImpl;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

@ParameterDefinitionList(input={
		                        @ParameterDefinition(name=IN_FIRSTNAME, isOptional=false, type= "java.lang.String"),
		                        @ParameterDefinition(name=IN_LASTNAME, isOptional=false, type= "java.lang.String"), 
		                        @ParameterDefinition(name=IN_ACCOUNTS, isOptional=false, type= "java.util.Map")},
                         output={
		                        @ParameterDefinition(name=OUT_ACCOUNT, isOptional=false, type= "org.neuro4j.example.web.mng.Account")})	
public class CreateAccount extends CustomBlock {
    
    static final String IN_FIRSTNAME = "firstName";
    static final String IN_ACCOUNTS = "accounts";
    static final String IN_LASTNAME = "lastName";
      
    static final String OUT_ACCOUNT = "account"; 
    
    
    private AccountMng accountMng = null;

	public int execute(LogicContext ctx)
			throws FlowExecutionException {
		
		String firstName = (String) ctx.get(IN_FIRSTNAME);
		String lastName = (String)  ctx.get(IN_LASTNAME);
		Map accounts = (Map) ctx.get(IN_ACCOUNTS);
		
		Account account = null;
		try {
			account = accountMng.createAccount(accounts, firstName, lastName);
		} catch (CreateException e) {
			e.printStackTrace();
			
			ctx.put("errorMessage", e.getMessage());
			return ERROR;
		}

		
		ctx.put(OUT_ACCOUNT, account); 
    
		
		return NEXT;
	}


	@Override
	protected void init() throws FlowInitializationException{
		super.init();
		accountMng = AccountMngImpl.getInstance();
	}
	
	

}
