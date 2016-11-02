package org.neuro4j.workflow.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.neuro4j.workflow.ExecutionResult;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.neuro4j.workflow.loader.ClasspathWorkflowLoader;
import org.neuro4j.workflow.loader.RemoteWorkflowLoader;
import org.neuro4j.workflow.loader.WorkflowLoader;

public class WorkflowLoaderIntTests {
	WorkflowConverter converter;

	private Server server;

	@Before
	public void startServer() throws Exception {
		server = new Server(8080);
		server.setStopAtShutdown(true);
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/app");
		webAppContext.setResourceBase("src/test/resources");
		webAppContext.setClassLoader(getClass().getClassLoader());

		server.addHandler(webAppContext);
		server.start();
		converter = new XmlWorkflowConverter();
	}

	@Test
	public void loadWorkflowOverHttp() throws Exception {

		String name = "org/neuro4j/workflow/flows/FlowForClasspathLoader.n4j";

		HttpClient client = new DefaultHttpClient();
		HttpGet mockRequest = new HttpGet("http://localhost:8080/app/" + name);

		HttpResponse mockResponse = client.execute(mockRequest);
		BufferedReader rd = new BufferedReader(new InputStreamReader(mockResponse.getEntity().getContent()));
		assertNotNull(rd);
		Workflow workflow = converter.convert(rd, name);
		assertNotNull(workflow);
	}

	@Test
	public void remoteLoaderTest() throws Exception {

		String name = "http://localhost:8080/app/" + "org/neuro4j/workflow/flows/FlowForClasspathLoader.n4j";

		RemoteWorkflowLoader subject = new RemoteWorkflowLoader(converter, new FakeLoader());

		Workflow workflow = subject.load(name);
		assertNotNull(workflow);
	}

	@Test
	public void remoteLoaderGetMessageTest() throws Exception {

		WorkflowEngine engine = new WorkflowEngine(
				new ConfigBuilder().withLoader(new RemoteWorkflowLoader(converter, new FakeLoader())));

		Map<String, Object> parameters = new HashMap<String, Object>();

		WorkflowRequest request = new WorkflowRequest(parameters);
		ExecutionResult result = engine
				.execute("http://localhost:8080/app/org/neuro4j/workflow/flows/RemoteFlow.n4j-StartNode1", request);
		String message = (String) result.getFlowContext().get("message");
		assertEquals("Hello from remote host", message);
	}

	@Test
	public void localFlowCallsRemoteTest() throws Exception {

		WorkflowEngine engine = new WorkflowEngine(new ConfigBuilder()
				.withLoader(new RemoteWorkflowLoader(converter, new ClasspathWorkflowLoader(converter))));

		Map<String, Object> parameters = new HashMap<String, Object>();

		WorkflowRequest request = new WorkflowRequest(parameters);
		ExecutionResult result = engine.execute("org.neuro4j.workflow.flows.LocalFlowCallsRemote-StartNode1", request);
		String message = (String) result.getFlowContext().get("message");
		assertEquals("Hello from remote host", message);
	}

	@After
	public void shutdownServer() throws Exception {
		server.stop();
	}

	public static class FakeLoader implements WorkflowLoader {

		@Override
		public Workflow load(String name) throws FlowExecutionException {
			fail("should not be here");
			throw new FlowExecutionException("It should not be there!");
		}

	}

}
