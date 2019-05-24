package net.erdfelt.util.jarinfo.analysis;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;

public interface PathPredicate extends BiPredicate<Path, BasicFileAttributes>
{
}
