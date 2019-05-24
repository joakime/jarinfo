package net.erdfelt.util.jarinfo.analysis;

import java.nio.file.Path;
import java.util.Comparator;

public class PathComparator implements Comparator<Path>
{
    @Override
    public int compare(Path o1, Path o2)
    {
        return o1.toString().compareTo(o2.toString());
    }
}
