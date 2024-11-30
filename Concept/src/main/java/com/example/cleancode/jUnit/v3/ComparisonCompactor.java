package com.example.cleancode.jUnit.v3;

/**
 * 리펙토링 사항
 * 1.shouldNotCompact에서 사용한 메서드명을 긍정문으로 바꿈(하지 않는다면 보다 한다면이 더 직관적)
 * - canBeCompact로 변경
 * - areStringsEqual보다는 areDiffrerentStrings로 메서드 명을 바꾸는게 나아서 임의 변경
 * 2. compact 함수내에서 canBeCompact가 false이면 실제로 compact는 진행되지 않는데,
 * 함수 이름은 단순하게 compact하는 걸로 표현함. 즉 오류 점검이라는 부가 단계를 드러낼 수 없음
 * 또한 단순하게 압축된 문자열이 아닌 formatting된 문자열을 반환함
 * -> 따라서 formatCompactedComparison 으로 변경
 * 3. formatCompactedComparison내에서 압축하는 과정을 다른 메서드로 위임
 * - compactExpectedAndActual메서드를 만들고 여기로 위임
 * - compactActual,compactExpected를 위의 class 변수로 뺌
 */

public class ComparisonCompactor {

    private static final String ELLIPSIS = "...";
    private static final String DELTA_END = "]";
    private static final String DELTA_START = "[";

    private int contextLength;
    private String expected;
    private String actual;
    private int prefix;
    private int suffix;
    private String compactExpected;
    private String compactActual;

    public ComparisonCompactor(int contextLength, String expected, String actual) {
        this.contextLength = contextLength;
        this.expected = expected;
        this.actual = actual;
    }

    public String formatCompactedComparison(String message) {
        if (canBeCompact()) {
            compactExpectedAndActual();
            return format(message, compactExpected, compactActual);
        }
        else {
            return format(message, expected, actual);
        }
    }

    private void compactExpectedAndActual() {
        findCommonPrefix();
        findCommonSuffix();
        compactExpected= compactString(expected);
        compactActual  = compactString(actual);
    }

    private boolean canBeCompact() {
        //긍정문으로 바꾸면서 변화
        return expected != null && actual != null && areDiffrerentStrings();
    }

    public  String format(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null && message.length() > 0) {
            formatted = message + " ";
        }

        return formatted + "expected:<" + expected + "> but was:<" + actual + ">";
    }

    private String compactString(String source) {
        String result = DELTA_START + source.substring(prefix, source.length() - suffix + 1) + DELTA_END;
        if (prefix > 0) {
            result = computeCommonPrefix() + result;
        }
        if (suffix > 0) {
            result = result + computeCommonSuffix();
        }
        return result;
    }

    private void findCommonPrefix() {
        prefix = 0;
        int end = Math.min(expected.length(), actual.length());
        for (; prefix < end; prefix++) {
            if (expected.charAt(prefix) != actual.charAt(prefix)) {
                break;
            }
        }
    }

    private void findCommonSuffix() {
        int expectedSuffix = expected.length() - 1;
        int actualSuffix = actual.length() - 1;

        for (; actualSuffix >= prefix && expectedSuffix >= prefix; actualSuffix--, expectedSuffix--) {
            if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) {
                break;
            }
        }
        suffix = expected.length() - expectedSuffix;
    }

    private String computeCommonPrefix() {
        return (prefix > contextLength ? ELLIPSIS : "") + expected.substring(Math.max(0, prefix - contextLength), prefix);
    }

    private String computeCommonSuffix() {
        int end = Math.min(expected.length() - suffix + 1 + contextLength, expected.length());
        return expected.substring(expected.length() - suffix + 1, end) + (expected.length() - suffix + 1 < expected.length() - contextLength ? ELLIPSIS : "");
    }

    private boolean areDiffrerentStrings() {
        return !expected.equals(actual);
    }
}
