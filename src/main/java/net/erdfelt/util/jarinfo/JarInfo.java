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
import java.util.List;

import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainer;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * JarInfo
 * 
 * @author Joakim Erdfelt
 */
public class JarInfo
{
    public static enum OutputFormat
    {
        DELIMITED, STANDARD
    }

    @Option(name = "-o", aliases =
    { "--output-format" }, usage = "Output format")
    private OutputFormat outputFmt = OutputFormat.STANDARD;

    @Argument(required = true, index = 0, metaVar = "FILE", usage = "jar file")
    private List<String> jarfilenames = new ArrayList<String>();

    public static void main(String[] args)
    {
        JarInfo info = new JarInfo();
        CmdLineParser parser = new CmdLineParser(info);
        try
        {
            parser.parseArgument(args);
            info.execute();
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage());
            System.err.println("java -jar jarinfo.jar [options...] arguments...");
            parser.printUsage(System.err);
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }

    private void execute() throws Exception
    {
        PlexusContainer plexus = new DefaultPlexusContainer();
        try
        {
            String dumperHint = "standard"; // default
            if (outputFmt == OutputFormat.DELIMITED)
            {
                dumperHint = "delimited";
            }
            Dumper dumper = (Dumper)plexus.lookup(Dumper.class.getName(),dumperHint);

            for (String jarFilename : jarfilenames)
            {
                File jarFile = new File(jarFilename);

                if (!jarFile.exists())
                {
                    System.err.println("ERROR: Jar file does not exist: " + jarFile.getAbsolutePath());
                    System.exit(1);
                }

                if (!jarFile.isFile())
                {
                    System.err.println("ERROR: Not a file: " + jarFile.getAbsolutePath());
                    System.exit(1);
                }

                if (!jarFile.canRead())
                {
                    System.err.println("ERROR: Unable to read file: " + jarFile.getAbsolutePath());
                    System.exit(1);
                }

                dumper.dump(jarFile);
            }
        }
        finally
        {
            plexus.dispose();
        }
    }
}
