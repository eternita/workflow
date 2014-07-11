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

public class ExecutionResult {

    int errorCode = -1;
    Exception exception = null;
    FlowContext logicContext = null;

    String lastSuccessfulNodeName = null;

    public ExecutionResult(FlowContext logicContext) {
        this.logicContext = logicContext;
    }

    public void setExecutionExeption(Exception ex) {
        this.exception = ex;
    }

    public Exception getException() {
        return exception;
    }

    public FlowContext getFlowContext() {
        return logicContext;
    }

    public void print() {
        System.out.println("exception is" + exception);
        if (exception != null) {
            exception.printStackTrace();
        }

    }

    public String getLastSuccessfulNodeName() {
        return lastSuccessfulNodeName;
    }

    public void setLastSuccessfulNodeName(String lastSuccessfulNodeName) {
        this.lastSuccessfulNodeName = lastSuccessfulNodeName;
    }

}
