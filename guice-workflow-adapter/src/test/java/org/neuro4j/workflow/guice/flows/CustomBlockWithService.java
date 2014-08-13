
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

package org.neuro4j.workflow.guice.flows;

import org.junit.Assert;
import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.guice.service.MessageService;
import org.neuro4j.workflow.node.CustomBlock;

import com.google.inject.Inject;


/**
 * This custom block uses Google Guice library for dependency injection.
 *
 */
public class CustomBlockWithService extends CustomBlock {

    private MessageService service;

    public int execute(FlowContext ctx) throws FlowExecutionException {

        Assert.assertEquals(this.service.sendMessage("Hi", "Mister"), "Message to Mister = Hi");
        return NEXT;
    }


    @Inject
    public void setService(MessageService svc) {
        this.service = svc;
    }

}
