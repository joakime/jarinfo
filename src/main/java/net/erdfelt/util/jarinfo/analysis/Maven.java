package net.erdfelt.util.jarinfo.analysis;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.jar.Attributes;

import net.erdfelt.util.jarinfo.path.FilePredicate;
import net.erdfelt.util.jarinfo.path.NamePredicate;

public class Maven
{
    /* Example of Maven generated MANIFEST.MF entries
     *
     * Created-By: Apache Maven Bundle Plugin
     * Built-By: joakim
     * Build-Jdk: 11.0.2
     * Originally-Created-By: Apache Maven Bundle Plugin
     */
    public static final Attributes.Name CREATED_BY = new Attributes.Name("Created-By");
    public static final Attributes.Name BUILT_BY = new Attributes.Name("Built-By");
    public static final Attributes.Name BUILD_JDK = new Attributes.Name("Build-Jdk");
    public static final Attributes.Name ORIGINALLY_CREATED_BY = new Attributes.Name("Originally-Created-By");

    public static BiPredicate<Path, BasicFileAttributes> newPomPropsPredicate()
    {
        return new FilePredicate().and(new NamePredicate("^pom\\.properties$"));
    }
}
