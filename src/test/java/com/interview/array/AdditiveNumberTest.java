package com.interview.array;

import org.junit.jupiter.api.Assertions;

public class AdditiveNumberTest {
    public static void main(String args[]) {
        AdditiveNumber additiveNumber = new AdditiveNumber();
        Assertions.assertTrue(additiveNumber.isAdditiveNumber("12351174"));
        Assertions.assertFalse(additiveNumber.isAdditiveNumber("1023"));
        Assertions.assertTrue(additiveNumber.isAdditiveNumber("198019823962"));
    }
}
