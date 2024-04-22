package com.interview.number;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BasicCalculatorTest {

    @Test
    public void testDifferentCases() {
        BasicCalculator basicCalculator = new BasicCalculator();
        Assertions.assertEquals(0, basicCalculator.calculate("0"));
        Assertions.assertEquals(9, basicCalculator.calculate("0 +  9"));
        Assertions.assertEquals(19, basicCalculator.calculate("1 + 9 * 2"));
        Assertions.assertEquals(15, basicCalculator.calculate("3 + 9/2 * 3"));
        Assertions.assertEquals(6, basicCalculator.calculate("8 -2 + 3/ 5  "));

    }
}
