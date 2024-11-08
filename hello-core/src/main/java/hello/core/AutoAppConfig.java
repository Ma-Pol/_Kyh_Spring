package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(
        // @ComponentScan 을 사용하면 @Configuration 이 붙은 설정 정보도 자동으로 등록된다.
        // 따라서 AppConfig, TestConfig 등도 함께 등록이 되어버린다.
        // 때문에 excludeFilters 옵션으로 Configuration 애노테이션을 제외시켰다.
        excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class),
        // 모든 자바 클래스를 스캔하면 시간이 오래 걸린다.
        // 꼭 필요한 위치부터 탐색하도록 시작 위치를 지정할 수 있다.
        // 설정하지 않으면 해당 클래스가 위치한 패키지가 시작 위치로 지정된다.
        basePackages = "hello.core"
        // basePackages = {"hello.core", "hello.service"}
)
public class AutoAppConfig {
    // 빈 이름이 충돌되는 경우(수동 빈 등록 vs 자동 빈 등록)
    // 수동 빈 등록이 우선권을 가지고 오버라이딩된다.
    // => 현재는 빈 이름 충돌 시, 오류가 발생한다.
//    @Bean(name = "memoryMemberRepository")
//    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//    }
}
