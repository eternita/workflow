/*
 * Copyright (c) 2013-2014, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuro4j.jasper.report;

import static org.neuro4j.jasper.report.GenerateReport.IN_JASPER_FORMAT;
import static org.neuro4j.jasper.report.GenerateReport.IN_JASPER_INTPUT_STREAM;
import static org.neuro4j.jasper.report.GenerateReport.IN_JASPER_OUTPUT_STREAM;
import static org.neuro4j.jasper.report.GenerateReport.IN_JASPER_PARAMETERS;

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

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input = {
        @ParameterDefinition(name = IN_JASPER_FORMAT, isOptional = true, type = "java.lang.String"),
        @ParameterDefinition(name = IN_JASPER_OUTPUT_STREAM, isOptional = false, type = "java.io.OutputStream"),
        @ParameterDefinition(name = IN_JASPER_INTPUT_STREAM, isOptional = false, type = "java.io.InputStream"),
        @ParameterDefinition(name = IN_JASPER_PARAMETERS, isOptional = true, type = "java.util.Map")
},
        output = {
        })
public class GenerateReport extends CustomBlock {

    static final String IN_JASPER_OUTPUT_STREAM = "jasperOutputStream";
    static final String IN_JASPER_INTPUT_STREAM = "jasperInputStream";

    static final String IN_JASPER_PARAMETERS = "jasperParameters";
    static final String IN_JASPER_FORMAT = "jasperFormat";

    static final String IN_JASPER_DATASOURCE = "jasperDataSource";

    private static final String DEFAULT_FORMAT = "pdf";

    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {

        String format = (String) ctx.get(IN_JASPER_FORMAT);
        if (format == null)
        {
            format = DEFAULT_FORMAT;
        }

        Map<String, Object> parameters = (Map<String, Object>) ctx.get(IN_JASPER_PARAMETERS);

        if (parameters == null)
        {
            parameters = new HashMap<String, Object>();
        }

        JRDataSource dataSource = (JRDataSource) ctx.get(IN_JASPER_DATASOURCE);
        if (dataSource == null)
        {
            dataSource = new JREmptyDataSource();
        }

        OutputStream output = (OutputStream) ctx.get(IN_JASPER_OUTPUT_STREAM);

        InputStream jasperInputStream = (InputStream) ctx.get(IN_JASPER_INTPUT_STREAM);

        JasperPrint jasperPrint = null;
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperInputStream);
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
        } else if (format.equals("html")) {
            exporter = new JRHtmlExporter();
        } else if (format.equals("docx")) {
            exporter = new JRDocxExporter();
        } else if (format.equals("pptx")) {
            exporter = new JRPptxExporter();
        }

        return exporter;
    }

    @Override
    protected void init() throws FlowInitializationException {
        super.init();
    }

}
