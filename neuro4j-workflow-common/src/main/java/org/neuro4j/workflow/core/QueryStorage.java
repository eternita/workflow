package org.neuro4j.workflow.core;

import static org.neuro4j.workflow.core.QueryStorage.IN_QUERY;
import static org.neuro4j.workflow.core.QueryStorage.OUT_NETWORK;

import org.neuro4j.core.Network;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;
import org.neuro4j.storage.NQLException;
import org.neuro4j.storage.NeuroStorage;
import org.neuro4j.storage.StorageException;


@ParameterDefinitionList(input={
    	@ParameterDefinition(name=IN_QUERY, isOptional=false, type= "java.lang.String")},
output={
 		@ParameterDefinition(name=OUT_NETWORK, isOptional=false, type= "org.neuro4j.core.Network")})	


public class QueryStorage extends CustomBlock {

	final static String IN_QUERY = "query";
	final static String OUT_NETWORK = "network";
	
	final static String CURRENT_STORAGE = "CURRENT_STORAGE";
	
	public int execute(LogicContext ctx) throws FlowExecutionException {
		
		String queryStr = (String) ctx.get(IN_QUERY);
		NeuroStorage currentStorage = (NeuroStorage) ctx.get(CURRENT_STORAGE); 
		try {
			Network net = currentStorage.query(queryStr);
			ctx.put(OUT_NETWORK, net); 
		} catch (NQLException e) {
			ctx.put("Error", e); 
			return ERROR;
		} catch (StorageException e) {
			ctx.put("Error", e); 
			return ERROR;
		}
	
		return NEXT;
	}
	
	
	

}
