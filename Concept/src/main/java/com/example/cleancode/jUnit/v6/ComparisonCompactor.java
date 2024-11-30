package com.example.cleancode.jUnit.v6;

/**
 * 리펙토링 사항
 * 1. compactExpected,compact..는 전체 클래스 내에서 사용하는데 아니라,
 * 특정 매서드 내에서만 사용하므로,
 * 함수내에서 선언하는 것으로 변경
 * 2.   formatCompactedComparison내에서 압축시
 *      compactExpectedAndActual()로 축약보다 차라리 아래 처럼 suffix,prefix찾기,압축하기
 *      -> 이런식으로 나두는게 가독성 면에서 나음(한 함수내에 다른 함수로 의미 없는 위임시
 *      이동을 해야하므로 가독성이 떨어짐)
 * 3. canBeCompact문 != != ...내의 코드가 보기 불편(하므로 아래와 같이 리팩토링)
 * 4. compactString을 아래와 같이 compact로 리팩토링
 * - 가독성이 좋아지고, 해당 부분이 문제가 생겼을 때 바로바로 이동후 수정이 가능함
 * - 메서드 명들이 compute어쩌고 보다 이게 훨씬 직관적이라고 느낌
 *
 *
 *   리팩토링 결과
 * - 개인적으로 눈이 보기에 매우 편한느낌이고, 훨씬 명확해진 느낌이다.
 * - 한번의 리펙토링시 test코드를 실행하면서, 점진적으로 개선시, 리팩토링시 두려움이 훨씬 줄어듬
 * (조금씩 바꾸고 테스트 코드를 돌리는 거니까 안되도 그부분만 고치면되지)
 * -
 * - 책 내용
     * -  분석함수-> 조합함수
     * -  각 함수가 사용된 직후에 정의됨
     * -  리팩토링을 하다보면 원래로 돌아가는 경우가 생김
     * ->  formatCompactedComparison내의 함수 등등
 *-
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


    public ComparisonCompactor(int contextLength, String expected, String actual) {
        this.contextLength = contextLength;
        this.expected = expected;
        this.actual = actual;
    }

    public String formatCompactedComparison(String message) {
        String compactExpected=expected;
        String compactActual=actual;
        if (shouldBeCompacted()) {
            findCommonPrefixAndSuffix();
            compactExpected= compact(expected);
            compactActual  = compact(actual);
            return format(message, compactExpected, compactActual);
        }
        else {
            return format(message, expected, actual);
        }
    }

 private boolean shouldBeCompacted() {
     //긍정문으로 바꾸면서 변화
     return !shouldNotBeCompacted();
 }
 private boolean shouldNotBeCompacted() {
     //긍정문으로 바꾸면서 변화
     return expected == null || actual == null || areStringsEquals();
 }


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


    public  String format(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null && message.length() > 0) {
            formatted = message + " ";
        }

        return formatted + "expected:<" + expected + "> but was:<" + actual + ">";
    }


/*    private String compactString(String source) {

        return computeCommonprefixLength()+DELTA_START + source.substring(prefixLength, source.length() - suffixLength ) + DELTA_END
                +computeCommonSuffix();
    }*/

    private void findCommonprefix() {
        prefixLength = 0;
        int end = Math.min(expected.length(), actual.length());
        for (; prefixLength < end; prefixLength++) {
            if (expected.charAt(prefixLength) != actual.charAt(prefixLength)) {
                break;
            }
        }
    }
    private String compact(String s){
        return startingEllipsis() +
                startingContext() +
                DELTA_START +
                delta(s) +
                DELTA_END+
                endingContext() +
                endingEllipsis();
    }
    private String startingEllipsis(){
        return prefixLength>contextLength?ELLIPSIS:"";
    }
    private String startingContext(){
        int contextStart=Math.max(0,prefixLength-contextLength);
        int contextEnd=prefixLength;
        return expected.substring(contextStart, contextEnd);
    }
    private String delta(String s){
        int deltaStart=prefixLength;
        int deltaEnd=s.length()-suffixLength;
        return  s.substring(deltaStart,deltaEnd);
    }
    private String endingContext(){
        int contextStart=expected.length()-suffixLength;
        int conextEnd=Math.min(contextStart+contextLength,expected.length());
        return expected.substring(contextStart, conextEnd);
    }
    private String endingEllipsis(){
        return (suffixLength > contextLength ? ELLIPSIS : "");
    }

    private boolean areStringsEquals() {
        return expected.equals(actual);
    }
}
