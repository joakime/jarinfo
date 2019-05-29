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
package net.erdfelt.util.jarinfo.analysis;

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

    private Set<String> builtBy = new TreeSet<>();
    private Set<String> buildJdk = new TreeSet<>();
    private Set<String> createdBy = new TreeSet<>();

    public void addArtifactId(String value)
    {
        if (value == null)
            return;
        artifactId.add(value);
    }

    public void addBuildJdk(String value)
    {
        if(value == null)
            return;
        buildJdk.add(value);
    }

    public void addBuiltBy(String value)
    {
        if(value == null)
            return;
        builtBy.add(value);
    }

    public void addCreatedBy(String value)
    {
        if(value == null)
            return;
        createdBy.add(value);
    }

    public void addGroupId(String value)
    {
        if (value == null)
            return;
        groupId.add(value);
    }

    public void addJpmsModuleName(String value)
    {
        if (value == null)
            return;
        jpmsModuleName.add(value);
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

    public Set<String> getArtifactId()
    {
        return artifactId;
    }

    public Set<String> getBuildJdk()
    {
        return buildJdk;
    }

    public Set<String> getBuiltBy()
    {
        return builtBy;
    }

    public Set<String> getCreatedBy()
    {
        return createdBy;
    }

    public Set<String> getGroupId()
    {
        return groupId;
    }

    public Set<String> getJpmsModuleName()
    {
        return jpmsModuleName;
    }

    public Set<String> getName()
    {
        return name;
    }

    public String getOsgiSymbolicName()
    {
        return osgiSymbolicName;
    }

    public void setOsgiSymbolicName(String value)
    {
        if (value == null)
            return;
        osgiSymbolicName = value;
    }

    public Set<String> getVendor()
    {
        return vendor;
    }

    public Set<String> getVersion()
    {
        return version;
    }
}
