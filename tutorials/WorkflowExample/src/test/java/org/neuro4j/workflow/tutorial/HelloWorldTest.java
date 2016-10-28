package org.neuro4j.workflow.tutorial;

import static org.junit.Assert.*;
import org.junit.Test;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;

public class HelloWorldTest {
	
	@Test
	public void testHelloWorld(){
		
		HelloWorld helloWorld = new HelloWorld();
		
		FlowContext context = new FlowContext();
		context.put("name", "John");
		
		try {
			int result = helloWorld.execute(context);
			context.get("message");
			assertEquals(ActionBlock.NEXT, result);
			
			assertEquals("Hello World! John", context.get("message"));
			
		} catch (FlowExecutionException e) {
			e.printStackTrace();
		}
		
	}

}
