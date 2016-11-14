package org.neuro4j.workflow.async;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.neuro4j.workflow.common.Workflow;
import org.neuro4j.workflow.common.WorkflowConverter;
import org.neuro4j.workflow.common.XmlWorkflowConverter;
import org.neuro4j.workflow.guava.GuavaCachedWorkflowLoader;
import org.neuro4j.workflow.loader.RemoteWorkflowLoader;

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

		String name = "http://localhost:8080/app/" + "org/neuro4j/workflow/flows/FlowForClasspathLoader.n4j";
				
		RemoteWorkflowLoader remoteLoader = new RemoteWorkflowLoader(converter, new FakeWorkflowLoader());
		CounterWorkflowLoader counterLoader = new CounterWorkflowLoader(remoteLoader);
		AtomicInteger counter = counterLoader.getCounter();
		
		GuavaCachedWorkflowLoader loader = GuavaCachedWorkflowLoader.cacheWithExpiration(counterLoader, 3, TimeUnit.SECONDS);
		
		Workflow workflow = loader.load(name);
		
		assertNotNull(workflow);
		
		assertEquals(1, counter.get());
		
		// load one more time
		workflow = loader.load(name);
		
		assertNotNull(workflow);
		
		assertEquals(1, counter.get());
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			
		}

		// load one more time - should call remote loader
		workflow = loader.load(name);
		
		assertNotNull(workflow);
		
		assertEquals(2, counter.get());

	}
	

	@After
	public void shutdownServer() throws Exception {
		server.stop();
	}

}
