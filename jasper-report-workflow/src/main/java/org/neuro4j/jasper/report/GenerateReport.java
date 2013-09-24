package org.neuro4j.jasper.report;




import static org.neuro4j.jasper.report.GenerateReport.*;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.neuro4j.logic.LogicContext;
import org.neuro4j.logic.def.CustomBlock;
import org.neuro4j.logic.swf.FlowExecutionException;
import org.neuro4j.logic.swf.FlowInitializationException;
import org.neuro4j.logic.swf.ParameterDefinition;
import org.neuro4j.logic.swf.ParameterDefinitionList;

@ParameterDefinitionList(input={
                                	@ParameterDefinition(name=IN_JASPER_FORMAT, isOptional=true, type= "java.lang.String"),
                                	@ParameterDefinition(name=IN_JASPER_OUTPUT_STREAM, isOptional=false, type= "java.io.OutputStream"),
                                	@ParameterDefinition(name=IN_JASPER_INTPUT_STREAM, isOptional=false, type= "java.io.InputStream"),
                                	@ParameterDefinition(name=IN_JASPER_PARAMETERS, isOptional=true, type= "java.util.Map")                                	
                                	},
                         output={
                         	        })	

public class GenerateReport extends CustomBlock {
    
   
    static final String IN_JASPER_OUTPUT_STREAM = "jasperOutputStream";
    static final String IN_JASPER_INTPUT_STREAM = "jasperInputStream";
    
	static final String IN_JASPER_PARAMETERS = "jasperParameters";
	static final String IN_JASPER_FORMAT = "jasperFormat";

	static final String IN_JASPER_DATASOURCE = "jasperDataSource";
    
    private static final String DEFAULT_FORMAT = "pdf";
      
    
    
    @Override
    public int execute(LogicContext ctx) throws FlowExecutionException {
		
		String format = (String)ctx.get(IN_JASPER_FORMAT);
		if (format == null)
		{
			format = DEFAULT_FORMAT;
		}
		
		Map<String, Object> parameters = (Map<String, Object>)ctx.get(IN_JASPER_PARAMETERS);
		
		if(parameters == null)
		{
			parameters = new HashMap<String, Object>();
		}
		
	    JRDataSource dataSource = (JRDataSource)ctx.get(IN_JASPER_DATASOURCE);
	    if (dataSource == null)
	    {
	    	dataSource = new JREmptyDataSource();
	    }
	    
	    OutputStream output = (OutputStream)ctx.get(IN_JASPER_OUTPUT_STREAM);
	    
	    InputStream jasperInputStream = (InputStream)ctx.get(IN_JASPER_INTPUT_STREAM);
		

		JasperPrint jasperPrint = null;
		try {
		 JasperReport jasperReport = (JasperReport)JRLoader.loadObject(jasperInputStream);
		 jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

			 
			JRAbstractExporter exporter = getJRExporter(format);
			
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
			exporter.exportReport();

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
				
		
		return NEXT;
	}

	
	private JRAbstractExporter getJRExporter(String format) {
		JRAbstractExporter exporter = null;
		if (format.equals("pdf")) {
			exporter = new JRPdfExporter();
		} else if (format.equals("rtf")) {
			exporter = new JRRtfExporter();
		}else if (format.equals("html")) {
			exporter = new JRHtmlExporter();
		}else if (format.equals("docx")) {
			exporter = new JRDocxExporter();
		}else if (format.equals("pptx")) {
			exporter = new JRPptxExporter();
		}
		
		return exporter;
	}
	
	@Override
	protected void init() throws FlowInitializationException{
		super.init();
	}
	
	
	

}
