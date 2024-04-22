package com.interview.bits;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MaxProductWordLengthTest {

    @Test
    public void testDifferentCases() {
        MaxProductWordLength maxProductWordLength = new MaxProductWordLength();
        String[] words1 = {"abcw", "baz", "foo", "bar", "xtfn", "abcdef"};
        Assertions.assertEquals(16, maxProductWordLength.maxProduct(words1));

        String[] words2 = {"a", "ab", "abc", "d", "cd", "bcd", "abcd"};
        Assertions.assertEquals(4, maxProductWordLength.maxProduct(words2));
    }
}
