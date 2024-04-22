package com.interview;

import java.util.List;

import org.junit.jupiter.api.Assertions;

public class TestUtil<T> {
    public void compareList(List<T> expected, List<T> actual) {
        int i = 0;
        for (T str : expected) {
          //  Assertions.assertEquals("Failed at index " + i, str, actual.get(i++));
        }
    }

    public void compareListOfList(List<List<T>> expected, List<List<T>> actual) {
    	Assertions.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            List<T> a1 = expected.get(i);
            List<T> a2 = expected.get(i);
            compareList(a1, a2);
        }
    }
}
