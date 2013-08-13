package org.neuro4j.web.taglib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.neuro4j.apache.commons.beanutils.PropertyUtils;

public class IncludeTagHandler extends TagSupport {
	
	private static final String charset = "UTF-8";
	
	
	private static final long serialVersionUID = 2507581424775535445L;
	
	/**
	 * Flow name. example: Welcome-Start
	 */
	private String flow;
	
	/**
	 * Flow parameters. example parame1=12&param2=34
	 */
	private String parameters;

     
    
    public String getFlow() {
		return flow;
	}



	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getParameters()
	{			
		return parameters;
	}
	
	public void setParameters(String parameters)
	{		
		
		this.parameters = parameters;
	}

	@Override
	public int doStartTag() throws JspException {

		JspWriter out = pageContext.getOut();

		String url = getUrlWithFlow(flow);



		processIncludeRequest(url, out);

		return SKIP_BODY;
	}
	
	
	private String getUrlWithFlow(String flow)
	{
		
		HttpServletRequest  request = (HttpServletRequest)pageContext.getRequest();
		
		String originalUrl =  getRequestURL(request);
		int index = originalUrl.lastIndexOf("/");
		originalUrl = originalUrl.substring(0, index);
		
		
		StringBuffer url = new StringBuffer(originalUrl);

		url.append("/").append(flow);
		if(getParameters() != null)
		{
				url.append("?").append(getParameters());
		}
		
		return url.toString();
		
	}
	

	
	private void processIncludeRequest(String url, JspWriter out) {
		
		BufferedReader dis = null;
		try{
			
			URLConnection connection = new URL(url).openConnection();

			connection.setRequestProperty("Accept-Charset", charset);

			dis = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String inputLine;

			while ((inputLine = dis.readLine()) != null) {
				out.println(inputLine);
			}

		} catch (MalformedURLException me) {
			System.err.println("MalformedURLException: " + me);
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe);
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (IOException e) {					
				}
			}

		}

	}
	
	/**
	 * @param request
	 * @return
	 */
	public  String getRequestURL(HttpServletRequest request)
	{
		//TODO: Should be updated.
        String requestURL = null;

        try 
        {
        	Method getRequestURLMethod = null;
			// read real request url (which like in browser) using reflection
        	if (null == getRequestURLMethod)
        		getRequestURLMethod = Class.forName("org.apache.catalina.connector.RequestFacade").getMethod("getRequestURL");

        	Object internalRequest = PropertyUtils.getProperty(request, "request.request");
        	if (null != getRequestURLMethod && null != internalRequest)
        		requestURL = getRequestURLMethod.invoke(internalRequest).toString();
        	
        } catch (Exception ex) {
        	System.err.print(ex.getMessage());
        }
        
        if (null == requestURL)
        	requestURL = request.getRequestURL().toString();
        
        return requestURL;
	}
    	
    
}