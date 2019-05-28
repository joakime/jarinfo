package net.erdfelt.util.jarinfo.analysis;

import org.objectweb.asm.Opcodes;

public class AccessModifiers
{
    private static final Ref[] MAPPINGS = new Ref[]{
        new Ref(Opcodes.ACC_PUBLIC, "public "),
        new Ref(Opcodes.ACC_PRIVATE, "private "),
        new Ref(Opcodes.ACC_PROTECTED, "protected "),
        new Ref(Opcodes.ACC_STATIC, "static "),
        new Ref(Opcodes.ACC_FINAL, "final "),
        new Ref(Opcodes.ACC_SYNCHRONIZED, "synchronized "),
        new Ref(Opcodes.ACC_OPEN, "open "), // module
        new Ref(Opcodes.ACC_TRANSITIVE, "transitive "), // module
        new Ref(Opcodes.ACC_VOLATILE, "volatile "),
        new Ref(Opcodes.ACC_BRIDGE, "bridge "), // method??
        new Ref(Opcodes.ACC_STATIC_PHASE, "static-phase "), // module
        new Ref(Opcodes.ACC_VARARGS, "... "),
        new Ref(Opcodes.ACC_TRANSIENT, "transient "),
        new Ref(Opcodes.ACC_NATIVE, "native "),
        new Ref(Opcodes.ACC_INTERFACE, "interface "),
        new Ref(Opcodes.ACC_ABSTRACT, "abstract "),
        new Ref(Opcodes.ACC_STRICT, "strict "),
        new Ref(Opcodes.ACC_SYNTHETIC, "synthetic "),
        new Ref(Opcodes.ACC_ANNOTATION, "annotation "),
        new Ref(Opcodes.ACC_ENUM, "enum "),
        new Ref(Opcodes.ACC_MANDATED, "mandated "), // module
        new Ref(Opcodes.ACC_MODULE, "module "), // module
    };

    public static void appendModifierWords(StringBuilder sig, int access)
    {
        for (Ref ref : MAPPINGS)
        {
            if ((access & ref.bits) != 0)
                sig.append(ref.modifier);
        }
    }

    private static class Ref
    {
        int bits;
        String modifier;

        public Ref(int accessBits, String modifierWord)
        {
            this.bits = accessBits;
            this.modifier = modifierWord;
        }
    }
}
