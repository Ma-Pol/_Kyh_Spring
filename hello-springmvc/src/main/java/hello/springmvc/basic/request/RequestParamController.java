package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {
    private void logParams(Object username, Object age) {
        log.info("username={}, age={}", username, age);
    }

    /**
     * 반환 타입 없이 response에 값을 직접 집어넣으면 view를 조회하지 않음
     */
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        logParams(username, age);

        response.getWriter().write("ok");
    }

    /**
     * &#064;RequestParam  사용 <br />
     * - 파라미터 이름으로 바인딩 <br />
     * &#064;ResponseBody 추가 <br />
     * - View 조회를 무시하고, HTTP message body에 직접 해당 내용을 입력
     */
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge
    ) {
        logParams(memberName, memberAge);

        return "ok";
    }

    /**
     * &#064;RequestParam 사용 <br />
     * HTTP 파라미터 이름이 변수 이름과 같으면, <br />
     * &#064;RequestParam(name="...") 생략 가능
     */
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age
    ) {
        logParams(username, age);

        return "ok";
    }

    /**
     * &#064;RequestParam 사용 <br />
     * 기본형, 기본형 래퍼 클래스, String 등의 단순 타입인 경우, <br />
     * &#064;RequestParam 또한 생략 가능
     */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        logParams(username, age);

        return "ok";
    }

    /**
     * &#064;RequestParam.required <br />
     * /request-param-required -> username이 없으므로 예외 <br />
     * <br />
     * 주의! <br />
     * /request-param-required?username= -> 빈 문자열로 통과 <br />
     * <br />
     * 주의! <br />
     * age의 타입을 int로 할 경우, null 값을 처리하지 못하므로 예외 발생 <br />
     * -> Integer를 사용하거나, defaultValue 사용
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age
    ) {
        logParams(username, age);

        return "ok";
    }

    /**
     * &#064;RequestParam.defaultValue <br />
     * <br />
     * 참고: defaultValue는 빈 문자의 경우에도 적용 <br />
     * /request-param-default?username= -> username=guest
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age
    ) {
        logParams(username, age);

        return "ok";
    }

    /**
     * &#064;RequestParam (Map, MultiValueMap) <br />
     * Map (key=value) <br />
     * MultiValueMap (key=[value1, value2, ...]) <br />
     * <br />
     * 파라미터의 값이 1개임이 확실하다면 Map <br />
     * 그렇지 않다면 MultiValueMap을 사용 <br />
     * <br />
     * 특히 GET 메서드의 경우 사용자가 임의로 URI를 조작할 수 있다는 점에 주의
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        logParams(paramMap.get("username"), paramMap.get("age"));

        return "ok";
    }

    /**
     * &#064;ModelAttribute 사용 <br />
     * 참고: model.addAttribute(helloData) 코드도 함께 자동 적용됨
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        logParams(helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    /**
     * &#064;ModelAttribute 사용 <br />
     * <br />
     * &#064;ModelAttribute 또한 생략할 수 있다. <br />
     * <br />
     * 스프링은 파라미터에 애노테이션을 생략할 경우, 다음과 같은 규칙을 적용한다. <br />
     * - 기본형, 기본형 래퍼 클래스, String 과 같은 단순 타입: @RequestParam 적용 <br />
     * - 그 외 나머지 타입(클래스): @ModelAttribute 적용
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        logParams(helloData.getUsername(), helloData.getAge());

        return "ok";
    }
}
