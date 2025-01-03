package com.example.di.jUnit;

import com.example.cleancode.jUnit.v6.ComparisonCompactor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//
public class ComparisonCompactorTest_V3_V6 {

    @Test
    public void testMessage() {
        String failure = new ComparisonCompactor(0, "b", "c").formatCompactedComparison("a");
        assertTrue("a expected:<[b]> but was:<[c]>".equals(failure));
    }


    @Test
    public void testStartSame() {
        String failure = new ComparisonCompactor(1, "ba", "bc").formatCompactedComparison(null);
        assertEquals("expected:<b[a]> but was:<b[c]>", failure);
    }

    @Test
    public void testEndSame() {
        String failure = new ComparisonCompactor(1, "ab", "cb").formatCompactedComparison(null);
        assertEquals("expected:<[a]b> but was:<[c]b>", failure);
    }

    @Test
    public void testSame() {
        String failure = new ComparisonCompactor(1, "ab", "ab").formatCompactedComparison(null);
        assertEquals("expected:<ab> but was:<ab>", failure);
    }
    @Test
    public void testNoContextStartAndEndSame() {
        String failure = new ComparisonCompactor(0, "abc", "adc").formatCompactedComparison(null);
        assertEquals("expected:<...[b]...> but was:<...[d]...>", failure);
    }
    @Test
    public void testStartAndEndContext() {
        String failure = new ComparisonCompactor(1, "abc", "adc").formatCompactedComparison(null);
        assertEquals("expected:<a[b]c> but was:<a[d]c>", failure);
    }
    @Test
    public void testStartAndEndContextWithEllipses() {
        String failure = new ComparisonCompactor(1, "abcde", "abfde").formatCompactedComparison(null);
        assertEquals("expected:<...b[c]d...> but was:<...b[f]d...>", failure);
    }
    @Test
    public void testComparisonErrorStartSameComplete() {
        String failure = new ComparisonCompactor(2, "ab", "abc").formatCompactedComparison(null);
        assertEquals("expected:<ab[]> but was:<ab[c]>", failure);
    }
    @Test
    public void testComparisonErrorEndSameComplete() {
        String failure = new ComparisonCompactor(0, "bc", "abc").formatCompactedComparison(null);
        assertEquals("expected:<[]...> but was:<[a]...>", failure);
    }
    @Test
    public void testComparisonErrorEndSameCompleteContext() {
        String failure = new ComparisonCompactor(2, "bc", "abc").formatCompactedComparison(null);
        assertEquals("expected:<[]bc> but was:<[a]bc>", failure);
    }
    @Test
    public void testComparisonErrorOverlappingMatches() {
        String failure = new ComparisonCompactor(0, "abc", "abbc").formatCompactedComparison(null);
        assertEquals("expected:<...[]...> but was:<...[b]...>", failure);
    }
    @Test
    public void testComparisonErrorOverlappingMatchesContext() {
        String failure = new ComparisonCompactor(2, "abc", "abbc").formatCompactedComparison(null);
        assertEquals("expected:<ab[]c> but was:<ab[b]c>", failure);
    }
    @Test
    public void testComparisonErrorOverlappingMatches2() {
        String failure = new ComparisonCompactor(0, "abcdde", "abcde").formatCompactedComparison(null);
        assertEquals("expected:<...[d]...> but was:<...[]...>", failure);
    }
    @Test
    public void testComparisonErrorOverlappingMatches2Context() {
        String failure = new ComparisonCompactor(2, "abcdde", "abcde").formatCompactedComparison(null);
        assertEquals("expected:<...cd[d]e> but was:<...cd[]e>", failure);
    }
    @Test
    public void testComparisonErrorWithActualNull() {
        String failure = new ComparisonCompactor(0, "a", null).formatCompactedComparison(null);
        assertEquals("expected:<a> but was:<null>", failure);
    }
    @Test
    public void testComparisonErrorWithActualNullContext() {
        String failure = new ComparisonCompactor(2, "a", null).formatCompactedComparison(null);
        assertEquals("expected:<a> but was:<null>", failure);
    }
    @Test
    public void testComparisonErrorWithExpectedNull() {
        String failure = new ComparisonCompactor(0, null, "a").formatCompactedComparison(null);
        assertEquals("expected:<null> but was:<a>", failure);
    }
    @Test
    public void testComparisonErrorWithExpectedNullContext() {
        String failure = new ComparisonCompactor(2, null, "a").formatCompactedComparison(null);
        assertEquals("expected:<null> but was:<a>", failure);
    }
    @Test
    public void testBug609972() {
        String failure = new ComparisonCompactor(10, "S&P500", "0").formatCompactedComparison(null);
        assertEquals("expected:<[S&P50]0> but was:<[]0>", failure);
    }
}
