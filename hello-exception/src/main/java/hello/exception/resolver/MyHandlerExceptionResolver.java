package hello.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    /**
     * 반환 값에 따른 DispatcherServlet 동작 방식 <br />
     * <br />
     * 빈 ModelAndView <br />
     * - 뷰를 렌더링 하지 않는다. <br />
     * - 정상 흐름으로 서블릿이 리턴된다. <br />
     * <br />
     * ModelAndView 지정(Model, View 정보 지정) <br />
     * - 뷰를 렌더링한다. <br />
     * <br />
     * null <br />
     * - 다음 ExceptionResolver 를 찾아서 실행한다. <br />
     * - 처리할 수 있는 ExceptionResolver 가 없으면 예외를 서블릿 밖으로 던진다.
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("illegalArgumentException resolver to 400");
                response.sendError(SC_BAD_REQUEST, ex.getMessage());

                return new ModelAndView();
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null;
    }
}
