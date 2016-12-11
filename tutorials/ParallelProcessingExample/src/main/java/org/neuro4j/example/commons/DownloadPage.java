package org.neuro4j.example.commons;

import static org.neuro4j.example.commons.DownloadPage.IN_URL;
import static org.neuro4j.example.commons.DownloadPage.OUT_DOCUMENT;
import static org.neuro4j.workflow.enums.ActionBlockCache.SINGLETON;

import java.io.IOException;

import org.neuro4j.example.services.DownloadService;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.enums.CachedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@ParameterDefinitionList(input = {
		@ParameterDefinition(name = IN_URL, isOptional = true, type = "java.lang.String") }, output = {
				@ParameterDefinition(name = OUT_DOCUMENT, isOptional = true, type = "java.lang.String") })
@CachedNode(type = SINGLETON)
@Component
public class DownloadPage implements ActionBlock {

	private static final Logger Logger = LoggerFactory.getLogger(DownloadPage.class);

	static final String IN_URL = "url";

	static final String OUT_DOCUMENT = "document";
	

	@Autowired
	private DownloadService service;

	public int execute(FlowContext ctx) throws FlowExecutionException {

		String url = (String) ctx.get(IN_URL);

		Logger.debug("Downloading page {}", url);

		String content = "";

		try {

			content = service.download(url);

		} catch (IOException e) {
			Logger.error("Error during downloading url {}", url, e);
		}

		ctx.put(OUT_DOCUMENT, content);

		return NEXT;
	}

}
