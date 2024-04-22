package com.interview.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LongestSubstringWithKDistinctCharactersTest {

    @Test
    public void testDifferenceCases() {
        LongestSubstringWithKDistinctCharacters longestSubstringWithKDistinctCharacters = new LongestSubstringWithKDistinctCharacters();
        Assertions.assertEquals(3, longestSubstringWithKDistinctCharacters.lengthOfLongestSubstringKDistinct("eceba", 2));
        Assertions.assertEquals(1, longestSubstringWithKDistinctCharacters.lengthOfLongestSubstringKDistinct("aba", 1));
        Assertions.assertEquals(5, longestSubstringWithKDistinctCharacters.lengthOfLongestSubstringKDistinct("caebegcle", 4));
        Assertions.assertEquals(0, longestSubstringWithKDistinctCharacters.lengthOfLongestSubstringKDistinct("eceba", 0));
        Assertions.assertEquals(3, longestSubstringWithKDistinctCharacters.lengthOfLongestSubstringKDistinctUsingMap("eceba", 2));
        Assertions.assertEquals(1, longestSubstringWithKDistinctCharacters.lengthOfLongestSubstringKDistinctUsingMap("aba", 1));
        Assertions.assertEquals(5, longestSubstringWithKDistinctCharacters.lengthOfLongestSubstringKDistinctUsingMap("caebegcle", 4));
        Assertions.assertEquals(0, longestSubstringWithKDistinctCharacters.lengthOfLongestSubstringKDistinctUsingMap("eceba", 0));
    }
}
