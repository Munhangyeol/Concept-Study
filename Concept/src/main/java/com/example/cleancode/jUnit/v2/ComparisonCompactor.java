package com.example.cleancode.jUnit.v2;

/**
 * 리펙토링 사항
 * 1.변수의 범위를 나타내는 접두어 제거
 * - fExpected,fActual등등 -> expected,actual 등등-> 생성자 내에 this 사용
 * 2. 의도를 명확하게 하기 위하여 조건문을 캡슐화
 * - shouldNotCompact
 * 3. 1번 때문에 함수에서 멤버 변수와 이름이 같은 변수가 생기는데,이것 보다는 함수내에서 사용하는
 * 변수의 의미를 명확히 붙여야함
 *         String compactExpected  = compactString(expected); 등등
 * 각 과정의 리팩토링 후 테스트 코드를 돌려본다.-> 성공-> tdd
 * 인터페이스를 도입할까도 생각했지만 이 경우는 리팩토링이므로, 진행하지 않음
 * (실제 코딩할 때는 수정할 것이므로)
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

    public ComparisonCompactor(int contextLength, String expected, String actual) {
        this.contextLength = contextLength;
        this.expected = expected;
        this.actual = actual;
    }

    public String compact(String message) {
        if (shouldNotCompact()) {
            return format(message, expected, actual);
        }

        findCommonPrefix();
        findCommonSuffix();
        String compactExpected  = compactString(expected);
        String compactActual  = compactString(actual);

        return format(message, compactExpected, compactActual);
    }

    private boolean shouldNotCompact() {
        return expected == null || actual == null || areStringsEqual();
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

    private boolean areStringsEqual() {
        return expected.equals(actual);
    }
}
