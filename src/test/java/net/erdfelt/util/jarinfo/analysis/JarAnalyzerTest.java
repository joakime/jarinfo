package net.erdfelt.util.jarinfo.analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
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
        }
    }
}
