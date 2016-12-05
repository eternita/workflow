
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

package org.neuro4j.workflow.spring;


import static org.neuro4j.workflow.spring.CustomBlockWithService.OUT_OUT1;

import org.neuro4j.workflow.ActionBlock;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.spring.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;





/**
 * This custom block uses Google Guice library for dependency injection.
 *
 */
@ParameterDefinitionList(input={},
output={
	        @ParameterDefinition(name=OUT_OUT1, isOptional=true, type= "java.lang.String")})	
@Component
public class CustomBlockWithService implements ActionBlock {

    static final String OUT_OUT1 = "out1"; 
	
    @Autowired
    public MessageService service;

    public int execute(FlowContext ctx) throws FlowExecutionException {
    	String  message = this.service.sendMessage("Hi", "Mister");
		ctx.put(OUT_OUT1, message); 
        return NEXT;
    }


    
    
}
