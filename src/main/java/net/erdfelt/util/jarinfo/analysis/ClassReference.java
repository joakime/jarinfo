package net.erdfelt.util.jarinfo.analysis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import net.erdfelt.util.jarinfo.Digester;
import org.objectweb.asm.tree.MethodNode;

public class ClassReference
{
    private final Path path;
    private String className;
    private BytecodeVersion bytecodeVersion;
    private String hash;
    private List<String> methodSignatures = new ArrayList<>();
    private List<Throwable> errors = new ArrayList<>();

    public ClassReference(Path path)
    {
        this.path = path;
        try
        {
            this.hash = Digester.SHA1().calc(path);
        }
        catch (IOException e)
        {
            this.hash = "-";
        }
    }

    public void addError(Throwable cause)
    {
        this.errors.add(cause);
    }

    public void addMethod(String methodSignature)
    {
        this.methodSignatures.add(methodSignature);
    }

    public void addMethod(MethodNode methodNode)
    {
        StringBuilder sig = new StringBuilder();

        AccessModifiers.appendModifierWords(sig, methodNode.access);

        sig.append(className).append("#");
        sig.append(methodNode.name);
        sig.append(methodNode.desc);

        this.methodSignatures.add(sig.toString());
    }

    public BytecodeVersion getBytecodeVersion()
    {
        return bytecodeVersion;
    }

    public void setBytecodeVersion(BytecodeVersion version)
    {
        this.bytecodeVersion = version;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String name)
    {
        this.className = name.replaceAll("/", ".");
    }

    public List<Throwable> getErrors()
    {
        return errors;
    }

    public String getHash()
    {
        return hash;
    }

    public List<String> getMethodSignatures()
    {
        return methodSignatures;
    }

    public Path getPath()
    {
        return path;
    }
}
