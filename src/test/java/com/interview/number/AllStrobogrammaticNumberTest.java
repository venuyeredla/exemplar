package com.interview.number;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AllStrobogrammaticNumberTest {

    @Test
    public void testDifferentCases() {
        AllStrobogrammaticNumber allStrobogrammaticNumber = new AllStrobogrammaticNumber();
        Assertions.assertEquals(19, allStrobogrammaticNumber.strobogrammaticInRange("0", "1000"));
        Assertions.assertEquals(34171, allStrobogrammaticNumber.strobogrammaticInRange("1011010", "2210101121121"));

    }
}
