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
package net.erdfelt.util.jarinfo.analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class JarAnalyzerTest
{
    @Test
    public void testGetClasses() throws IOException
    {
        Path mystery = MavenTestingUtils.getTestResourcePathFile("jars/mystery.jar");

        try (JarAnalyzer analyzer = new JarAnalyzer(mystery))
        {
            List<Path> classes = analyzer.getClasses();
            assertThat(classes, notNullValue());
            assertThat(classes.size(), is(84));
        }
    }

    @Test
    public void testGetReference() throws IOException
    {
        Path jettyUtil = MavenTestingUtils.getTestResourcePathFile("jars/jetty-util.jar");

        try (JarAnalyzer analyzer = new JarAnalyzer(jettyUtil))
        {
            JarReference jarReference = analyzer.getReference();
            assertThat(jarReference, notNullValue());

            assertThat("groupId", jarReference.getGroupId(), contains("org.eclipse.jetty"));
            assertThat("artifactId", jarReference.getArtifactId(), contains("jetty-util"));
            assertThat("versionId", jarReference.getVersion(), contains("9.4.18.v20190429"));

            assertThat("built-by", jarReference.getBuiltBy(), contains("joakim"));
            assertThat("built-jdk", jarReference.getBuildJdk(), contains("11.0.2"));
        }
    }
}
