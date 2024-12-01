package hello.springmvc.basic.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 요청 데이터 형태
 * {"username":"hello", "age":20}
 * content-type: application/json
 */
@Slf4j
@Controller
public class RequestBodyJsonController {
    private ObjectMapper objectMapper = new ObjectMapper();

    private void logBody(String messageBody) {
        log.info("messageBody={}", messageBody);
    }

    private void logData(String messageBody) throws JsonProcessingException {
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        logData(data);
    }

    private void logData(HelloData data) {
        log.info("username={}, age={}", data.getUsername(), data.getAge());
    }

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, UTF_8);

        logBody(messageBody);
        logData(messageBody);

        response.getWriter().write("ok");
    }

    /**
     * &#064;RequestBody <br />
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용 <br />
     * <br />
     * &#064;ResponseBody  <br />
     * - 모든 메서드에 @ResponseBody 적용 <br />
     * - 메시지 바디 정보 직접 반환(view 조회 X) <br />
     * - httpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        logData(messageBody);

        return "ok";
    }

    /**
     * &#064;RequestBody 생략 불가능(생략시 @ModelAttribute 가 적용됨) <br />
     * - HttpMessageConverter 사용 <br />
     * &nbsp;&nbsp;-> MappingJackson2HttpMessageConverter (content-type:application/json)
     */
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData data) {
        logData(data);

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> helloEntity) {
        HelloData data = helloEntity.getBody();
        logData(data);

        return "ok";
    }

    /**
     * &#064;RequestBody 생략 불가능(생략시 @ModelAttribute 가 적용됨) <br />
     * - HttpMessageConverter 사용 <br />
     * &nbsp;&nbsp;-> MappingJackson2HttpMessageConverter (content-type:application/json) <br />
     * <br />
     * &#064;ResponseBody 적용 <br />
     * - 메시지 바디 정보 직접 반환(view 조회 X) <br />
     * - HttpMessageConverter 사용 <br />
     * &nbsp;&nbsp;-> MappingJackson2HttpMessageConverter 적용(Accept:application/json)
     */
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
        logData(data);

        return data;
    }
}
