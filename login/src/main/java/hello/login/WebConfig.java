package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()) // 인터셉터를 등록한다.
                .order(1) // 인터셉터의 호출 순서를 지정한다.
                .addPathPatterns("/**") // 인터셉터를 적용할 URL 패턴을 지정한다.
                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 인터셉터에서 제외할 URL 패턴을 지정한다.

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", // 홈
                        "/members/add", // 회원가입
                        "/login", // 로그인
                        "/logout", // 로그아웃
                        "/css/**", // css
                        "/*.ico", // 파비콘
                        "/error" // 오류
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    //    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        // 등록할 필터를 지정한다.
        filterRegistrationBean.setFilter(new LogFilter());
        // 필터의 순서를 지정한다. 숫자가 낮을수록 먼저 동작한다.
        filterRegistrationBean.setOrder(1);
        // 필터를 적용할 URL 패턴을 지정한다. 한번에 여러 패턴을 지정할 수 있다.
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    //    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
