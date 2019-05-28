package net.erdfelt.util.jarinfo.analysis;

public enum BytecodeVersion
{
    JAVA_1_1("1.1", 45),
    JAVA_1_2("1.2", 46),
    JAVA_1_3("1.3", 47),
    JAVA_1_4("1.4", 48),
    JAVA_5("5.0", 49),
    JAVA_6("6.0", 50),
    JAVA_7("7", 51),
    JAVA_8("8", 52),
    JAVA_9("9", 53),
    JAVA_10("10", 54),
    JAVA_11("11", 55),
    JAVA_12("12", 56),
    JAVA_13("13", 57);

    private String desc;
    private int classFormatVersion;

    BytecodeVersion(String desc, int classFormatVersion)
    {
        this.desc = desc;
        this.classFormatVersion = classFormatVersion;
    }

    public static BytecodeVersion from(int version)
    {
        for (BytecodeVersion bver : BytecodeVersion.values())
        {
            if (bver.classFormatVersion == version)
                return bver;
        }
        return null;
    }

    public String getDesc()
    {
        return desc;
    }

    public int getClassFormatVersion()
    {
        return classFormatVersion;
    }
}
