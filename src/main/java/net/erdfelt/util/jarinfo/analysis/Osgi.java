package net.erdfelt.util.jarinfo.analysis;

import java.util.jar.Attributes;

public class Osgi
{
    /* Example META-INF/MANIFEST.MF from jetty-util 9.4.18
     *
     *  Bnd-LastModified: 1556570532075
     *  Bundle-Classpath: .
     *  Bundle-Copyright: Copyright (c) 2008-2019 Mort Bay Consulting Pty. Ltd.
     *  Bundle-Description: Jetty module for Jetty :: Utilities
     *  Bundle-DocURL: http://www.eclipse.org/jetty
     *  Bundle-License: http://www.apache.org/licenses/LICENSE-2.0, http://www.e
     *   clipse.org/org/documents/epl-v10.php
     *  Bundle-ManifestVersion: 2
     *  Bundle-Name: Jetty :: Utilities
     *  Bundle-RequiredExecutionEnvironment: JavaSE-1.8
     *  Bundle-SymbolicName: org.eclipse.jetty.util
     *  Bundle-Vendor: Eclipse Jetty Project
     *  Bundle-Version: 9.4.18.v20190429
     */

    public static final Attributes.Name BUNDLE_COPYRIGHT = new Attributes.Name("Bundle-Copyright");
    public static final Attributes.Name BUNDLE_DESCRIPTION = new Attributes.Name("Bundle-Description");
    public static final Attributes.Name BUNDLE_LICENSE = new Attributes.Name("Bundle-License");
    public static final Attributes.Name BUNDLE_SYMBOLIC_NAME = new Attributes.Name("Bundle-SymbolicName");
    public static final Attributes.Name BUNDLE_NAME = new Attributes.Name("Bundle-Name");
    public static final Attributes.Name BUNDLE_VERSION = new Attributes.Name("Bundle-Version");
    public static final Attributes.Name BUNDLE_VENDOR = new Attributes.Name("Bundle-Vendor");
}
