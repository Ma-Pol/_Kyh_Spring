package hello.login.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

import static hello.login.web.SessionConst.LOGIN_MEMBER;

@Slf4j
public class LoginCheckFilter implements Filter {
    private static final String[] whitelist = {"/", "/member/add", "/login", "/logout", "/css/*", "/favicon.ico"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);

            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);

                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);

                    // 로그인 화면으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);

                    return; // (최중요) 미인증 사용자는 다음 필터, 서블릿을 실행하지 않음
                }
            }

            chain.doFilter(request, response);
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트 URI의 경우 인증 체크 X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
