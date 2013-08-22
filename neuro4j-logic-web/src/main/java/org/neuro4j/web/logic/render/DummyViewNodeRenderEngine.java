package org.neuro4j.web.logic.render;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.utils.IOUtils;

/**
 * 
 * Development demo only
 *
 */
public class DummyViewNodeRenderEngine implements ViewNodeRenderEngine {

	public String getName()
	{
		return "dummy";
	}

	public void render(HttpServletResponse response, LogicContext logicContext, String viewTemplate) throws IOException
	{
		String s = "Hi Mister!";

		ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());

		// Set content size
		response.setContentLength((int) s.length());
		// Set content type
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "filename=\"hi.mister.txt\";");
		
		OutputStream out = response.getOutputStream();
		IOUtils.copyLarge(is, out);
		out.close();
		
		return;
	}


}
