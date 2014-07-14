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

package org.neuro4j.workflow;

/**
 * Class holds information regarding execution result. 
 *
 */
public class ExecutionResult {

    int errorCode = -1;
    
    /**
     *  Keeps exception if flow finished with error.
     */
    Exception exception = null;
    
    /**
     *  Keeps context after execution.
     */
    FlowContext flowContext = null;

    /**
     * Keeps name of last node which was executed.
     */
    String lastSuccessfulNodeName = null;

    public ExecutionResult(FlowContext logicContext) {
        this.flowContext = logicContext;
    }

    public void setExecutionExeption(Exception ex) {
        this.exception = ex;
    }

    public Exception getException() {
        return exception;
    }

    /**
     * Returns flowContext with output parameters.
     * @return flow context.
     */
    public FlowContext getFlowContext() {
        return flowContext;
    }

    public void print() {
        System.out.println("exception is" + exception);
        if (exception != null) {
            exception.printStackTrace();
        }

    }

    /**
     * Returns last executed node.
     * @return node name
     */
    public String getLastSuccessfulNodeName() {
        return lastSuccessfulNodeName;
    }

    public void setLastSuccessfulNodeName(String lastSuccessfulNodeName) {
        this.lastSuccessfulNodeName = lastSuccessfulNodeName;
    }

}
