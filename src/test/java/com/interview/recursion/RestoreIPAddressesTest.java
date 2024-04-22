package com.interview.recursion;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.interview.TestUtil;

public class RestoreIPAddressesTest {

    @Test
    public void testDifferenceCases() {
        RestoreIPAddresses restoreIPAddresses = new RestoreIPAddresses();
        List<String> result = restoreIPAddresses.restoreIpAddresses("25525511135");
        List<String> expected = new ArrayList<>();
        expected.add("255.255.11.135");
        expected.add("255.255.111.35");
        TestUtil<String> t = new TestUtil<>();
        t.compareList(expected, result);

        List<String> result1 = restoreIPAddresses.restoreIpAddresses("0000");
        expected = new ArrayList<>();
        expected.add("0.0.0.0");
        t.compareList(expected, result1);
    }
}
