package org.neuro4j.compiler;

/*
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.neuro4j.compiler.builder.WorkflowBuilder;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.loader.f4j.ConvertationException;
import org.neuro4j.workflow.loader.f4j.FlowXML;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * 
 *
 */

public  abstract class AbstractN4j2JavaMojo extends AbstractMojo {
	@Component
	private BuildContext buildContext;

	/**
	 * The default maven project object.
	 */
	@Component
	private MavenProject project;

	@Component
	private MojoExecution execution;

	@Parameter
	protected String packageName;

	@Parameter
	protected File catalog;
	
    @Parameter
    protected File generatedResourcesDirectory;
    


    
	// @Parameter
	private Set<String> includes = new HashSet<String>();




    @Parameter( defaultValue = "true" )
    protected boolean clearOutputDir;
	
	public AbstractN4j2JavaMojo() {
		super();
	}


	
	protected abstract File getOutputDirectory();
	


    
	public void execute() throws MojoExecutionException {
		getLog().info("Running ...");

		try {

			getLog().info("Generating java source from *.n4j files ...");

			 prepareDirectory( getOutputDirectory() );
			
			Set<FileHolder> files = getBindingFiles();
			for (FileHolder file : files) {
				generateJavaFile(file);
			}
			buildContext.refresh( getOutputDirectory() );
			
			addCompileSourceRoot( project );
			
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
	
    protected abstract void addCompileSourceRoot(MavenProject project2);



	protected void prepareDirectory( File dir )
            throws MojoExecutionException
        {
            // If the directory exists, whack it to start fresh
            if ( clearOutputDir && dir.exists() )
            {
                try
                {
                    FileUtils.deleteDirectory( dir );
                }
                catch ( IOException e )
                {
                    throw new MojoExecutionException( "Error cleaning directory " + dir.getAbsolutePath(), e );
                }
            }

            if ( !dir.exists() )
            {
                if ( !dir.mkdirs() )
                {
                    throw new MojoExecutionException( "Could not create directory " + dir.getAbsolutePath() );
                }
            }
        }

	protected final Set<FileHolder> getBindingFiles()
			throws ConvertationException, FlowInitializationException {

		SourceScanner scanner = getSourceInclusionScanner(".n4j");
		Set<FileHolder> staleSources = new HashSet<FileHolder>();

		List<String> sourceDirs = sourceDirs();

		for (String sourceRoot : sourceDirs) {
			File rootFile = new File(sourceRoot);
			if (!rootFile.isDirectory()) {
				continue;
			}

			staleSources.addAll(scanner.getIncludedSources(sourceRoot,
					rootFile, null));

		}

		return staleSources;
	}
	


	protected abstract List<String> sourceDirs();

	protected SourceScanner getSourceInclusionScanner(String inputFileEnding) {

		String defaultIncludePattern = "**/*"
				+ (inputFileEnding.startsWith(".") ? "" : ".")
				+ inputFileEnding;

		if (includes.isEmpty()) {
			includes.add(defaultIncludePattern);
		}
		SourceScanner scanner = new SourceScanner(includes,
				Collections.<String> emptySet());

		return scanner;
	}

	private void generateJavaFile(FileHolder sourceFileHolder)
			throws ConvertationException, FlowInitializationException {

		
		File newjavaFile = null;
		try {
			newjavaFile = getNewFile(sourceFileHolder);
		} catch (IOException e1) {
          getLog().error(e1);
          throw new FlowInitializationException("Can not create output file");
		}
		


		try {
			getLog().info("Creating file : " + newjavaFile.getPath());
			newjavaFile.createNewFile();

			InputStream templateStream = openContentStream();

			String content = readStreamToBAO(templateStream);

			Map<String, String> parameters = parseParameters(sourceFileHolder);

			Iterator<String> it = parameters.keySet().iterator();

			while (it.hasNext()) {
				String key = it.next();
				String value = parameters.get(key);
				content = content.replace(key, value);
			}

			saveToFile(content, newjavaFile);

		} catch (IOException e) {
			getLog().error(e);
		} catch (JAXBException e) {
			getLog().error(e);
		}

	}
	
	private File getNewFile(FileHolder sourceFileHolder) throws IOException
	{
		File dir = new File (getOutputDirectory(), getFlowPackageValue(sourceFileHolder).replace('.', File.separatorChar));
		
		if (!dir.exists()){
			getLog().info("Creating directory: " + dir.getAbsolutePath());
			dir.mkdirs();
			
		}
			
		File newjavaFile = new File(dir, sourceFileHolder.getFile().getName().replace(".n4j", ".java"));
		return newjavaFile;
	}

	private String readStreamToBAO(InputStream inputStream) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		try {
			while ((length = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Exception ex) {
				getLog().error(ex);
			}
		}
		return new String(baos.toByteArray());
	}

	protected InputStream openContentStream() {
		return this.getClass().getResourceAsStream(
				"template/flowTemplate.resource");
	}

	private void saveToFile(String baos, File path) {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(path);
			os.write(baos.getBytes());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.flush();
					os.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private Map<String, String> parseParameters(FileHolder workflowFileHolder)
			throws ConvertationException, FlowInitializationException,
			FileNotFoundException, JAXBException {
		Map<String, String> parameters = new HashMap<String, String>();
		long start = System.currentTimeMillis();
		getLog().debug(
				"Loading workflow : " + workflowFileHolder.getFile().getPath());
		JAXBContext ctx = JAXBContext.newInstance(FlowXML.class);

		Unmarshaller um = ctx.createUnmarshaller();
		FlowXML flowxml = (FlowXML) um.unmarshal(workflowFileHolder.getFile());
		getLog().info("Workflow loaded in ms:" + (System.currentTimeMillis() - start));

		parameters.put("{fileName}", workflowFileHolder.getFile().getName()
				.replace(".n4j", ""));
		String flowPackage = getFlowPackageValue(workflowFileHolder);
		parameters.put("{flowPackage}", flowPackage.replace(".", "/"));

		String packageFullName = getPackageFullNameValue(flowPackage);

		parameters.put("{packageFullName}", packageFullName);
		
		WorkflowBuilder
				.buildParameters(workflowFileHolder, parameters, flowxml);

		return parameters;
	}
	
	private static String getFlowPackageValue(FileHolder workflowFileHolder) {
		String sourceFolder = workflowFileHolder.getSourceRoot();
		String path = workflowFileHolder.getFile().getPath();
		//:)
		String packageStr = path.replace(sourceFolder + File.separator, "").replace(workflowFileHolder.getFile().getName(), "").replace(File.separator, ".");
		if (packageStr.endsWith("."))
		{
			packageStr = packageStr.substring(0, packageStr.length() - 1);
		}
		
		return packageStr.trim();
	}

	private static String getPackageFullNameValue(String packageStr) {
		if (packageStr.length() > 1)
		{
			return "package " + packageStr + ";";
		}
		return "";
	}

}