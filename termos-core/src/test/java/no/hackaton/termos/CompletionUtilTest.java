package no.hackaton.termos;

import static java.util.Arrays.*;
import static no.hackaton.termos.CompletionUtil.*;
import static no.hackaton.termos.ReadLine.*;
import static org.junit.Assert.*;
import org.junit.*;

import java.util.ArrayList;
import java.util.*;

public class CompletionUtilTest {

    @Test
    public void testFindLongestMatch() {
        List<String> strings = new ArrayList<String>(Arrays.asList(
            "JMImplementation:type=MBeanServerDelegate",
            "com.sun.management:type=HotSpotDiagnostic",
            "java.lang:name=CMS Old Gen,type=MemoryPool",
            "java.lang:name=CMS Perm Gen,type=MemoryPool",
            "java.lang:name=Code Cache,type=MemoryPool",
            "java.lang:name=CodeCacheManager,type=MemoryManager",
            "java.lang:name=ConcurrentMarkSweep,type=GarbageCollector",
            "java.lang:name=Par Eden Space,type=MemoryPool",
            "java.lang:name=Par Survivor Space,type=MemoryPool",
            "java.lang:name=ParNew,type=GarbageCollector",
            "java.lang:type=ClassLoading",
            "java.lang:type=Compilation",
            "java.lang:type=Memory",
            "java.lang:type=OperatingSystem",
            "java.lang:type=Runtime",
            "java.lang:type=Threading",
            "java.util.logging:type=Logging"));
        assertEquals("", findLongestMatch(0, strings));

        strings.remove(0);
        strings.remove(0);
        assertEquals("java.", findLongestMatch(0, strings));
        assertEquals("java.", findLongestMatch(4, strings));
    }

    @Test
    public void testBasic() {
        String[] expected = new String[]{"aaa", "aaaa"};
        String[] input = new String[]{
            "aaa",
            "aaaa",
            "bbb"};
        assertEquals(asList(expected), completeStrings(asList(input), "a"));
    }
}
