package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

public class PrototypeProviderTest {
    @Test
    void providerSpringTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBeanSpring.class, PrototypeBean.class);

        ClientBeanSpring clientBean1 = ac.getBean(ClientBeanSpring.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);


        ClientBeanSpring clientBean2 = ac.getBean(ClientBeanSpring.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);
    }

    @Test
    void providerJavaTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBeanJava.class, PrototypeBean.class);

        ClientBeanJava clientBean1 = ac.getBean(ClientBeanJava.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);


        ClientBeanJava clientBean2 = ac.getBean(ClientBeanJava.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);
    }

    @Scope("singleton")
    @Component
    static class ClientBeanJava {
        // Java가 제공하는 Provider(추가 라이브러리 필요)
        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();

            return prototypeBean.getCount();
        }
    }

    @Scope("singleton")
    @Component
    static class ClientBeanSpring {
        // 스프링이 제공하는 Provider(스프링 기본 제공)
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;

        public int logic() {
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            prototypeBean.addCount();

            return prototypeBean.getCount();
        }
    }

    @Scope("prototype")
    @Component
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}
