package com.interview.array;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MultiplyAllFieldsExceptOwnPositionTest {

    @Test
    public void testDifferentCases() {
        MultiplyAllFieldsExceptOwnPosition mop = new MultiplyAllFieldsExceptOwnPosition();
        int[] input1 = {0, 9, -2};
        int[] output1 = {-18, 0, 0};
        Assertions.assertArrayEquals(output1, mop.multiply(input1));

        int[] input2 = {1, 1};
        int[] output2 = {1, 1};
        Assertions.assertArrayEquals(output2, mop.multiply(input2));

        int[] input3 = {3, 1, -3, 6};
        int[] output3 = {-18, -54, 18, -9};
        Assertions.assertArrayEquals(output3, mop.multiply(input3));
    }
}
