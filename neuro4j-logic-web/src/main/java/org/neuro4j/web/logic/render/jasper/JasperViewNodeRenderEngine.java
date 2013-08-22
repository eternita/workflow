package org.neuro4j.web.logic.render.jasper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.utils.IOUtils;
import org.neuro4j.web.logic.render.ViewNodeRenderEngine;

public class JasperViewNodeRenderEngine implements ViewNodeRenderEngine {

	public String getName()
	{
		return "jasper";
	}
	
	public void render(HttpServletResponse response, LogicContext logicContext, String viewTemplate) throws IOException
	{
/*		File f = new File("C:/data/temp/hadoop.pdf");
		FileInputStream is = null;
		try {
			is = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Set content size
		response.setContentLength((int) f.length());
		// Set content type
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "filename=\"hadoop.pdf\";");
		
		OutputStream out = response.getOutputStream();
		IOUtils.copyLarge(is, out);
		out.close();
*/		
		return;
	}


}
