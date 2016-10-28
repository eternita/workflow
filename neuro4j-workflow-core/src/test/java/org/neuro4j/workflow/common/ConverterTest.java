package org.neuro4j.workflow.common;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neuro4j.workflow.node.EndNode;
import org.neuro4j.workflow.node.KeyMapper;
import org.neuro4j.workflow.node.WorkflowNode;

public class ConverterTest {

	WorkflowConverter converter;

	@Before
	public void setUp() {
		converter = new XmlWorkflowConverter();
	}

	@After
	public void tearDown() {
		converter = null;
	}

	@Test
	public void testFileExtXmlConverter() throws FlowExecutionException {

		assertEquals(".n4j", converter.getFileExt());

	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testJsonConverter() throws FlowExecutionException {
		WorkflowConverter converter = new JSONWorkflowConverter();  
		converter.getFileExt();
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testJsonConverterFileExtension() throws FlowExecutionException {
		WorkflowConverter converter = new JSONWorkflowConverter();  
		converter.convert(null, null);
	}

	@Test
	public void testXmlConverter() throws Exception {

		String name = "org.neuro4j.workflow.flows.FlowForClasspathLoader";

		Workflow workflow = convertWorkflow(name);

		assertNotNull(workflow);
		assertEquals(name, workflow.getFlowName());
		assertEquals("org.neuro4j.workflow.flows", workflow.getPackage());
		assertTrue(workflow.isPublic());
		assertNotNull(workflow.getStartNode("Start1"));
		assertNotNull(workflow.getStartNode("StartNode2"));
		assertNull(workflow.getStartNode("Start2"));
		WorkflowNode mapNode = workflow.getById("C4QKBws3CP0AAAFXChfi4LIH");
		assertThat(mapNode, IsInstanceOf.instanceOf(KeyMapper.class));
	}

	@Test
	public void testXmlConverterWithCustomNodes() throws Exception {

		String name = "org.mydomain.FlowForFileWorkflowLoader";

		Workflow workflow = convertWorkflow(name);

		assertNotNull(workflow);
		assertEquals(name, workflow.getFlowName());
		assertEquals("org.mydomain", workflow.getPackage());
		assertTrue(workflow.isPublic());
		assertNotNull(workflow.getStartNode("StartNode1"));
		WorkflowNode mapNode = workflow.getById("aynAqAIFz9IAAAFX_.EWJ9E6");
		assertThat(mapNode, IsInstanceOf.instanceOf(EndNode.class));
	}
	
	private Workflow convertWorkflow(String name) throws Exception {

		Reader reader = getResourceReader(name);

		return converter.convert(reader, name);
	}

	private Reader getResourceReader(String name) throws UnsupportedEncodingException, IOException {
		URL resource = getClass().getClassLoader().getResource(normalize(name) + converter.getFileExt());
		return new InputStreamReader(resource.openStream(), "UTF-8");
	}

	protected String normalize(String path) {
		return path.replaceAll("\\.", File.separator);
	}
}
