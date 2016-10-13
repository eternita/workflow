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

package org.neuro4j.jasper.datasource;

import static org.neuro4j.jasper.datasource.CreateJRBeanCollectionDataSource.IN_COLLECTION;
import static org.neuro4j.jasper.datasource.CreateJRBeanCollectionDataSource.OUT_JASPERDATASOURCE;

import java.util.Collection;
import java.util.Collections;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.neuro4j.workflow.FlowContext;
import org.neuro4j.workflow.common.FlowExecutionException;
import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.common.ParameterDefinition;
import org.neuro4j.workflow.common.ParameterDefinitionList;
import org.neuro4j.workflow.node.CustomBlock;

@ParameterDefinitionList(input = {
        @ParameterDefinition(name = IN_COLLECTION, isOptional = true, type = "java.util.Collection") },
        output = {
                @ParameterDefinition(name = OUT_JASPERDATASOURCE, isOptional = false, type = "net.sf.jasperreports.engine.data.JRBeanCollectionDataSource") })
public class CreateJRBeanCollectionDataSource extends CustomBlock {

    static final String IN_COLLECTION = "collection";

    static final String OUT_JASPERDATASOURCE = "jasperDataSource";

    @Override
    public int execute(FlowContext ctx) throws FlowExecutionException {

        Collection<?> collection = (Collection<?>) ctx.get(IN_COLLECTION);

        JRBeanCollectionDataSource datasource = null;
        if (collection != null)
        {
            datasource = new JRBeanCollectionDataSource(collection);
        } else {
            datasource = new JRBeanCollectionDataSource(Collections.EMPTY_LIST);
        }

        ctx.put(OUT_JASPERDATASOURCE, datasource);

        return NEXT;
    }

    @Override
    public void init() throws FlowInitializationException {
        super.init();
    }

}
