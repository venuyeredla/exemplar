package com.interview.array;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberOfTriangledInUnsortedArrayTest {

    @Test
    public void testSimpleCase() {
        NumberOfTrianglesInUnsortedArray numberOfTrianglesInUnsortedArray = new NumberOfTrianglesInUnsortedArray();
        int[] input = {3, 4, 5, 6, 8, 9, 15};
        int result = numberOfTrianglesInUnsortedArray.numberOfTriangles(input);
        Assertions.assertEquals(15, result);
    }
}
