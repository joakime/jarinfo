package net.erdfelt.util.jarinfo.analysis;

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
