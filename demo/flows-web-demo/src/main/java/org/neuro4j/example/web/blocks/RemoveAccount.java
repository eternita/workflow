package org.neuro4j.example.web.blocks;

import static org.neuro4j.example.web.blocks.RemoveAccount.IN_UUID;

import org.neuro4j.example.web.mng.AccountMng;
import org.neuro4j.example.web.mng.impl.AccountMngImpl;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

@ParameterDefinitionList(input={
		                         @ParameterDefinition(name=IN_UUID, isOptional=false, type= "java.lang.String")},
                         output={ })	
public class RemoveAccount extends CustomBlock {
    
    static final String IN_UUID = "uuid";
      
    private AccountMng accountMng = null;

	public int execute(LogicContext ctx)
			throws FlowExecutionException {
		
		String uuid = (String)ctx.get(IN_UUID);
        
		accountMng.removeAccount(uuid);
			
		return NEXT;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
		accountMng = AccountMngImpl.getInstance();
	}

}
