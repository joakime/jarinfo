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
import java.util.List;
import java.util.jar.JarEntry;

import org.apache.maven.shared.jar.classes.JarClasses;
import org.codehaus.plexus.component.annotations.Component;

/**
 * DelimitedDumper
 * 
 * @author Joakim Erdfelt
 */
@Component(role = Dumper.class, hint = "delimited")
public class DelimitedDumper implements Dumper
{
    public void dump(File jarFile) throws Exception
    {
        JarAnalysis analysis = new JarAnalysis(jarFile);

        OUT(jarFile,"FILE|" + jarFile.getName());
        OUT(jarFile,"SIZE|" + jarFile.length());
        OUT(jarFile,"HASH_MD5|" + analysis.getHash(JarAnalysis.MD5));
        OUT(jarFile,"HASH_SHA1|" + analysis.getHash(JarAnalysis.SHA1));
        OUT(jarFile,"HASH_BYTECODE|" + analysis.getHash(JarAnalysis.BYTECODE));

        JarClasses jarClasses = analysis.getJarData().getJarClasses();
        OUT(jarFile,"JDK|" + jarClasses.getJdkRevision());

        @SuppressWarnings("unchecked")
        List<String> classnames = jarClasses.getClassNames();
        for (String classname : classnames)
        {
            OUT(jarFile,"CLASS|" + classname);
        }

        @SuppressWarnings("unchecked")
        List<String> methodnames = jarClasses.getMethods();
        for (String methodName : methodnames)
        {
            OUT(jarFile,"METHOD|" + methodName);
        }

        @SuppressWarnings("unchecked")
        List<JarEntry> entries = analysis.getEntries();
        for (JarEntry entry : entries)
        {
            OUT(jarFile,"FILE|" + entry.getName());
        }
    }

    private void OUT(File jarFile, String msg)
    {
        System.out.println(jarFile.getAbsolutePath() + "|" + msg);
    }
}
