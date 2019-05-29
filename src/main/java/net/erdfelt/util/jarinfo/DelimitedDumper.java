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
package net.erdfelt.util.jarinfo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import net.erdfelt.util.jarinfo.analysis.BytecodeVersion;
import net.erdfelt.util.jarinfo.analysis.ClassReference;
import net.erdfelt.util.jarinfo.analysis.JarAnalyzer;
import net.erdfelt.util.jarinfo.analysis.JarReference;

/**
 * DelimitedDumper
 */
public class DelimitedDumper implements Dumper
{
    @Override
    public void dump(JarAnalyzer analyzer) throws Exception
    {
        OUT(analyzer, "FILE|" + analyzer.getPath().getFileName());
        OUT(analyzer, "SIZE|" + analyzer.getFileSize());

        JarReference jarReference = analyzer.getReference();

        jarReference.getGroupId().forEach((groupId) -> OUT(analyzer, "GROUP_ID|" + groupId));
        jarReference.getArtifactId().forEach((artifactId) -> OUT(analyzer, "ARTIFACT_ID|" + artifactId));
        jarReference.getVersion().forEach((version) -> OUT(analyzer, "VERSION|" + version));

        jarReference.getJpmsModuleName().forEach((name) -> OUT(analyzer, "JPMS_MODULE_NAME|" + name));
        if (jarReference.getOsgiSymbolicName() != null)
        {
            OUT(analyzer, "OSGI_SYMBOLIC_NAME|" + jarReference.getOsgiSymbolicName());
        }
        jarReference.getName().forEach((name) -> OUT(analyzer, "NAME|" + name));
        jarReference.getVendor().forEach((vendor) -> OUT(analyzer, "VENDOR|" + vendor));

        jarReference.getBuiltBy().forEach((builtBy) -> OUT(analyzer, "BUILT_BY|" + builtBy));
        jarReference.getBuildJdk().forEach((buildJdk) -> OUT(analyzer, "BUILD_JDK|" + buildJdk));
        jarReference.getCreatedBy().forEach((createdBy) -> OUT(analyzer, "CREATED_BY|" + createdBy));

        OUT(analyzer, "HASH_MD5|" + analyzer.getHash(JarAnalyzer.HashType.MD5));
        OUT(analyzer, "HASH_SHA1|" + analyzer.getHash(JarAnalyzer.HashType.SHA1));
        OUT(analyzer, "HASH_BYTECODE|" + analyzer.getHash(JarAnalyzer.HashType.BYTECODE));

        List<ClassReference> classReferences = analyzer.getClassReferences();

        classReferences.stream().map(classReference -> classReference.getBytecodeVersion())
            .max(Comparator.comparingInt(BytecodeVersion::getClassFormatVersion))
            .ifPresent((max) -> OUT(analyzer, "JDK|" + max.getDesc()));

        for (ClassReference classRef : classReferences)
        {
            OUT(analyzer, "CLASS|" + classRef.getClassName());
        }

        for (ClassReference classRef : classReferences)
        {
            classRef.getMethodSignatures().forEach((signature) -> OUT(analyzer, "METHOD|" + signature));
        }

        List<Path> entries = analyzer.getAll();
        for (Path entry : entries)
        {
            if (Files.isDirectory(entry))
                OUT(analyzer, "DIR|" + entry.toString());
            else
                OUT(analyzer, "FILE|" + entry.toString());
        }
    }

    private void OUT(JarAnalyzer analyzer, String msg)
    {
        System.out.println(analyzer.getPath().toString() + "|" + msg);
    }
}
