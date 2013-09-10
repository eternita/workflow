package org.neuro4j.jasper.output;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

import static org.neuro4j.jasper.output.CreateFileOutputStream.*;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_FILEPATH, isOptional=false, type= "java.lang.String"),
                                	@ParameterDefinition(name=IN_CREATEFILE, isOptional=false, type= "java.lang.String")},
                                	
                         output={
                         	        @ParameterDefinition(name=OUT_OUTPUTSTREAM, isOptional=false, type= "java.io.OutputStream")})	
public class CreateFileOutputStream extends CustomBlock {
    
    static final String IN_FILEPATH = "filePath";
    static final String IN_CREATEFILE = "createFile";
      
    static final String OUT_OUTPUTSTREAM = "outputStream"; 
    
    
    @Override
    public int execute(LogicContext ctx) throws FlowExecutionException {
		
		String filePath = (String)ctx.get(IN_FILEPATH);
		String createFile = (String)ctx.get(IN_CREATEFILE);
		boolean createFileBoolean = false;
		
		if (createFile != null)
		{
			createFileBoolean = Boolean.parseBoolean(createFile);			
		}
		
		File file = new File(filePath);
		
		try {
			
			if (createFileBoolean && !file.exists())
			{
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return ERROR;
				}
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ctx.put(OUT_OUTPUTSTREAM, fileOutputStream); 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ERROR;
		}
		
		return NEXT;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
	}
	

}
