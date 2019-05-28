package net.erdfelt.util.jarinfo.analysis;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.MethodNode;

public class ClassReference
{
    private final Path path;
    private String className;
    private BytecodeVersion bytecodeVersion;
    private List<String> methodSignatures = new ArrayList<>();
    private List<Throwable> errors = new ArrayList<>();

    public ClassReference(Path path)
    {
        this.path = path;
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

    public void setBytecodeVersion(BytecodeVersion version)
    {
        this.bytecodeVersion = version;
    }

    public void setClassName(String name)
    {
        this.className = name.replaceAll("/", ".");
    }

    public String getClassName()
    {
        return className;
    }

    public BytecodeVersion getBytecodeVersion()
    {
        return bytecodeVersion;
    }

    public Path getPath()
    {
        return path;
    }

    public List<Throwable> getErrors()
    {
        return errors;
    }

    public List<String> getMethodSignatures()
    {
        return methodSignatures;
    }
}
