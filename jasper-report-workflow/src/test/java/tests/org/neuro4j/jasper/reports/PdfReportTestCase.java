/**
 * 
 */
package tests.org.neuro4j.jasper.reports;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.SimpleWorkflowEngine;

/**
 *
 */
public class PdfReportTestCase extends TestCase{



	@Test
	public void testPdfCreation() {
		try {
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			LogicContext logicContext = SimpleWorkflowEngine.run("tests.org.neuro4j.jasper.reports.GenerateReport-Start", parameters);
		
            System.out.println();
		

		} catch (FlowExecutionException e) {
			fail(e.toString());
		}
	}

	
}
