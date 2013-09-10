package org.neuro4j.web.render.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.generic.log.LogSystemCommonsLog;
import org.apache.velocity.tools.view.ServletUtils;
import org.apache.velocity.tools.view.ToolboxManager;
import org.apache.velocity.tools.view.context.ChainedContext;
import org.apache.velocity.tools.view.servlet.ServletToolboxManager;
import org.apache.velocity.util.SimplePool;
import org.neuro4j.logic.LogicContext;
import org.neuro4j.web.logic.render.ViewNodeRenderEngine;
import org.neuro4j.web.logic.render.ViewNodeRenderExecutionException;

public class VelocityViewNodeRenderEngine implements ViewNodeRenderEngine {

	private static SimplePool writerPool = new SimplePool(40);
	protected ToolboxManager toolboxManager = null;
	public static final String DEFAULT_OUTPUT_ENCODING = "ISO-8859-1";
	private VelocityEngine velocity = null;
	private String defaultContentType = null;
	protected static final String DEFAULT_TOOLBOX_PATH = "/WEB-INF/toolbox.xml";
	private boolean warnOfOutputStreamDeprecation = true;
	public static final String CONTENT_TYPE = "default.contentType";
	public static final String DEFAULT_CONTENT_TYPE = "text/html";
	protected static final String TOOLBOX_KEY = "org.apache.velocity.toolbox";
	public static final String DEFAULT_TOOLS_PROPERTIES = "/org/apache/velocity/tools/view/servlet/velocity.properties";
	protected static final String DEFAULT_PROPERTIES_PATH = "/WEB-INF/velocity.properties";
	protected static final String INIT_PROPS_KEY = "org.apache.velocity.properties";
	protected static final String DEFAULT_TEMPLATE_PATH = "/WEB-INF/velotemplates";
	protected static final String DEFAULT_TEMPLATE_PATH_KEY = "webapp.resource.loader.path";
	protected String TEMPLATE_PATH = DEFAULT_TEMPLATE_PATH;

	public static final String SERVLET_CONTEXT_KEY = ServletContext.class
			.getName();

	@Override
	public void init(ServletConfig config, ServletContext servletContext)
			throws ViewNodeRenderExecutionException {
		// do whatever we have to do to init Velocity

		initVelocity(config, servletContext);

		// init this servlet's toolbox (if any)
		initToolbox(config, servletContext);

		loadRelativePath(config);
		
		// we can get these now that velocity is initialized
		defaultContentType = (String) getVelocityProperty(CONTENT_TYPE, 	DEFAULT_CONTENT_TYPE);

		String encoding = (String) getVelocityProperty(RuntimeConstants.OUTPUT_ENCODING, DEFAULT_OUTPUT_ENCODING);

		// For non Latin-1 encodings, ensure that the charset is
		// included in the Content-Type header.
		if (!DEFAULT_OUTPUT_ENCODING.equalsIgnoreCase(encoding)) {
			int index = defaultContentType.lastIndexOf("charset");
			if (index < 0) {
				// the charset specifier is not yet present in header.
				// append character encoding to default content-type
				defaultContentType += "; charset=" + encoding;
			} else {
				// The user may have configuration issues.
				velocity.warn("VelocityViewNodeRenderEngine: Charset was already "
						+ "specified in the Content-Type property.  "
						+ "Output encoding property will be ignored.");
			}
		}

		velocity.info("VelocityViewNodeRenderEngine: Default content-type is: "
				+ defaultContentType);
	}
	
	
	private void loadRelativePath(ServletConfig config)
	{
		String path = findInitParameter(config, DEFAULT_TEMPLATE_PATH_KEY);
		if (path != null)
		{
			TEMPLATE_PATH = path;
		}

	}

	private String updateTemplatePath(String templatePath)
	{
		return templatePath.replaceFirst(TEMPLATE_PATH + "/", "");
	}
	
	
	private void mergeContext(Context context, LogicContext logicContext)
	{
		Set<String> keys = logicContext.keySet();
		for(String key: keys)
		{
			context.put(key, logicContext.get(key));
		}
		
	}
	
	@Override
	public void render(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext,
			LogicContext logicContext, String viewTemplate)
			throws ViewNodeRenderExecutionException {
		Context context = null;
		try {
			// first, get a context
			context = createContext(request, response, servletContext);

			viewTemplate =  updateTemplatePath(viewTemplate);
			
			// set the content type
			setContentType(response);
			
			mergeContext(context, logicContext);
			
			// get the template
			Template template = getTemplate(viewTemplate, null);

			// bail if we can't find the template
			if (template == null) {
				velocity.warn("VelocityViewServlet: couldn't find template to match request.");
				return;
			}

			// merge the template and context
			mergeTemplate(template, context, response);
		} catch (Exception e) {
			// log the exception
			velocity.error("VelocityViewServlet: Exception processing the template: "
					+ e);

			// call the error handler to let the derived class
			// do something useful with this failure.
			error(request, response, e);
		} finally {
			// call cleanup routine to let a derived class do some cleanup
			requestCleanup(request, response, context);
		}

		return;
	}

