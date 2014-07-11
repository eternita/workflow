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

package org.neuro4j.workflow.loader;

import java.io.IOException;
import java.io.InputStream;

import org.neuro4j.workflow.Workflow;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.loader.n4j.ConvertationException;
import org.neuro4j.workflow.loader.n4j.NetworkConverter;

public class WorkflowLoader {

    public static Workflow loadWorkFlowFromFile(String file) throws FlowInitializationException
    {
        Workflow flow = null;
        InputStream fis = WorkflowLoader.class.getClassLoader().getResourceAsStream(file + ".n4j");
        if (null != fis) {
            flow = loadFlowFromFS(fis, file);
        }

        return flow;
    }

    public static Workflow loadFlowFromFS(InputStream is, String flow) throws FlowInitializationException
    {
        Workflow net = null;
        try {
            if (null != is)
                try {
                    net = NetworkConverter.xml2workflow(is, flow);
                } catch (ConvertationException e) {
                    e.printStackTrace();
                }
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return net;
    }

}
