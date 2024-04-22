package com.interview.suffixprefix;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TrieTest {

    @Test
    public void testDifferentCases() {
        Trie trie = new Trie();

        trie.insert("abcd");
        trie.insert("abgl");
        trie.insertRecursive("lmn");
        trie.insertRecursive("lmnpq");
        trie.insert("cdeg");
        trie.insert("ghijk");

        Assertions.assertFalse(trie.search("ab"));
        Assertions.assertFalse(trie.search("abc"));
        Assertions.assertTrue(trie.search("abcd"));
        Assertions.assertFalse(trie.search("abg"));
        Assertions.assertTrue(trie.search("abgl"));
        Assertions.assertFalse(trie.search("lm"));
        Assertions.assertTrue(trie.search("lmn"));
        Assertions.assertFalse(trie.search("lmnp"));
        Assertions.assertTrue(trie.search("lmnpq"));

        trie.delete("abcd");
        Assertions.assertTrue(trie.search("abgl"));
        Assertions.assertFalse(trie.search("abcd"));

        trie.delete("lmn");
        Assertions.assertFalse(trie.search("lmn"));
        Assertions.assertTrue(trie.search("lmnpq"));

        trie.delete("lmnpq");
        Assertions.assertFalse(trie.search("lmnpq"));

    }
}
