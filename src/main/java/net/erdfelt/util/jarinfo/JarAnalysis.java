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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.shared.jar.JarAnalyzer;
import org.apache.maven.shared.jar.classes.JarClassesAnalysis;
import org.apache.maven.shared.jar.identification.JarIdentificationAnalysis;
import org.apache.maven.shared.jar.identification.JarIdentificationExposer;
import org.apache.maven.shared.jar.identification.exposers.EmbeddedMavenModelExposer;
import org.apache.maven.shared.jar.identification.exposers.FilenameExposer;
import org.apache.maven.shared.jar.identification.exposers.JarClassesExposer;
import org.apache.maven.shared.jar.identification.exposers.ManifestExposer;
import org.apache.maven.shared.jar.identification.exposers.TextFileExposer;
import org.apache.maven.shared.jar.identification.exposers.TimestampExposer;
import org.apache.maven.shared.jar.identification.hash.JarBytecodeHashAnalyzer;
import org.codehaus.plexus.digest.Digester;
import org.codehaus.plexus.digest.DigesterException;
import org.codehaus.plexus.digest.Md5Digester;
import org.codehaus.plexus.digest.Sha1Digester;
import org.codehaus.plexus.digest.StreamingSha1Digester;

/**
 * Immutable JarAnalysis
 * 
 * @author Joakim Erdfelt
 * 
 * @todo java imports?
 * @todo jar entry date/time size?
 * @todo jar entry bytecode hash?
 * @todo jar identification with maven repo? (likelyhood: official, likely, calculated)
 */
public class JarAnalysis extends JarAnalyzer
{
    public static final String MD5 = "md5";
    public static final String SHA1 = "sha1";
    public static final String BYTECODE = "bytecode";

    private Map<String, String> hashes = new HashMap<String, String>();

    public JarAnalysis(File file) throws IOException
    {
        // Default Processing
        super(file);

        // Hash Processing
        setHash(file,MD5,new Md5Digester());
        setHash(file,SHA1,new Sha1Digester());

        JarBytecodeHashAnalyzer bytecodeHash = new JarBytecodeHashAnalyzer();
        bytecodeHash.setDigester(new StreamingSha1Digester());
        bytecodeHash.computeHash(this);

        this.hashes.put(BYTECODE,this.getJarData().getBytecodeHash());

        // Class Processing
        JarClassesAnalysis classesAnalysis = new JarClassesAnalysis();
        classesAnalysis.analyze(this);

        // Identification Processing
        List<JarIdentificationExposer> exposers = new ArrayList<JarIdentificationExposer>();
        exposers.add(new EmbeddedMavenModelExposer());
        exposers.add(new ManifestExposer());
        exposers.add(new FilenameExposer());
        exposers.add(new TextFileExposer());
        exposers.add(new TimestampExposer());
        JarClassesExposer jarClassesExposer = new JarClassesExposer();
        jarClassesExposer.setAnalyzer(classesAnalysis);
        exposers.add(jarClassesExposer);

        JarIdentificationAnalysis identificationAnalysis = new JarIdentificationAnalysis();
        identificationAnalysis.setExposers(exposers);

        identificationAnalysis.analyze(this);
    }

    private void setHash(File file, String key, Digester digester)
    {
        String hash;
        try
        {
            hash = digester.calc(file);
        }
        catch (DigesterException e)
        {
            hash = "-"; // error condition
        }
        this.hashes.put(key,hash);
    }

    public String getHash(String key)
    {
        return hashes.get(key);
    }
}
