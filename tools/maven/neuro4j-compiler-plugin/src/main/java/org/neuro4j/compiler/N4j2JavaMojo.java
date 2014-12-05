package org.neuro4j.compiler;

/*
 * Copyright (c) 2013-2014, Neuro4j
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

import java.io.File;
import java.util.List;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * 
 *
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PROCESS_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class N4j2JavaMojo extends AbstractN4j2JavaMojo {
	

	@Parameter(defaultValue = "${project.compileSourceRoots}", readonly = true, required = true)
	private List<String> compileSourceRoots;

	
    @Parameter( defaultValue = "${project.build.directory}/generated-sources/n4j", required = true )
    private File outputDirectory;
   
	protected List<String> sourceDirs()
	{
		return getCompileSourceRoots();

	}
	
	protected File getOutputDirectory(){
		return outputDirectory;
	}


	protected List<String> getCompileSourceRoots() {
		return compileSourceRoots;
	}
	
    protected void addCompileSourceRoot( MavenProject project )
    {
        project.addCompileSourceRoot( getOutputDirectory().getAbsolutePath() );
    }

}