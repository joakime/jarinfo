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

import java.nio.file.Path;

import net.erdfelt.util.jarinfo.analysis.JarAnalyzer;
import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.jupiter.api.Test;

/**
 * DelimitedDumperTest
 */
public class DelimitedDumperTest
{
    @Test
    public void testJarDump() throws Exception
    {
        Dumper dumper = new DelimitedDumper();
        Path mystery = MavenTestingUtils.getTestResourcePathFile("jars/mystery.jar");
        try (JarAnalyzer jarAnalyzer = new JarAnalyzer(mystery))
        {
            dumper.dump(jarAnalyzer);
        }
    }
}
