package net.erdfelt.util.jarinfo.analysis;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class JarReference
{
    private Set<String> groupId = new TreeSet<>();
    private Set<String> artifactId = new TreeSet<>();
    private Set<String> version = new TreeSet<>();

    private Set<String> jpmsModuleName = new TreeSet<>();
    private String osgiSymbolicName;

    private Set<String> name = new TreeSet<>();
    private Set<String> vendor = new TreeSet<>();

    public void addGroupId(String value)
    {
        if (value == null)
            return;
        groupId.add(value);
    }

    public void addArtifactId(String value)
    {
        if (value == null)
            return;
        artifactId.add(value);
    }

    public void addName(String value)
    {
        if (value == null)
            return;
        name.add(value);
    }

    public void addVendor(String value)
    {
        if (value == null)
            return;
        vendor.add(value);
    }

    public void addVersion(String value)
    {
        if (value == null)
            return;
        version.add(value);
    }

    public void addJpmsModuleName(String value)
    {
        if (value == null)
            return;
        jpmsModuleName.add(value);
    }

    public void setOsgiSymbolicName(String value)
    {
        if (value == null)
            return;
        osgiSymbolicName = value;
    }

    public Set<String> getGroupId()
    {
        return groupId;
    }

    public Set<String> getArtifactId()
    {
        return artifactId;
    }

    public Set<String> getVersion()
    {
        return version;
    }

    public Set<String> getJpmsModuleName()
    {
        return jpmsModuleName;
    }

    public String getOsgiSymbolicName()
    {
        return osgiSymbolicName;
    }

    public Set<String> getName()
    {
        return name;
    }

    public Set<String> getVendor()
    {
        return vendor;
    }
}
