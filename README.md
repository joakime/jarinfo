JarInfo
=======

Simple utilty to dump multiple jar details in a machine readable way.

    $ java -jar jarinfo.jar -o DELIMITED mystery.jar

Results in

    mystery.jar|FILE|jarinfo-1.0-SNAPSHOT.jar
    mystery.jar|SIZE|12943
    mystery.jar|HASH_MD5|35afcc3998572e60d6e45fbfa57e32e5
    mystery.jar|HASH_SHA1|e0051f38f0063883e5d5d7e819cdc4faff82dea8
    mystery.jar|HASH_BYTECODE|f8ef079b27f7b68be4217e77b45e6020c56d6730
    mystery.jar|JDK|1.6
    mystery.jar|CLASS|net.erdfelt.util.jarinfo.JarAnalysis
    mystery.jar|CLASS|net.erdfelt.util.jarinfo.JarInfo$OutputFormat
    ...
    mystery.jar|CLASS|net.erdfelt.util.jarinfo.JarInfo
    mystery.jar|CLASS|net.erdfelt.util.jarinfo.StandardDumper
    mystery.jar|METHOD|net.erdfelt.util.jarinfo.DelimitedDumper.<init>()V
    mystery.jar|METHOD|net.erdfelt.util.jarinfo.DelimitedDumper.dump(Ljava/io/File;)V
    mystery.jar|METHOD|net.erdfelt.util.jarinfo.DelimitedDumper.OUT(Ljava/io/File;Ljava/lang/String;)V
    ...
    mystery.jar|METHOD|net.erdfelt.util.jarinfo.StandardDumper.<init>()V
    mystery.jar|METHOD|net.erdfelt.util.jarinfo.StandardDumper.dump(Ljava/io/File;)V
    mystery.jar|METHOD|net.erdfelt.util.jarinfo.StandardDumper.OUT(Ljava/lang/String;)V
    mystery.jar|FILE|META-INF/
    mystery.jar|FILE|META-INF/MANIFEST.MF
    ...
    mystery.jar|FILE|META-INF/maven/net.erdfelt.util/
    mystery.jar|FILE|META-INF/maven/net.erdfelt.util/jarinfo/

This was written back in 2006 to help in searching a large collection of anonymous jar files
for an eventual port to a maven build system.


