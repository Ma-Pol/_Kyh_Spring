package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {
    @Test
    void lifeCycleTest() {
        ConfigurableApplicationContext ac = new AnnotationConfigReactiveWebApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        /* 출력 결과
         *
         * 생성자 호출, url = null
         * connect: null
         * call: null message = 초기화 연결 메시지
         */

        ac.close(); // 스프링 컨테이너를 종료, ConfigurableApplicationContext 종료
    }

    @Configuration
    static class LifeCycleConfig {
//        @Bean(initMethod = "init", destroyMethod = "close")
        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
}
