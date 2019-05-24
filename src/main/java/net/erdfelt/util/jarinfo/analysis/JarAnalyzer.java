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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiPredicate;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.erdfelt.util.jarinfo.Digester;

public class JarAnalyzer implements AutoCloseable
{
    private final Path jarFile;
    private final FileSystem zipFileSystem;
    private final Path rootZipPath;

    private Map<String, String> hashes = new HashMap<String, String>();

    public JarAnalyzer(Path jarfile) throws IOException
    {
        this.jarFile = jarfile;

        setHash(jarFile, Digester.MD5());
        setHash(jarFile, Digester.SHA1());

        URI zipFsUri = toZipFsUri(jarfile);

        Map<String, Object> env = new HashMap<>();
        env.put("multi-release", "8");

        zipFileSystem = FileSystems.newFileSystem(zipFsUri, env);

        // assume only 1 root for ZipFS
        rootZipPath = zipFileSystem.getPath("/");
    }

    private final URI toZipFsUri(Path jarFile)
    {
        URI uri = jarFile.toAbsolutePath().toUri();
        String asciiUri = uri.toASCIIString();

        if (asciiUri.equalsIgnoreCase("jar:file:") &&
            asciiUri.endsWith(".jar"))
        {
            // we have what we need already.
            return uri;
        }

        if ("file".equalsIgnoreCase(uri.getScheme()))
        {
            if (asciiUri.endsWith(".jar"))
            {
                return URI.create("jar:" + asciiUri);
            }
        }

        throw new UnsupportedOperationException(JarAnalyzer.class.getSimpleName() + " only supports [jar:file:/] or [file:/] URIs that end with [.jar] - " + uri);
    }

    public JarReference getReference() throws IOException
    {
        JarReference jarReference = new JarReference();

        Path metaInfDir = resolveWellKnownDirectory(rootZipPath, "META-INF");

        if (metaInfDir != null)
        {
            // Walk Manifest
            Manifest manifest = loadManifest(metaInfDir);

            if (manifest != null)
            {
                addFromManifestAttributes(jarReference, manifest.getMainAttributes());

                Map<String, Attributes> entries = manifest.getEntries();
                for (Map.Entry<String, Attributes> entry : entries.entrySet())
                {
                    Attributes attributes = entry.getValue();
                    addFromManifestAttributes(jarReference, attributes);
                }
            }

            // Walk Maven Project Properties file
            Path metainfMaven = resolveWellKnownDirectory(metaInfDir, "maven");
            if (Files.exists(metainfMaven) && Files.isDirectory(metainfMaven))
            {
                BiPredicate<Path, BasicFileAttributes> pathPredicate = new FilePredicate()
                    .and(new NamePredicate("^pom\\.properties$"));
                try (Stream<Path> finder = Files.find(metainfMaven, 20, pathPredicate))
                {
                    finder.sorted(new PathComparator())
                        .forEach((pomPath) ->
                        {
                            Path propPath = metainfMaven.resolve(pomPath);
                            try (InputStream inputStream = Files.newInputStream(propPath))
                            {
                                Properties props = new Properties();
                                props.load(inputStream);

                                jarReference.addGroupId(props.getProperty("groupId"));
                                jarReference.addArtifactId(props.getProperty("artifactId"));
                                jarReference.addVersion(props.getProperty("version"));
                            }
                            catch (IOException e)
                            {
                                throw new RuntimeException("Unable to load file: " + propPath, e);
                            }
                        });
                }
            }
        }

        // Walk JPMS module-info.class

        return jarReference;
    }

    private void addFromManifestAttributes(JarReference jarReference, Attributes attributes)
    {
        jarReference.addName(attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE));
        jarReference.addVersion(attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION));
        jarReference.addVendor(attributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR));

        jarReference.addName(attributes.getValue(Attributes.Name.SPECIFICATION_TITLE));
        jarReference.addVersion(attributes.getValue(Attributes.Name.SPECIFICATION_VERSION));
        jarReference.addVendor(attributes.getValue(Attributes.Name.SPECIFICATION_VENDOR));

        jarReference.addGroupId(attributes.getValue(Attributes.Name.EXTENSION_NAME));

        // OSGi
        jarReference.addName(attributes.getValue(Osgi.BUNDLE_NAME));
        jarReference.addVersion(attributes.getValue(Osgi.BUNDLE_VERSION));
        jarReference.addVendor(attributes.getValue(Osgi.BUNDLE_VENDOR));
        jarReference.setOsgiSymbolicName(attributes.getValue(Osgi.BUNDLE_SYMBOLIC_NAME));

        // JMPS
        jarReference.addJpmsModuleName(attributes.getValue(Jpms.AUTOMATIC_MODULE_NAME));
    }

    private Manifest loadManifest(Path metaInfDir) throws IOException
    {
        List<Path> manifestPaths = resolve(metaInfDir, "MANIFEST.MF");

        if (manifestPaths.isEmpty())
        {
            return null;
        }

        if (manifestPaths.size() > 1)
        {
            throw new IOException("Bad JAR file: has more then one META-INF/MANIFEST.MF file present");
        }

        Path manifestPath = manifestPaths.get(0);

        try (InputStream manifestInputStream = Files.newInputStream(manifestPath))
        {
            return new Manifest(manifestInputStream);
        }
    }

    private List<Path> resolve(Path path, String reference) throws IOException
    {
        return Files.list(path)
            .filter((ref) ->
            {
                String lastName = NamePredicate.getName(ref);
                return lastName.equalsIgnoreCase(reference);
            })
            .sorted(new PathComparator())
            .collect(Collectors.toList());
    }

    private Path resolveWellKnownDirectory(Path path, String reference) throws IOException
    {
        Path expected = path.resolve(reference);

        List<Path> paths = resolve(path, reference);
        if (paths.isEmpty())
            return null;

        if (paths.size() > 1)
            throw new IOException("Bad JAR File: has more then one " + expected + " directory present");

        Path dir = paths.get(0);
        if (!Files.isDirectory(dir))
            throw new IOException("Bad JAR File: path is not directory: " + dir);

        return dir.toAbsolutePath();
    }

    public List<Path> getClasses() throws IOException
    {
        BiPredicate<Path, BasicFileAttributes> pathPredicate = new FilePredicate()
            .and(new NamePredicate("^.*\\.class$"));
        try (Stream<Path> finder = Files.find(rootZipPath, 20, pathPredicate))
        {
            return finder.sorted(new PathComparator()).collect(Collectors.toList());
        }
    }

    public List<Path> getAll() throws IOException
    {
        BiPredicate<Path, BasicFileAttributes> pathPredicate = new FilePredicate();
        try (Stream<Path> finder = Files.find(rootZipPath, 20, pathPredicate))
        {
            return finder.sorted(new PathComparator()).collect(Collectors.toList());
        }
    }

    private void setHash(Path file, Digester digester)
    {
        String hash;
        try
        {
            hash = digester.calc(file);
        }
        catch (IOException e)
        {
            hash = "-"; // error condition
        }
        this.hashes.put(digester.getHashID(), hash);
    }

    public String getHash(String key)
    {
        return hashes.get(key);
    }

    @Override
    public void close() throws IOException
    {
        zipFileSystem.close();
    }
}
