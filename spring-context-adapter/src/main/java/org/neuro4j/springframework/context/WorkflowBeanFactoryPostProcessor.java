package org.neuro4j.springframework.context;

import org.neuro4j.workflow.common.WorkflowEngine;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkflowBeanFactoryPostProcessor extends SpringContextInitStrategy implements CustomBlockInitStrategy, BeanFactoryPostProcessor {

	
	public WorkflowBeanFactoryPostProcessor(BeanFactory beanFactory) {
		super(beanFactory);
	}



	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		WorkflowEngine.setCustomBlockInitStrategy(this);
	}

}
