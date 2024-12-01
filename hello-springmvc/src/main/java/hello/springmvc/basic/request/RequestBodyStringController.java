package hello.springmvc.basic.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Controller
public class RequestBodyStringController {
    private void logBody(String messageBody) {
        log.info("messageBody={}", messageBody);
    }

    @PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, UTF_8);

        logBody(messageBody);

        response.getWriter().write("ok");
    }

    /**
     * InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회 <br />
     * OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, UTF_8);

        logBody(messageBody);

        responseWriter.write("ok");
    }

    /**
     * HttpEntity: HTTP header, body 정보를 편리하게 조회 <br />
     * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X) <br />
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용 <br />
     * <br />
     * 응답에도 HttpEntity 사용 가능 <br />
     * - 메시지 바디 정보 직접 출력(view 조회 X) <br />
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {
        String messageBody = httpEntity.getBody();
        logBody(messageBody);

        return new HttpEntity<>("ok");
    }

    /**
     * HttpEntity를 상속받은 다음 객체들도 같은 기능을 제공 <br />
     * - RequestEntity: HttpMethod, url 정보 추가. 요청에서 사용 <br />
     * - ResponseEntity: HTTP 상태 코드 설정 가능. 응답에서 사용
     */
    @PostMapping("/request-body-string-v4")
    public ResponseEntity<String> requestBodyStringV4(RequestEntity<String> requestEntity) {
        String messageBody = requestEntity.getBody();
        logBody(messageBody);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("HeaderTest", "spring");

        return new ResponseEntity<>("ok", httpHeaders, HttpStatus.OK);
    }

    /**
     * &#064;RequestBody <br />
     * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X) <br />
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용 <br />
     * <br />
     * &#064;ResponseBody  <br />
     * - 메시지 바디 정보 직접 반환(view 조회 X) <br />
     * - httpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @ResponseBody
    @PostMapping("/request-body-string-v5")
    public String requestBodyStringV5(@RequestBody String messageBody) {
        logBody(messageBody);

        return "ok";
    }
}