	private void requestCleanup(HttpServletRequest request,
			HttpServletResponse response, Context context) {

	}

	private ExtendedProperties loadDefaultProperties(ServletContext servletContext) {
		InputStream inputStream = null;
		ExtendedProperties defaultProperties = new ExtendedProperties();

		try {
			inputStream = getClass().getResourceAsStream(
					DEFAULT_TOOLS_PROPERTIES);
			if (inputStream != null) {
				defaultProperties.load(inputStream);
			}
		} catch (IOException ioe) {
			log(servletContext, "Cannot load default extendedProperties!", ioe);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ioe) {
				log(servletContext, "Cannot close default extendedProperties!", ioe);
			}
		}
		return defaultProperties;
	}

	protected void initVelocity(ServletConfig config,
			ServletContext servletContext)
			throws ViewNodeRenderExecutionException {
		velocity = new VelocityEngine();
		setVelocityEngine(velocity);

		// register this engine to be the default handler of log messages
		// if the user points commons-logging to the LogSystemCommonsLog
		LogSystemCommonsLog.setVelocityEngine(velocity);

		velocity.setApplicationAttribute(SERVLET_CONTEXT_KEY, servletContext);

		// Try reading the VelocityTools default configuration
		try {
			ExtendedProperties defaultProperties = loadDefaultProperties(servletContext);
			velocity.setExtendedProperties(defaultProperties);
		} catch (Exception e) {
			log(servletContext, "VelocityViewServlet: Unable to read Velocity Servlet configuration file: ", e);

			// This is a fatal error...
			throw new ViewNodeRenderExecutionException(e.getMessage());
		}

		// Try reading an overriding user Velocity configuration
		try {
			ExtendedProperties p = loadConfiguration(config, servletContext);
			velocity.setExtendedProperties(p);
		} catch (Exception e) {
			log(servletContext, "VelocityViewServlet: Unable to read Velocity configuration file: ", e);
			log(servletContext, "VelocityViewServlet: Using default Velocity configuration.", null);
		}

		// now all is ready - init Velocity
		try {
			velocity.init();
		} catch (Exception e) {
			log(servletContext, "VelocityViewServlet: PANIC! unable to init()", e);
			throw new ViewNodeRenderExecutionException(e.getMessage());
		}
	}

	public void log(ServletContext servletContext, String msg, Exception e) {
		servletContext.log("VelocityViewNodeRenderEngine: "+ msg);

	}

	protected void error(HttpServletRequest request,
			HttpServletResponse response, Exception e)
			throws ViewNodeRenderExecutionException {
		try {
			StringBuffer html = new StringBuffer();
			html.append("<html>\n");
			html.append("<head><title>Error</title></head>\n");
			html.append("<body>\n");
			html.append("<h2>VelocityViewServlet : Error processing a template for path '");
			html.append(ServletUtils.getPath(request));
			html.append("'</h2>\n");

			Throwable cause = e;

			String why = cause.getMessage();
			if (why != null && why.trim().length() > 0) {
				html.append(StringEscapeUtils.escapeHtml(why));
				html.append("\n<br>\n");
			}

			// if it's an MIE, i want the real stack trace!
			if (cause instanceof MethodInvocationException) {
				// get the real cause
				cause = ((MethodInvocationException) cause)
						.getWrappedThrowable();
			}

			StringWriter sw = new StringWriter();
			cause.printStackTrace(new PrintWriter(sw));

			html.append("<pre>\n");
			html.append(StringEscapeUtils.escapeHtml(sw.toString()));
			html.append("</pre>\n");
			html.append("</body>\n");
			html.append("</html>");
			getResponseWriter(response).write(html.toString());
		} catch (Exception e2) {
			// clearly something is quite wrong.
			// let's log the new exception then give up and
			// throw a servlet exception that wraps the first one
			velocity.error("VelocityView: Exception while printing error screen: "
					+ e2);
			throw new ViewNodeRenderExecutionException(e.getMessage());
		}
	}

	private void setContentType(HttpServletResponse response) {
	}

	protected void initToolbox(ServletConfig config, ServletContext servletContext)
			throws ViewNodeRenderExecutionException {
		/* check the servlet config and context for a toolbox param */
		String file = findInitParameter(config, TOOLBOX_KEY);
		if (file == null) {
			// ok, look in the default location
			file = DEFAULT_TOOLBOX_PATH;
			velocity.debug("VelocityViewServlet: No toolbox entry in configuration. Looking for '" + DEFAULT_TOOLBOX_PATH + "'");
		}

		/* try to get a manager for this toolbox file */
		toolboxManager = ServletToolboxManager.getInstance(servletContext, file);
	}

	protected String findInitParameter(ServletConfig config, String key) {
		// check the servlet config
		String param = config.getInitParameter(key);

		if (param == null || param.length() == 0) {
			// check the servlet context
			ServletContext servletContext = config.getServletContext();
			param = servletContext.getInitParameter(key);
		}
		return param;
	}

	protected String getVelocityProperty(String key, String alternate) {
		String prop = (String) velocity.getProperty(key);
		if (prop == null || prop.length() == 0) {
			return alternate;
		}
		return prop;
	}

	public Template getTemplate(String name, String encoding)
			throws ResourceNotFoundException, ParseErrorException, Exception {
		if (encoding == null) {
			return getVelocityEngine().getTemplate(name);
		} else {
			return getVelocityEngine().getTemplate(name, encoding);
		}
	}

	protected VelocityEngine getVelocityEngine() {
		return velocity;
	}

	/**
	 * Sets the underlying VelocityEngine
	 */
	protected void setVelocityEngine(VelocityEngine ve) {
		if (ve == null) {
			throw new NullPointerException(
					"Cannot set the VelocityEngine to null");
		}
		this.velocity = ve;
	}

	protected Context createContext(HttpServletRequest request,	HttpServletResponse response, ServletContext servletContext) {
		
		ChainedContext ctx = new ChainedContext(velocity, request, response, servletContext);

		/* if we have a toolbox manager, get a toolbox from it */
		if (toolboxManager != null) {
			ctx.setToolbox(toolboxManager.getToolbox(ctx));
		}
		return ctx;
	}

	protected void mergeTemplate(Template template, Context context, HttpServletResponse response) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException,
			UnsupportedEncodingException, Exception {
		VelocityWriter vw = null;
		Writer writer = getResponseWriter(response);
		try {
			vw = (VelocityWriter) writerPool.get();
			if (vw == null) {
				vw = new VelocityWriter(writer, 4 * 1024, true);
			} else {
				vw.recycle(writer);
			}
			performMerge(template, context, vw);
		} finally {
			if (vw != null) {
				try {
					// flush and put back into the pool
					// don't close to allow us to play
					// nicely with others.
					vw.flush();
					/*
					 * This hack sets the VelocityWriter's internal ref to the
					 * PrintWriter to null to keep memory free while the writer
					 * is pooled. See bug report #18951
					 */
					vw.recycle(null);
					writerPool.put(vw);
				} catch (Exception e) {
					velocity.debug("VelocityViewServlet: "
							+ "Trouble releasing VelocityWriter: "
							+ e.getMessage());
				}
			}
		}
	}

	protected void performMerge(Template template, Context context,	Writer writer) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, Exception {
		template.merge(context, writer);
	}

	protected Writer getResponseWriter(HttpServletResponse response) throws UnsupportedEncodingException, IOException {
		Writer writer = null;
		try {
			writer = response.getWriter();
		} catch (IllegalStateException e) {
			// ASSUMPTION: We already called getOutputStream(), so
			// calls to getWriter() fail. Use of OutputStreamWriter
			// assures our desired character set
			if (this.warnOfOutputStreamDeprecation) {
				this.warnOfOutputStreamDeprecation = false;
				velocity.warn("VelocityViewServlet: "
						+ "Use of ServletResponse's getOutputStream() "
						+ "method with VelocityViewServlet is "
						+ "deprecated -- support will be removed in "
						+ "an upcoming release");
			}
			// Assume the encoding has been set via setContentType().
			String encoding = response.getCharacterEncoding();
			if (encoding == null) {
				encoding = DEFAULT_OUTPUT_ENCODING;
			}
			writer = new OutputStreamWriter(response.getOutputStream(),
					encoding);
		}
		return writer;
	}

	protected ExtendedProperties loadConfiguration(ServletConfig config, ServletContext servletContext) throws IOException {
		// grab the path to the custom props file (if any)
		String propsFile = findInitParameter(config, INIT_PROPS_KEY);
		if (propsFile == null) {
			// ok, look in the default location for custom props
			propsFile = DEFAULT_PROPERTIES_PATH;
			velocity.debug("VelocityViewServlet: Looking for custom properties at '" + DEFAULT_PROPERTIES_PATH + "'");
		}

		ExtendedProperties p = new ExtendedProperties();
		InputStream is = servletContext.getResourceAsStream(propsFile);
		if (is != null) {
			// load the properties from the input stream
			p.load(is);
			velocity.info("VelocityViewServlet: Using custom properties at '" + propsFile + "'");
		} else {
			velocity.debug("VelocityViewServlet: No custom properties found. "	+ "Using default Velocity configuration.");
		}
		return p;
	}
}
