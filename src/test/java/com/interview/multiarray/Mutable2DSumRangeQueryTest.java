package com.interview.multiarray;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Mutable2DSumRangeQueryTest {

    @Test
    public void testDifferentCases() {
        int[][] input = {{2, 3, 6}, {-1, 2, 4}, {-3, 2, 5}};
        Mutable2DSumRangeQuery mutable2DSumRangeQuery = new Mutable2DSumRangeQuery(input);
        int total = mutable2DSumRangeQuery.sumRegion(1, 1, 2, 2);
        Assertions.assertEquals(13, total);

        total = mutable2DSumRangeQuery.sumRegion(0, 1, 2, 1);
        Assertions.assertEquals(7, total);

        mutable2DSumRangeQuery.update(1, 1, 4);
        total = mutable2DSumRangeQuery.sumRegion(1, 1, 2, 2);
        Assertions.assertEquals(15, total);

        total = mutable2DSumRangeQuery.sumRegion(0, 1, 2, 1);
        Assertions.assertEquals(9, total);

    }
}
