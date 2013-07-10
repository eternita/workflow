/**
 * 
 */
package org.neuro4j.flows.nodes.loopnode;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.tests.base.BaseFlowTestCase;

/**
 *
 */
public class LoopNodeTestCase extends BaseFlowTestCase{



	@Test
	public void testArrayList() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			List<String> list = new ArrayList<String>();
			list.add("value1");
			list.add("value2");
			list.add("value3");
			
			params.put("array1", list);
			
			String lastValue = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.loopnode.LoopExample-StartNode1", params, "var1");
		
			assertSame("value3", lastValue);
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	

	@Test
	public void testArray() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			String[] list = new String[]{"value1","value2", "value3"};

			
			params.put("array1", list);
			
			String lastValue = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.loopnode.LoopExample-StartNode1", params, "var1");
		
			assertSame("value3", lastValue);
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}

}
