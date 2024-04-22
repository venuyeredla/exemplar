package com.interview.array;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThreeSumSmallerThanTargetTest {

    @Test
    public void testDifferentCases() {
        ThreeSumSmallerThanTarget threeSumSmallerThanTarget = new ThreeSumSmallerThanTarget();
        int[] input = {-2, 0, 1, 3};
        Assertions.assertEquals(2, threeSumSmallerThanTarget.threeSumSmaller(input, 2));
    }
}
