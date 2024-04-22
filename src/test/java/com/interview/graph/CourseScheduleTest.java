package com.interview.graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CourseScheduleTest {

    @Test
    public void testDifferentCases() {
        CourseSchedule cs = new CourseSchedule();
        int[][] courses = {{1,0},{2,0},{3,1},{3,2}};
        int[] output = cs.findOrder(4, courses);
        int[] expected = {0, 2, 1, 3};
        Assertions.assertArrayEquals(expected, output);


        int[][] courses1 = {{1,0},{2,0},{3,1},{3,2}, {0, 3}};
        int[] output1 = cs.findOrder(4, courses1);
        int[] expected1 = {};
        Assertions.assertArrayEquals(expected1, output1);
    }
}
