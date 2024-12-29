package com.example.di.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import javax.sql.DataSource;

@SpringBootTest
@Slf4j
public class BasicTxTest {
    @Autowired
    private PlatformTransactionManager txManager;

    @TestConfiguration
    static class BasicTxConfig{
        @Bean
        public PlatformTransactionManager platformTransactionManager(DataSource source){
            return new DataSourceTransactionManager(source);
        }
    }

    //커밋과 롤백을 수행시 txManager를 통해서 직접 수행하는 방법.
    @Test
    void commit(){
        log.info("트랜젝션 시작");
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜젝션 커밋 시작");
        txManager.commit(status);
        log.info("트랜젝션 커밋 종료");
    }
    @Test
    void rollback(){
        log.info("트랜젝션 시작");
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜젝션 롤백 시작");
        txManager.rollback(status);
        log.info("트랜젝션 롤백 종료");
    }
    @Test
    void double_commit(){
        log.info("트랜젝션 1 시작");
        //여기에 반환하는 커넥션은 실제 커낵션을 감싼 히카리 프록시 커넥션임
        //conn을 통해서 커넥션을 재사용한 것이지, 두 곳에서 사용한 커넥션은 엄연히 다른 커넥션이다.
        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜젝션 1 커밋");
        txManager.commit(tx1);
        //이 시점에서 transcation1에 대한 커낵션은 반환됨.

        log.info("트랜젝션 2 시작");
        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜젝션 2 커밋");
        txManager.commit(tx2);
    }
    @Test
    void double_commit_rollback(){
        log.info("트랜젝션 1 시작");
        //여기에 반환하는 커넥션은 실제 커낵션을 감싼 히카리 프록시 커넥션임
        //conn을 통해서 커넥션을 재사용한 것이지, 두 곳에서 사용한 커넥션은 엄연히 다른 커넥션이다.
        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜젝션 1 커밋");
        txManager.commit(tx1);
        log.info("트랜젝션 2 시작");
        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜젝션 2 커밋");
        txManager.rollback(tx2);
    }

