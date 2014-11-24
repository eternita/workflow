package org.neuro4j.springframework.jms.example;

import java.io.File;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;
import org.springframework.util.FileSystemUtils;

/**
 * Remove tmp files.
 * 
 */
@ParameterDefinitionList(input = {}, output = {})
public class RemoveJmsTmpFiles extends CustomBlock {

	public int execute(FlowContext ctx) throws FlowExecutionException {

		FileSystemUtils.deleteRecursively(new File("activemq-data"));

		return NEXT;
	}

}
