package net.erdfelt.util.jarinfo;

import java.nio.file.Path;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.Test;

public class JarInfoTest
{
    @Test
    public void testDumpSimple()
    {
        Path mystery = MavenTestingUtils.getTestResourcePathFile("jars/mystery.jar");
        String args[] = {mystery.toString()};
        JarInfo.main(args);
    }

    @Test
    public void testDumpDelimited()
    {
        Path mystery = MavenTestingUtils.getTestResourcePathFile("jars/mystery.jar");
        String args[] = {"-o", "DELIMITED", mystery.toString()};
        JarInfo.main(args);
    }
}
