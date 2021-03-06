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
package net.erdfelt.util.jarinfo;

import java.nio.file.Files;
import java.nio.file.Path;

import net.erdfelt.util.jarinfo.analysis.ClassReference;
import net.erdfelt.util.jarinfo.analysis.JarAnalyzer;

/**
 * StandardDumper
 */
public class StandardDumper implements Dumper
{
    @Override
    public void dump(JarAnalyzer analyzer) throws Exception
    {
        System.out.println("Path: " + analyzer.getPath());
        System.out.println("Size: " + analyzer.getFileSize());

        for (Path entry : analyzer.getAll())
        {
            if (Files.isRegularFile(entry))
            {
                if(entry.getFileName().toString().endsWith(".class"))
                {
                    ClassReference classReference = analyzer.parseClass(entry);
                    System.out.println("  class: " + classReference.getClassName());
                    System.out.println("    version: " + classReference.getBytecodeVersion());
                    System.out.println("    sha1: " + classReference.getHash());
                    System.out.println("    methods: ");
                    classReference.getMethodSignatures().forEach(sig -> System.out.println("     # " + sig));
                }
                else
                {
                    System.out.println("  file: " + entry);
                }
            }
            else if(Files.isDirectory(entry))
            {
                System.out.println("  dir : " + entry);
            }
        }
    }
}
