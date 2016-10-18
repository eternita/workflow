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

package org.neuro4j.jasper.output;

import static org.neuro4j.jasper.output.CreateFileOutputStream.IN_CREATEFILE;
import static org.neuro4j.jasper.output.CreateFileOutputStream.IN_FILEPATH;
import static org.neuro4j.jasper.output.CreateFileOutputStream.OUT_JASPER_OUTPUTSTREAM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input = {
        @ParameterDefinition(name = IN_FILEPATH, isOptional = false, type = "java.lang.String"),
        @ParameterDefinition(name = IN_CREATEFILE, isOptional = false, type = "java.lang.String") },

        output = {
                @ParameterDefinition(name = OUT_JASPER_OUTPUTSTREAM, isOptional = false, type = "java.io.OutputStream") })
public class CreateFileOutputStream extends CustomBlock {

    static final String IN_FILEPATH = "filePath";
    static final String IN_CREATEFILE = "createFile";

    static final String OUT_JASPER_OUTPUTSTREAM = "jasperOutputStream";

    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {

        String filePath = (String) ctx.get(IN_FILEPATH);
        String createFile = (String) ctx.get(IN_CREATEFILE);
        boolean createFileBoolean = false;

        if (createFile != null)
        {
            createFileBoolean = Boolean.parseBoolean(createFile);
        }

        File file = new File(filePath);

        try {

            if (createFileBoolean && !file.exists())
            {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return ERROR;
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ctx.put(OUT_JASPER_OUTPUTSTREAM, fileOutputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ERROR;
        }

        return NEXT;
    }


}
