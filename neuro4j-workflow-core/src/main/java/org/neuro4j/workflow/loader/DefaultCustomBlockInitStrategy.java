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
package org.neuro4j.workflow.loader;

import org.apache.commons.beanutils.ConstructorUtils;
import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.log.Logger;
import static org.neuro4j.workflow.utils.Validation.*;

/**
 * This class provides default implementation of CustomBlockInitStrategy.
 *
 */
public class DefaultCustomBlockInitStrategy implements CustomBlockInitStrategy {

	@Override
	public ActionBlock loadCustomBlock(String className) throws FlowExecutionException {
		Class<? extends ActionBlock> clazz = getCustomBlockClass(className);
		try {
			if (null != clazz) {
				ActionBlock action = (ActionBlock) ConstructorUtils.invokeConstructor(clazz, null);
				if (action != null) {
					return action;
				}
			}

		} catch (Exception e) {
			Logger.error(this, e);
		}
		throw new FlowExecutionException("CustomBlock: " + className + " can not be initialized.");
	}

	public Class<? extends ActionBlock> getCustomBlockClass(String className) throws FlowExecutionException {
		requireNonNull(className, () -> new FlowExecutionException("CustomClassName can not be null"));
		try {
			Class<?> clazz = getClass().getClassLoader().loadClass(className);
			if (null != clazz) {
				if (ActionBlock.class.isAssignableFrom(clazz)) {
					if (clazz != null) {
						return (Class<? extends ActionBlock>) clazz;
					}
				} else {
					throw new FlowExecutionException(className + " does not implement org.neuro4j.workflow.ActionBlock");
				}
			}

		} catch (ClassNotFoundException e) {
			Logger.error(this, e);
		}
		throw new FlowExecutionException("CustomBlock: " + className + " can not be initialized.");
	}

}
