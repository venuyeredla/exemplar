package com.interview.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidPalindromeTest {

    @Test
    public void testDifferentCases() {
        ValidPalindrome validPalindrome = new ValidPalindrome();
        Assertions.assertTrue(validPalindrome.isPalindrome("A man, a plan, a canal: Panama"));
        Assertions.assertFalse(validPalindrome.isPalindrome("race a car"));
    }
}
