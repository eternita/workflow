/**
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.neuro4j.springframework.context;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.loader.CustomBlockInitStrategy;
import org.neuro4j.workflow.loader.DefaultCustomBlockInitStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

/**
 * This class uses Spring's beanFactory to initialize Custom Blocks.
 *  Custom block should be marked as @Component (org.springframework.stereotype.Component)
 * If class does not have this annotation it will be loaded as simple class.
 */
public class SpringContextInitStrategy implements CustomBlockInitStrategy {

	private static final Logger Logger = LoggerFactory.getLogger(SpringContextInitStrategy.class);

	
	private BeanFactory beanFactory;

	private final CustomBlockInitStrategy defaultStrategy;

	public SpringContextInitStrategy(BeanFactory beanFactory) {
		this();
		this.beanFactory = beanFactory;
	}

	public SpringContextInitStrategy() {
		super();
		this.defaultStrategy = new DefaultCustomBlockInitStrategy();
	}
	
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}


	public ActionBlock loadCustomBlock(String className) throws FlowExecutionException {
		ActionBlock customBlock = null;
		try {

			Class<?> clazz = getClass().getClassLoader().loadClass(className);
			if (ActionBlock.class.isAssignableFrom(clazz)) {
				Component component = clazz.getAnnotation(org.springframework.stereotype.Component.class);
				if (component != null) {
					customBlock = (ActionBlock) beanFactory.getBean(clazz);
				} else {
					customBlock = this.defaultStrategy.loadCustomBlock(className);
				}
			} else {
				throw new FlowExecutionException(
						"Class " + className + " does not implement org.neuro4j.workflow.ActionBlock");
			}

		} catch (ClassNotFoundException e) {
			Logger.error(e.getMessage(), e);
			throw new FlowExecutionException(e);
		}
		return customBlock;

	}

}
