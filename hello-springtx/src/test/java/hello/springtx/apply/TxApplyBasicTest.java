package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class TxApplyBasicTest {
    @Autowired
    BasicService basicService;

    @Test
    void proxyCheck() {
        log.info("AOP class={}", basicService.getClass());

        // 메서드에 @Transactional 이 붙어도 객체 자체에 AOP 프록시가 설정됨
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void txTest() {
        basicService.tx();
        basicService.nonTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig {
        @Bean
        BasicService basicService() {
            return new BasicService();
        }
    }

    @Slf4j
    static class BasicService {
        @Transactional
        public void tx() {
            log.info("call tx");

            // 현재 스레드에 트랜잭션이 적용되어 있는지 확인
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();

            log.info("tx active={}", txActive);
        }

        public void nonTx() {
            log.info("call nonTx");

            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();

            log.info("tx active={}", txActive);
        }
    }
}
