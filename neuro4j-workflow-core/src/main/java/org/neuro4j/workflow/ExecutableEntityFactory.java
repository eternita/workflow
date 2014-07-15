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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neuro4j.workflow.common.FlowInitializationException;
import org.neuro4j.workflow.node.CustomBlock;
import org.neuro4j.workflow.node.CustomNode;

@SuppressWarnings("rawtypes")
public class ExecutableEntityFactory {

    private static Map<String, Class> entities = new HashMap<String, Class>();
    private static Map<String, String> shortNames = new HashMap<String, String>();

    public static CustomBlock getActionEntity(CustomNode node) throws FlowInitializationException
    {
        String executableClass = node.getExecutableClass();
        try {
            Class clazz = null;
            if (-1 == executableClass.indexOf('.'))
            {
                // short name
                // String fullName = shortNames.get(name);
                // if (null != fullName)
                clazz = entities.get(shortNames.get(executableClass));
            } else {
                clazz = entities.get(executableClass);
            }

            if (null == clazz)
            {
                clazz = Class.forName(executableClass);
                if (null != clazz)
                    entities.put(executableClass, clazz);
            }
            Object bObj = clazz.newInstance();
            if (bObj instanceof CustomBlock){
                CustomBlock ab = (CustomBlock)bObj;
            //    ab.setUuid(node.getUuid());
                return ab;                
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        throw new FlowInitializationException("Block " + executableClass + " not found");
    }

    @SuppressWarnings("rawtypes")
    private static boolean implementsInterface(Class checked, Class interf) {
        for (Class c : checked.getInterfaces()) {
            if (c.equals(interf)) {
                return true;
            }
        }

        if (null != checked.getSuperclass()) // && !checked.getSuperclass().equals(Object.class)
        {
            return implementsInterface(checked.getSuperclass(), interf);
        }

        return false;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     * 
     * @param packageName
     *        The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static List<Class> getClasses(String packageName)
            throws ClassNotFoundException, IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String fileName = resource.getFile();
            String fileNameDecoded = URLDecoder.decode(fileName, "UTF-8");
            dirs.add(new File(fileNameDecoded));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     * 
     * @param directory
     *        The base directory
     * @param packageName
     *        The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("rawtypes")
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException
    {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                assert !fileName.contains(".");
                classes.addAll(findClasses(file, packageName + "." + fileName));
            } else if (fileName.endsWith(".class") && !fileName.contains("$")) {
                Class _class;
                try {
                    _class = Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - 6));
                } catch (ExceptionInInitializerError e) {
                    // happen, for example, in classes, which depend on
                    // Spring to inject some beans, and which fail,
                    // if dependency is not fulfilled
                    _class = Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - 6),
                            false, Thread.currentThread().getContextClassLoader());
                }
                classes.add(_class);
            }
        }
        return classes;
    }

}
