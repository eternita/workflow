/**
 * 
 */
package org.neuro4j.flows.nodes.mappernode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.tests.base.BaseFlowTestCase;

/**
 *
 */
public class MapperNodeTestCase extends BaseFlowTestCase{



	@Test
	public void testMap2Values() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			
			params.put("var1", "123");
			params.put("var3", "Hello");
			
			LogicContext logicContext = executeFlow("org.neuro4j.flows.nodes.mappernode.MapperFlow-StartNode1", params);
			String var2 = (String)logicContext.get("var2");
			String var4 = (String)logicContext.get("var4");
			assertEquals(var2, "123");
			assertEquals(var4, "Hello");
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testMapNull() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			
			params.put("var1", "123");

			
			String var1 = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.mappernode.MapperFlow-StartNodeMapNull", params, "var1");
		
			assertEquals(var1, null);

		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testMapStringConstant() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			String var1 = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.mappernode.MapperFlow-StartNodeStringConstant", params, "var1");

			assertEquals(var1, "Hello world!");

		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	
}
