package org.neuro4j.workflow.common;

import java.io.IOException;
import java.net.URL;

public class ClasspathWorkflowLoader extends URLWorkflowLoader{
	
	public final static String DEFAULT_EXT = ".n4j";
	
	private final String fileExt;
	
	public ClasspathWorkflowLoader(final String ext){
		this.fileExt = ext;
	}
	public ClasspathWorkflowLoader(){
		this(DEFAULT_EXT);
	}

	@Override
	protected URL getResource(String location) throws IOException {
		  return  getClass().getClassLoader().getResource(location + fileExt);
	}
}
