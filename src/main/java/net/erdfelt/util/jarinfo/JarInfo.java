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

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import net.erdfelt.util.jarinfo.analysis.JarAnalyzer;

/**
 * JarInfo
 */
public class JarInfo
{
    public enum OutputFormat
    {
        DELIMITED, STANDARD
    }

    private OutputFormat outputFmt = OutputFormat.STANDARD;
    private List<String> jarfilenames = new ArrayList<>();

    public static void main(String[] args)
    {
        JarInfo info = new JarInfo();
        try
        {
            info.parseCommandLine(args);
            info.execute();
        }
        catch (IllegalArgumentException e)
        {
            info.showUsage(System.err, e);
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }

    private void parseCommandLine(String[] args) throws IllegalArgumentException
    {
        LinkedList<String> cmdline = new LinkedList<>(Arrays.asList(args));
        ListIterator<String> cmdIter = cmdline.listIterator();

        while(cmdIter.hasNext())
        {
            String arg = cmdIter.next();

            if(arg.equalsIgnoreCase("-o"))
            {
                if(cmdIter.hasNext())
                {
                    outputFmt = OutputFormat.valueOf(cmdIter.next().toUpperCase(Locale.ENGLISH));
                }
                else
                {
                    throw new IllegalArgumentException("Missing outputFormat");
                }
            }
            else if(arg.startsWith("--output-format="))
            {
                String name = arg.substring("--output-format=".length());
                outputFmt = OutputFormat.valueOf(name);
            }
            else if(arg.endsWith(".jar"))
            {
                jarfilenames.add(arg);
            }
            else
            {
                throw new IllegalArgumentException("Unrecognized command line argument");
            }
        }

        if(jarfilenames.isEmpty())
        {
            throw new IllegalArgumentException("FILE argument is required");
        }
    }

    private void showUsage(PrintStream out, Throwable cause)
    {
        if(cause != null)
        {
            cause.printStackTrace(out);
        }
        out.println("java -jar jarinfo.jar [options...] FILE...");
        out.println("FILE                                         : jar file");
        out.println(" -o (--output-format) [DELIMITED | STANDARD] : Output format");
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
