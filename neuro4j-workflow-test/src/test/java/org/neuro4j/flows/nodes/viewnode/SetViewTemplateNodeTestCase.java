/**
 * 
 */
package org.neuro4j.flows.nodes.viewnode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.SWFConstants;
import org.neuro4j.tests.base.BaseFlowTestCase;

/**
 *
 */
public class SetViewTemplateNodeTestCase extends BaseFlowTestCase{



	@Test
	public void testStaticViewNode() {
		try {
			LogicContext logicContext = executeFlow("org.neuro4j.flows.nodes.viewnode.SetViewTemplate-StartNode1Static");

			String templateName = (String) logicContext.get(SWFConstants.AC_VIEW_TEMPLATE);
			assertEquals("hello.jsp", templateName);
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testDynamicViewNode() {
		try {

			String templateName = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.viewnode.SetViewTemplate-StartNodeDynamic", SWFConstants.AC_VIEW_TEMPLATE);

			assertEquals("hello1.jsp", templateName);
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
	@Test
	public void testStaticDynamicViewNode() {
		try {
			String templateName = (String) executeFlowAndReturnObject("org.neuro4j.flows.nodes.viewnode.SetViewTemplate-StartNodeSD", SWFConstants.AC_VIEW_TEMPLATE);

			assertEquals("hello1.jsp", templateName);
			
		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}
}
