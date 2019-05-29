package net.erdfelt.util.jarinfo;

public class DumperFactory
{
    public static Dumper getDumper(String dumperHint)
    {
        switch (dumperHint)
        {
            case "standard":
                return new StandardDumper();
            case "delimited":
                return new DelimitedDumper();
            default:
                throw new RuntimeException("Unknown dumper type: " + dumperHint);
        }
    }
}
