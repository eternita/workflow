package org.neuro4j.workflow.common;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileWorkflowLoader extends URLWorkflowLoader{
	
	final URLWorkflowLoader delegate;
	final String baseDir;

	public FileWorkflowLoader(URLWorkflowLoader loader, String baseDir){
		this.delegate = loader;
		this.baseDir = baseDir;
	}
	
	public FileWorkflowLoader(final String baseDir,final String ext){
		this(new ClasspathWorkflowLoader(ext), baseDir);
	}
	
	@Override
	protected URL getResource(String location) throws IOException {
	    File file = new File(location);
	    return file.exists() ? file.toURI().toURL() : delegate.getResource(location);
	}

}
