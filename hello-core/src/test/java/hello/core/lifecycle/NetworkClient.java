package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class NetworkClient {
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
//        connect();
//        call("초기화 연결 메시지");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작 시 호출
    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    // 서비스 종료 시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }

    // @Bean(initMethod = "init")
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    // @Bean(destroyMethod = "close")
    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }

//    // InitializingBean 인터페이스
//    // 의존관계 주입 완료 후 콜백
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        connect();
//        call("초기화 연결 메시지");
//    }
//
//    // DisposableBean 인터페이스
//    // 소멸 전 콜백
//    @Override
//    public void destroy() throws Exception {
//        disconnect();
//    }
}
