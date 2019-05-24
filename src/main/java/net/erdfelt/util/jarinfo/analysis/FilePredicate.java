package net.erdfelt.util.jarinfo.analysis;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FilePredicate implements PathPredicate
{
    @Override
    public boolean test(Path path, BasicFileAttributes basicFileAttributes)
    {
        return Files.isRegularFile(path);
    }
}
