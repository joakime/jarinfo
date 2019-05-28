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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digester
{
    public static Digester MD5() throws IOException
    {
        try
        {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            return new Digester(MD5);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new IOException("Unable to initialize MD5 Digester", e);
        }
    }

    public static Digester SHA1() throws IOException
    {
        try
        {
            MessageDigest SHA1 = MessageDigest.getInstance("SHA1");
            return new Digester(SHA1);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new IOException("Unable to initialize SHA1 Digester", e);
        }
    }

    private static final int BUFFER_SIZE = 64 * 1024;
    private static final char[] hexcodes = "0123456789abcdef".toCharArray();

    private final MessageDigest messageDigest;

    public Digester(MessageDigest messageDigest)
    {
        this.messageDigest = messageDigest;
    }

    public String calc(Path file) throws IOException
    {
        synchronized (messageDigest)
        {
            try (InputStream fileInput = Files.newInputStream(file);)
            {
                messageDigest.reset();
                byte[] buffer = new byte[BUFFER_SIZE];
                int size;
                while (((size = fileInput.read(buffer, 0, BUFFER_SIZE)) >= 0))
                {
                    messageDigest.update(buffer, 0, size);
                }
                return asHex(messageDigest.digest());
            }
        }
    }

    public String getHashID()
    {
        return messageDigest.getAlgorithm();
    }

    private String asHex(byte buffer[])
    {
        int len = buffer.length;
        char out[] = new char[len * 2];
        for (int i = 0; i < len; i++)
        {
            out[i * 2] = hexcodes[(buffer[i] & 0xF0) >> 4];
            out[(i * 2) + 1] = hexcodes[(buffer[i] & 0x0F)];
        }
        return String.valueOf(out);
    }
}
