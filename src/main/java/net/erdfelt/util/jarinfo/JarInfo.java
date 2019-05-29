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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.erdfelt.util.jarinfo.analysis.JarAnalyzer;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * JarInfo
 */
public class JarInfo
{
    public enum OutputFormat
    {
        DELIMITED, STANDARD
    }

    @Option(name = "-o", aliases =
        {"--output-format"}, usage = "Output format")
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
        String dumperHint = "standard"; // default
        if (outputFmt == OutputFormat.DELIMITED)
        {
            dumperHint = "delimited";
        }
        Dumper dumper = DumperFactory.getDumper(dumperHint);

        for (String jarFilename : jarfilenames)
        {
            Path jarFile = Paths.get(jarFilename);

            if (!Files.exists(jarFile))
            {
                System.err.println("ERROR: Jar file does not exist: " + jarFile);
                System.exit(1);
            }

            if (!Files.isRegularFile(jarFile))
            {
                System.err.println("ERROR: Not a file: " + jarFile);
                System.exit(1);
            }

            if (!Files.isReadable(jarFile))
            {
                System.err.println("ERROR: Unable to read file: " + jarFile);
                System.exit(1);
            }

            try (JarAnalyzer jarAnalyzer = new JarAnalyzer(jarFile))
            {
                dumper.dump(jarAnalyzer);
            }
        }
    }
}
