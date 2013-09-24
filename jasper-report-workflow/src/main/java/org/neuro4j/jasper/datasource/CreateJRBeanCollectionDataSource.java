package org.neuro4j.jasper.datasource;


import java.util.Collection;
import java.util.Collections;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;
import static org.neuro4j.jasper.datasource.CreateJRBeanCollectionDataSource.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_COLLECTION, isOptional=true, type= "java.util.Collection")},
                         output={
                         	        @ParameterDefinition(name=OUT_JASPERDATASOURCE, isOptional=false, type= "net.sf.jasperreports.engine.data.JRBeanCollectionDataSource")})	
public class CreateJRBeanCollectionDataSource extends CustomBlock {
    
    static final String IN_COLLECTION = "collection";
      
    static final String OUT_JASPERDATASOURCE = "jasperDataSource"; 
    
    
    @Override
    public int execute(LogicContext ctx) throws FlowExecutionException {
		
    	Collection<?> collection = (Collection<?>)ctx.get(IN_COLLECTION);
        
    	JRBeanCollectionDataSource datasource = null;
		if (collection != null)
		{
			datasource = new JRBeanCollectionDataSource(collection);
		} else {
			datasource = new JRBeanCollectionDataSource(Collections.EMPTY_LIST);
		}
		

		
		ctx.put(OUT_JASPERDATASOURCE, datasource); 
    
		
		return NEXT;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
	}
	

}
