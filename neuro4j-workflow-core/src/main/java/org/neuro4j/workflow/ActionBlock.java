/*
 * Copyright (c) 2013-2016, Neuro4j
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

import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;

/**
 * Base interface for executable blocks. 
 *
 */
public interface ActionBlock {

    public static final int NEXT = 1;
    public static final int ERROR = 2;
    
    /**
     * @param context input context
     * @return  1 (NEXT) or 2 (ERROR) exit
     * @throws FlowExecutionException in case of error during execution
     */
    public int execute(FlowContext context) throws FlowExecutionException;
    
    /**
     * Processor run this method once during initialization.
     * @throws FlowInitializationException if there is an error
     */
    public default void init() throws FlowExecutionException{
    	
    };
    
}
