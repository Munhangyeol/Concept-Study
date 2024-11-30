package com.example.cleancode.jUnit.v5;

/**
 * 리펙토링 사항
 * 1.기존의 findCommonPerfixAndSuffix 코드가 너무 지저분해서 두개의 메서드로 위임
 * 2. Length가 실제로는 접미사,접미어의 길이임-> Length로 변경(+1)되어있는 부분 제거해야함
 * 3. 길이 이므로, compactString의 length>0은 의미 없는 조건문임-> 제거
 * - 코드를 보면 이미 length가 0이면 ""가 출력되게 해놨으므로,
 */


public class ComparisonCompactor {

    private static final String ELLIPSIS = "...";
    private static final String DELTA_END = "]";
    private static final String DELTA_START = "[";

    private int contextLength;
    private String expected;
    private String actual;
    private int prefixLength;
    private int suffixLength;
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
    //2번의 결과->prefLength를 따로 뺌
    /*private void compactExpectedAndActual() {
        prefixLength=findCommonprefixLength();
        suffixLength=findCommonSuffix(prefixLength);
        compactExpected= compactString(expected);
        compactActual  = compactString(actual);
    }*/
    private void compactExpectedAndActual() {
        findCommonPrefixAndSuffix();
        compactExpected= compactString(expected);
        compactActual  = compactString(actual);
    }
    //3번의 결과(인자로 넘기지 않고)
   /* private void findCommonPrefixAndSuffix() {
        findCommonprefixLength();
        int expectedSuffix = expected.length() - 1;
        int actualSuffix = actual.length() - 1;
        for (; actualSuffix >= prefixLength && expectedSuffix >= prefixLength; actualSuffix--, expectedSuffix--) {
            if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) {
                break;
            }
        }
        suffixLength=expected.length() - expectedSuffix;
    }
    
    */
    private void findCommonPrefixAndSuffix() {
        findCommonprefix();
        suffixLength=0;
        // ->->->              <-<-<-
        for (;!suffixOverlapsPrefix(suffixLength);suffixLength++) {
            if (charFromEnd(expected,suffixLength)!=
                    charFromEnd(actual,suffixLength)) {
                break;
            }
        }

    }
    private char charFromEnd(String s,int i){
        return s.charAt(s.length() -i- 1);
    }
    private boolean suffixOverlapsPrefix(int suffixLength){
        return actual.length()-suffixLength<=prefixLength||
                expected.length()-suffixLength<=prefixLength;
    }

    private boolean canBeCompact() {
        //긍정문으로 바꾸면서 변화
        //
        return expected != null && actual != null && areDiffrerentStrings();
    }

    public  String format(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null && message.length() > 0) {
            formatted = message + " ";
        }

        return formatted + "expected:<" + expected + "> but was:<" + actual + ">";
    }

    /*private String compactString(String source) {
        String result = DELTA_START + source.substring(prefixLength, source.length() - suffixLength ) + DELTA_END;
        if (prefixLength > 0) {
            result = computeCommonprefixLength() + result;
        }
        if (suffixLength > 0) {
            result = result + computeCommonSuffix();
        }
        return result;
    }*/
    private String compactString(String source) {
        return computeCommonprefixLength()+DELTA_START + source.substring(prefixLength, source.length() - suffixLength ) + DELTA_END
                +computeCommonSuffix();
    }



    private void findCommonprefix() {
        prefixLength = 0;
        int end = Math.min(expected.length(), actual.length());
        for (; prefixLength < end; prefixLength++) {
            if (expected.charAt(prefixLength) != actual.charAt(prefixLength)) {
                break;
            }
        }
//        return prefix;
    }

  /*  private int findCommonSuffix(int prefixLengthInFindSuffix) {
        int expectedSuffix = expected.length() - 1;
        int actualSuffix = actual.length() - 1;
        for (; actualSuffix >= prefixLengthInFindSuffix && expectedSuffix >= prefixLengthInFindSuffix; actualSuffix--, expectedSuffix--) {
            if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) {
                break;
            }
        }
        return expected.length() - expectedSuffix;
    }*/

    private String computeCommonprefixLength() {
        return (prefixLength > contextLength ? ELLIPSIS : "") + expected.substring(Math.max(0, prefixLength - contextLength), prefixLength);
    }

    private String computeCommonSuffix() {
        int end = Math.min(expected.length() - suffixLength  + contextLength, expected.length());
        return expected.substring(expected.length() - suffixLength , end) + (expected.length() - suffixLength  < expected.length() - contextLength ? ELLIPSIS : "");
    }

    private boolean areDiffrerentStrings() {
        return !expected.equals(actual);
    }
}
