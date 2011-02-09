package net.erdfelt.util.jarinfo;

/*
 * Copyright 2006 Joakim Erdfelt
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.codehaus.plexus.component.annotations.Component;

/**
 * StandardDumper
 * 
 * @author Joakim Erdfelt
 */
@Component(role = Dumper.class, hint = "standard")
public class StandardDumper implements Dumper
{
    public void dump(File jarFile) throws Exception
    {
        JarFile jar = new JarFile(jarFile);
        List<JarEntry> entries = new ArrayList<JarEntry>();
        entries.addAll(Collections.list(jar.entries()));
        Collections.sort(entries,new JarEntryComparator());

        String jarfilename = jarFile.getAbsolutePath();

        for (JarEntry entry : entries)
        {
            if (entry.getName().endsWith(".class"))
            {
                String classname = entry.getName();

                ClassParser classParser = new ClassParser(jarfilename,entry.getName());
                JavaClass javaClass;
                try
                {
                    javaClass = classParser.parse();

                    String classSignature = javaClass.getClassName();

                    OUT(classSignature + " package:" + javaClass.getPackageName());

                    Method methods[] = javaClass.getMethods();
                    for (int i = 0; i < methods.length; i++)
                    {
                        OUT(classSignature + "." + methods[i].getName() + methods[i].getSignature());
                    }
                }
                catch (ClassFormatException e)
                {
                    OUT("Unable to process class " + classname);
                    continue;
                }
            }
            else
            {
                OUT(entry.getName());
            }
        }
    }

    private void OUT(String msg)
    {
        System.out.println(msg);
    }
}
