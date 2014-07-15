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

package org.neuro4j.jasper.input;

import static org.neuro4j.jasper.input.LoadTemplate.IN_TEMPLATEPATH;
import static org.neuro4j.jasper.input.LoadTemplate.OUT_JASPER_INPUTSTREAM;

import java.io.InputStream;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input = {
        @ParameterDefinition(name = IN_TEMPLATEPATH, isOptional = false, type = "java.lang.String") },
        output = {
                @ParameterDefinition(name = OUT_JASPER_INPUTSTREAM, isOptional = true, type = "java.io.InputStream") })
public class LoadTemplate extends CustomBlock {

    static final String IN_TEMPLATEPATH = "templatePath";

    static final String OUT_JASPER_INPUTSTREAM = "jasperInputStream";

    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {
        // reports/report1.jasper
        String templatePath = (String) ctx.get(IN_TEMPLATEPATH);

        InputStream inputStream = LoadTemplate.class.getClassLoader().getResourceAsStream(templatePath);

        // TODO: put your code here

        if (inputStream == null)
        {
            return ERROR;
        }

        ctx.put(OUT_JASPER_INPUTSTREAM, inputStream);

        return NEXT;
    }

    @Override
    public void init() throws FlowInitializationException {
        super.init();
    }

}
