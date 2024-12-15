package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static hello.login.web.SessionConst.LOGIN_MEMBER;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        // 메서드 파라미터에 Login 어노테이션이 있는지 확인
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        // 메서드 파라미터의 타입이 Member 인지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    /**
     * 컨트롤러 호출 직전에 호출 <br />
     * 필요한 파라미터 정보를 생성 <br />
     * <br />
     * 이 ArgumentResolver 는 <br />
     * 1. Login 어노테이션이 있으면서 <br />
     * 2. 그 타입이 Member 인 파라미터에 <br />
     * 3. return 객체를 주입해준다.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        return session.getAttribute(LOGIN_MEMBER);
    }
}
