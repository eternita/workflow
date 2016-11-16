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
package org.neuro4j.workflow.node;

import java.util.concurrent.CompletableFuture;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.WorkflowRequest;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.loader.f4j.SWFConstants;

/**
 * JoinBlock links different blocks. Block has many 'input' transitions and one 'output' transition.
 */
public class JoinNode extends WorkflowNode {

    private Transition next = null;
    
    private boolean isJoinAfterFork = false;


    public JoinNode(String name, String uuid) {
        super(name, uuid);
    }

    @Override
    public final Transition execute(final WorkflowProcessor processor, final WorkflowRequest request)
            throws FlowExecutionException {
    	if (isJoinAfterFork){
    		
    		if (request.getCompletableFutures().isEmpty()){
    			return null;
    		}
    		
    		CompletableFuture.allOf(request.getCompletableFutures().toArray(new CompletableFuture<?>[0])).join();
    	}
        request.setNextRelation(next);
        return next;
    }

    @Override
    public final void init() throws FlowExecutionException {
        next = getExitByName(SWFConstants.NEXT_RELATION_NAME);
        return;
    }

    @Override
    public final void validate(final WorkflowProcessor processor, final FlowContext ctx) throws FlowExecutionException {
        if (next == null)
        {
            throw new FlowExecutionException("JoinBlock: Wrong configuration");
        }

    }

	public void setFork(boolean b) {
		this.isJoinAfterFork = b;
	}

}