    // Transcation 전파
    // 위의 예시 처럼 하나의 트랜젝션을 사용할 때마다 커넥션을 새로 받아서 사용하는게 아닌
    // 기존에 사용하던 트랜잭션을 이어서 사용하는 것을 트랜젝션 전파라고 한다
    // 또한 각각의 트랜젝션은 논리적 트랜젝션, 전파시에 하나의 단위로 묶이는 트랜젝션은 물리적 트랜젝션으로 나누어서
    // 생각하며, 모든 논리적 트랜젝션이 커밋되어야 물리적 트랜젝션도 커밋되는 것이 기본적인 트랜젝션 전파의 규칙이다.
    // 트랜젝션 전파시에 커밋과 롤백의 결정은 트랜젝션의 시작이 되는 시점 즉 외부 트랜젝션에서 결정하는 것으로, 트랜젝션의
    // 전파시 중복 문제를 일반적으로 해결한다.
    // 또한 외부 트랜젝션은 isNewTransaction을 통해서 여부를 일반적으로 확인한다.
    @Test
    void inner_commit(){
        log.info("외부 트랜젝션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        //처음 생성했는지의 여부이다. 외부 트랜젝션에서 커넥션이 새로 생성되었으므로, true이다.
        log.info("outer.isNewTranscation()={}", outer.isNewTransaction());

        //Paticipating 을 통해서 현재 존재하던 외부 트랜젝션에 참여한다.
        log.info("내부 트랜젝션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        //이 경우에는 기존의 전파된 트랜젝션을 이용, 즉 커낵션을 이용하므로, 생성하는 것이 아니다. 따라서 false가 됨.
        log.info("outer.isNewTranscation()={}", inner.isNewTransaction());

        //내부 트랜젝션 커밋을 수행시 isNewTrascation()이 false이므로 그냥 넘어간다.
        log.info("내부 트랜젝션 커밋");
        txManager.commit(inner);

        //여기서만 커밋,롤백에 수행됨
        log.info("외부 트랜젝션 커밋");
        txManager.commit(outer);
    }
    @Test
    void outer_rollback(){
        log.info("외부 트랜젝션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        //처음 생성했는지의 여부이다. 외부 트랜젝션에서 커넥션이 새로 생성되었으므로, true이다.
        log.info("outer.isNewTranscation()={}", outer.isNewTransaction());

        //Paticipating 을 통해서 현재 존재하던 외부 트랜젝션에 참여한다.
        log.info("내부 트랜젝션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        //이 경우에는 기존의 전파된 트랜젝션을 이용, 즉 커낵션을 이용하므로, 생성하는 것이 아니다. 따라서 false가 됨.
        log.info("outer.isNewTranscation()={}", inner.isNewTransaction());

        //내부 트랜젝션 커밋을 수행시 isNewTrascation()이 false이므로 그냥 넘어간다.
        log.info("내부 트랜젝션 커밋");
        txManager.commit(inner);

        //내부 트랜젝션과 관계없이 현재 외부 르랜젝션이 롤백되므로, 물리적 트랜젝션이 롤백된다.
        //이 경우는 예외가 터지지 않지만, inner_rollback인 경우는 의도한 경우와 다른 상황이므로 예외가 터진다.
        log.info("외부 트랜젝션 롤백");
        txManager.rollback(outer);
    }
    @Test
    void inner_rollback(){
        log.info("외부 트랜젝션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        //처음 생성했는지의 여부이다. 외부 트랜젝션에서 커넥션이 새로 생성되었으므로, true이다.
        log.info("outer.isNewTranscation()={}", outer.isNewTransaction());

        //Paticipating 을 통해서 현재 존재하던 외부 트랜젝션에 참여한다.
        log.info("내부 트랜젝션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        //이 경우에는 기존의 전파된 트랜젝션을 이용, 즉 커낵션을 이용하므로, 생성하는 것이 아니다. 따라서 false가 됨.
        log.info("outer.isNewTranscation()={}", inner.isNewTransaction());

        //내부 트랜젝션 롤백 수행시, 트랜잭션 동기화 매니저에서 보관하고 있는 커낵션을 rollbackOnly=True로 바꾼다.
        log.info("내부 트랜젝션 롤백");
        txManager.rollback(inner);

        // 외부 트랜젝션코드에서 커밋을 요청할 시,현재 트랜잭션 동기화 매니저내의 커낵션을 확인해서
        // rollbackOnly가 true인지를 확인하고 true이면, 물리적 트랜젝션의 롤백을 수행하고,
        // 외부 트랜잭션에서는 커밋되기를 예측했는데 현재 롤백이 되는 상황이므로
        // 사용자에게  UnexpectedRollbackException을 반환한다.
        log.info("외부 트랜젝션 커밋");
        Assertions.assertThrows(UnexpectedRollbackException.class, () -> txManager.commit(outer));
    }

    // 결국 이경우에는 커낵션을 2개 연결하므로, 대규모 트래픽이거나 고성능을 요구한 경우에 문제가 생길 수 있으므로
    // 조심해서 사용해야한다.
    @Test
    void inner_rollback_requires_new(){
        log.info("외부 트랜젝션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        //처음 생성했는지의 여부이다. 외부 트랜젝션에서 커넥션이 새로 생성되었으므로, true이다.
        log.info("outer.isNewTranscation()={}", outer.isNewTransaction());

        //내부 트랜잭션으로 하여금 requires_new를 통해서 새로운 트랜잭션이자 커낵션을 생성한다.
        //따라서 각각의 트랜젝션끼리 롤백, 커밋이 둘다 일어나는 물리적 트랜젝션이 두개다 되는 것임.
        log.info("내부 트랜젝션 시작");
        DefaultTransactionAttribute attribute = new DefaultTransactionAttribute();
        attribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus inner = txManager.getTransaction(attribute);
        //새로 생성된 것이므로 true
        log.info("outer.isNewTranscation()={}", inner.isNewTransaction());
        log.info("내부 트랜젝션 롤백");

        //isNewtransaction시 true이므로 물리적 트랜잭션의 주체가 됨.
        //따라서 롤백을 수행.
        txManager.rollback(inner);

        //이 때 rollbackOnly와 isNewtranscation둘다 체크
        log.info("외부 트랜젝션 커밋");
        txManager.commit(outer);
    }



}
