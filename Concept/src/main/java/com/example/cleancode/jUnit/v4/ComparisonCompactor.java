package com.example.cleancode.jUnit.v4;

/**
 * 리펙토링 사항
 * 1.compactExpectedAndActual 함수내에서 일관되게 사용을 하게 만듬
 * - 새 함수에서 마지막 두줄은 변수를 반환하지만 첫째 줄과 둘째 줄은 반환값이 없다.
 * - 기존의  pref를 prefixIndex로 변경,suffixIndex도 마찬가지
 * 2. findCommonSuffix는 바로 앞에서 구한 pefix에 의존하는데, 이것이 표현이 안됨
 * - findCommonSuffix에서 인자로 받고, 이를 사용하는 것으로 변경
 * 3. 하지만 이 방식은
 *    호출순서는 명확하지만
 *    인자를 넘기는 방식이 너무 인위적이며
 *    prefixlndex가 필요한 이유는 설명하지 못한다
 *    따라서
 * -  의도를 분명히 드러내기 위해서 findCommonPrefixAndSuffix()로 묶음
 * -  이 과정에서 findCommonPrefix에서 반환하던 prefix를 원래대로 다시 변수에 담음
 *
 *

 *
 */


public class ComparisonCompactor {

    private static final String ELLIPSIS = "...";
    private static final String DELTA_END = "]";
    private static final String DELTA_START = "[";

    private int contextLength;
    private String expected;
    private String actual;
    private int prefixIndex;
    private int suffixIndex;
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
    //2번의 결과->prefIndex를 따로 뺌
    /*private void compactExpectedAndActual() {
        prefixIndex=findCommonprefixIndex();
        suffixIndex=findCommonSuffix(prefixIndex);
        compactExpected= compactString(expected);
        compactActual  = compactString(actual);
    }*/
    private void compactExpectedAndActual() {
        findCommonPrefixAndSuffix();
        compactExpected= compactString(expected);
        compactActual  = compactString(actual);
    }
    //3번의 결과(인자로 넘기지 않고)
    private void findCommonPrefixAndSuffix() {
        findCommonprefixIndex();
        int expectedSuffix = expected.length() - 1;
        int actualSuffix = actual.length() - 1;
        for (; actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex; actualSuffix--, expectedSuffix--) {
            if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) {
                break;
            }
        }
        suffixIndex=expected.length() - expectedSuffix;
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
        String result = DELTA_START + source.substring(prefixIndex, source.length() - suffixIndex + 1) + DELTA_END;
        if (prefixIndex > 0) {
            result = computeCommonprefixIndex() + result;
        }
        if (suffixIndex > 0) {
            result = result + computeCommonSuffix();
        }
        return result;
    }

    private void findCommonprefixIndex() {
        prefixIndex = 0;
        int end = Math.min(expected.length(), actual.length());
        for (; prefixIndex < end; prefixIndex++) {
            if (expected.charAt(prefixIndex) != actual.charAt(prefixIndex)) {
                break;
            }
        }
//        return prefix;
    }

  /*  private int findCommonSuffix(int prefixIndexInFindSuffix) {
        int expectedSuffix = expected.length() - 1;
        int actualSuffix = actual.length() - 1;
        for (; actualSuffix >= prefixIndexInFindSuffix && expectedSuffix >= prefixIndexInFindSuffix; actualSuffix--, expectedSuffix--) {
            if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) {
                break;
            }
        }
        return expected.length() - expectedSuffix;
    }*/

    private String computeCommonprefixIndex() {
        return (prefixIndex > contextLength ? ELLIPSIS : "") + expected.substring(Math.max(0, prefixIndex - contextLength), prefixIndex);
    }

    private String computeCommonSuffix() {
        int end = Math.min(expected.length() - suffixIndex + 1 + contextLength, expected.length());
        return expected.substring(expected.length() - suffixIndex + 1, end) + (expected.length() - suffixIndex + 1 < expected.length() - contextLength ? ELLIPSIS : "");
    }

    private boolean areDiffrerentStrings() {
        return !expected.equals(actual);
    }
}
