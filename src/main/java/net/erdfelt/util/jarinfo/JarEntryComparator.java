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

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;
import java.util.jar.JarEntry;

/**
 * JarEntryComparator
 * 
 * @author Joakim Erdfelt
 */
class JarEntryComparator implements Comparator<JarEntry>
{
    private Collator collator = Collator.getInstance();

    public int compare(JarEntry j1, JarEntry j2)
    {
        CollationKey key1 = collator.getCollationKey(j1.getName());
        CollationKey key2 = collator.getCollationKey(j2.getName());
        return key1.compareTo(key2);
    }
}