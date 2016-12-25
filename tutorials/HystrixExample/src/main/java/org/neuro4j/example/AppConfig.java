package org.neuro4j.example;

import org.neuro4j.springframework.context.SpringContextInitStrategy;
import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.common.WorkflowEngine.ConfigBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

	@Autowired
	SpringContextInitStrategy initStrategy;

	@Bean
	@Scope("singleton")
	public WorkflowEngine getWorkflowEngine() {
		return new WorkflowEngine(new ConfigBuilder().withCustomBlockInitStrategy(initStrategy));
	}

}