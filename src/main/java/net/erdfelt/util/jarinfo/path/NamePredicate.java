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
package net.erdfelt.util.jarinfo.path;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Pattern;

public class NamePredicate implements PathPredicate
{
    private final Pattern namePattern;

    public NamePredicate(String regexPattern)
    {
        this.namePattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean test(Path path, BasicFileAttributes basicFileAttributes)
    {
        String name = getName(path);
        return namePattern.matcher(name).matches();
    }

    public static String getName(Path path)
    {
        String name = path.getFileName().toString();
        if (Files.isDirectory(path))
        {
            int nameCount = path.getNameCount();
            if (nameCount == 0)
                return ""; // we are on path "/"

            name = path.getName(path.getNameCount() - 1).toString();
            if (name.endsWith(File.separator))
                name = name.substring(0, name.length() - 1);
        }
        return name;
    }
}
