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

package org.neuro4j.workflow.hystrix;

import static org.neuro4j.workflow.enums.ActionBlockCache.NONE;
import static org.neuro4j.workflow.hystrix.DefaultHystrixBootstrap.DEFAULT_COMMAND_KEY;
import static org.neuro4j.workflow.hystrix.DefaultHystrixBootstrap.DEFAULT_GROUP_KEY;
import static org.neuro4j.workflow.hystrix.DefaultHystrixBootstrap.getSetterForGroup;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.enums.CachedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@CachedNode(type=NONE)
public abstract class BaseHystrixCommand extends HystrixLifecycleCommand implements ActionBlock {

	private static Logger logger = LoggerFactory.getLogger(BaseHystrixCommand.class);
	
	
	protected BaseHystrixCommand() {
		this(getSetterForGroup(DEFAULT_GROUP_KEY, DEFAULT_COMMAND_KEY));
	}
    protected BaseHystrixCommand(Setter setter) {
        super(setter);
    }
	
	
	@Override
	protected abstract Integer executeInternal(FlowContext ctx) throws Exception;

	@Override
	public final int execute(FlowContext context) throws FlowExecutionException {
		setFlowContext(context);
		return execute();
	}
	
	@Override
    protected Integer getFallback() {
        logger.debug("Running fallback for command {}", this.getClass().getSimpleName());
       return ERROR;
    }

}
