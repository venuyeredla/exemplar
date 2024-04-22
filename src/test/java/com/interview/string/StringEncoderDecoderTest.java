package com.interview.string;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.interview.TestUtil;

public class StringEncoderDecoderTest {

    @Test
    public void testDifferentCases() {
        StringEncoderDecoder stringEncoderDecoder = new StringEncoderDecoder();
        List<String> input = new ArrayList<>();
        input.add("Tushar");
        input.add("Roy");
        input.add("");
        String encoded = stringEncoderDecoder.encode(input);
        List<String> result = stringEncoderDecoder.decode(encoded);
        TestUtil<String> testUtil = new TestUtil();
        testUtil.compareList(input, result);
    }
}
