package net.erdfelt.util.jarinfo;

import org.eclipse.jetty.toolchain.test.OS;
import org.junit.Test;

public class JarInfoTest
{
    @Test
    public void testDumpSimple()
    {
        String args[] =
        { OS.separators("src/test/jars/mystery.jar") };
        JarInfo.main(args);
    }

    @Test
    public void testDumpDelimited()
    {
        String args[] =
        { "-o", "DELIMITED", OS.separators("src/test/jars/mystery.jar") };
        JarInfo.main(args);
    }
}
