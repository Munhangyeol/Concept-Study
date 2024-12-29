package com.example.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;
    /**
     * MemberService        @Transactionl:OFF
     * MemberRepository     @Transactionl:ON
     * LogRepository        @Transactionl:ON
     */
    //서비스 계층에 트랜잭션이 없어서 각각의 트랜잭션과 커낵션이 따로 생성되어서 실행됨
    //신규트랜잭션 여부와 rollbackOnly의 체크는 모두 함
    @Test
    void outTxOff_success(){
        //given
        String username = "outTxOff_success";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());

    }
    /**
     * MemberService        @Transactionl:OFF
     * MemberRepository     @Transactionl:ON
     * LogRepository        @Transactionl:ON  Exception
     */
    //서비스 계층에 트랜잭션이 없어서 각각의 트랜잭션과 커낵션이 따로 생성되어서 실행됨
    //이 경우 로그저장 로직이 RuntimeException때문에 커밋되지 않고 롤백이 된다.
    //예외 발생시 실제 객체로부터 aop프록시로 예외가 전달되어서, 여기서 트랜잭션 롤백 요청을 함
    @Test
    void outTxOff_fail(){
        //given
        String username = "로그예외_outTxOff_fail";

        //when
       Assertions.assertThrows(RuntimeException.class ,()->memberService.joinV1(username));

        //then
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }
    /**
     * MemberService        @Transactionl:ON
     * MemberRepository     @Transactionl:OFF
     * LogRepository        @Transactionl:OFF
     */
    // 위의 코드들의 경우 멤버는 가입 되었는데, 로그가 없을 수도 있는 데이터 정합성의 문제가 있을 수 있다.
    //따라서 서비스단의 단일 트랜젝션을 이용해서 하나의 트랜잭션만 이용해서 모두 롤백 or 커밋을 통해서 데이터 정합성 문제를 해결
    @Test
    void singleTX(){
        //given
        String username = "singleTX";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());

    }
    // 각 리포지토리마다의 트랜잭션이 필요할 수도 있고, memberService를 사용하는 클래스가 생겨서 해당 클래스에서의
    // 트랜젝션이 필요할 수 도 있다. 이를 위해서 트랜잭션 전파를 활용할 수 있음.
    // 기본옵션인 prpogation=required는 없으면 트랜잭션을 생성하고 현재 있으면 같은 물리 트랜잭션을 사용한다.
    /**
     * MemberService        @Transactionl:ON
     * MemberRepository     @Transactionl:ON
     * LogRepository        @Transactionl:ON
     */
    //트랜잭션 전파를 이용한 코드
    @Test
    void outTxOn_success(){
        //given
        String username = "outTxOn_success";

        //when
        memberService.joinV1(username);

        //then
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());

    }
    /**
     * MemberService        @Transactionl:ON
     * MemberRepository     @Transactionl:ON
     * LogRepository        @Transactionl:ON
     */
    //트랜잭션 전파를 이용한 코드
    //로그 저장시 예외가 발생하는 코드
    @Test
    void outTxOn_fail(){
        //given
        String username = "로그예외_outTxOn_fail";

        //when
        Assertions.assertThrows(RuntimeException.class ,()->memberService.joinV1(username));

        // then : 같은 물리적 트랜젝션이므로 log가 저장이 안되면, 멤버도 저장이 안되고 롤백이 됨
        // 이 과정은 로그저장의 예외가 멤버 서비스 단으로 올라와서 해당 예외를 감지한
        // 멤버 서비스의 트랜젝션 aop 프록시가 롤백을 하는 것이다.
        // 물론 rollbackOnly=True가 되어 있긴하나, 현재 서비스단의 aop 프록시가 이미 예외를 감지 했기 때문에
        // 해당 bollean값을 안 보고도 롤백을 트랜젝션 매니저에게 요청하고 트랜잭션 메니저는 이를 수행
        Assertions.assertTrue(memberRepository.find(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }
    //로그저장이 안된다고 회원가입도 안되는거보다는 로그저장은 추후에 하고 회원가입은 가능하게 하는 비즈니스 요구사항.
    //실패케이스
    /**
     * MemberService        @Transactionl:ON
     * MemberRepository     @Transactionl:ON
     * LogRepository        @Transactionl:ON Exception
     */
    //트랜잭션 전파를 이용한 코드
    //로그 저장시 예외가 발생하는 코드
    @Test
    void recoverException_fail(){
        //given
        String username = "로그예외_recoverException_fail";

        //when
        Assertions.assertThrows(UnexpectedRollbackException.class ,()->memberService.joinV2(username));

        //then
        Assertions.assertTrue(memberRepository.find(username).isEmpty());
        Assertions.assertTrue(logRepository.find(username).isEmpty());

        //기대한 것과는 달리 두 데이터가 모두 롤백이 되었다.
        // 왜냐하면 log 저장 예외가 더치면 해당 트랜잭션에서 rollbackOnly가 true로 setting 되어있기 때문에
        // memberSerivce aop  프록시 객체가 커밋을 요청해도, 트랜잭션 메니저가  rollbackOnly를 확인하고 롤백을
        // 하기 때문이다.-> 이 때문에 UnexpectedRollbackException도 발생한다.
     }
    //로그저장이 안된다고 회원가입도 안되는거보다는 로그저장은 추후에 하고 회원가입은 가능하게 하는 비즈니스 요구사항.
    //성공 케이스
    /**
     * MemberService        @Transactionl:ON
     * MemberRepository     @Transactionl:ON
     * LogRepository        @Transactionl(REQUIRES_NEW):ON Exception
     */
    // 트랜잭션 전파를 이용한 코드
    // 로그 저장시 예외가 발생하는 코드
    // REQUIRES_NEW세팅을 통해서 로그 저장 로직시 새로운 트랜젝션을 새로 만들어서
    // 해당 롤백이 로그 저장 단에서 처리가 되게 함
    // 애초에 같은 커넥션을 공유하지 않기 때문에, 위와 같은 문제도 발생하지 않는다.
    @Test
    void recoverException_success() {
        //given
        String username = "로그예외_recoverException_success";

        //when
        memberService.joinV2(username);

        //then
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty());
    }
    // 이렇게 되면 두개의 커넥션을 동시에 사용한다는 문제점이 있다.
    // 따라서 MemberService와 LogRepsitory에 의존하는 MemberFacade를 만들어서
    // 트랜젝션이 끝나서 커밋이나 롤백되는 시점이 MemberFacade에 안두는 이런
    // 구조를 통해서도 이 문제를 해결할 수 있는데
    // 이는 비즈니스 요구사항, 코드의 상태 등에 따라서 해결방법이 달라진다.



}