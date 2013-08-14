package org.neuro4j.workflow.core;


import static org.neuro4j.workflow.core.BatchQueryStorage.IN_FILENAME;

import org.neuro4j.utils.FileUtils;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;
import org.neuro4j.storage.Storage;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_FILENAME, isOptional=true, type= "java.lang.String")},
                         output={
                         	        })	
public class BatchQueryStorage extends CustomBlock {
    
    static final String IN_FILENAME = "fileName";
      
	final static String CURRENT_STORAGE = "CURRENT_STORAGE";
    
    
    @Override
    public int execute(LogicContext ctx) throws FlowExecutionException {
		
		Storage currentStorage = (Storage) ctx.get(CURRENT_STORAGE);

		String batchFileName = (String) ctx.get(IN_FILENAME);
        
		String batchString = FileUtils.getResourceAsString(batchFileName);
		
		String[] queries = batchString.split(";");
		
		for (String query : queries)
		{
			query = query.trim();
			
			if (query.length() < 5) // check for empty strings
				continue;
			
			try {
				currentStorage.query(query);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return NEXT;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
	}
	

}
