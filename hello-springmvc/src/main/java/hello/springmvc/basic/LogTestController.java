package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j // (Lombok 제공)
@RestController
public class LogTestController {
    // @Slf4j로 대체 가능
//    private final Logger log = LoggerFactory.getLogger(getClass());
//    private final Logger log = LoggerFactory.getLogger(LogTestController.class);

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        log.trace("trace log={}", name);
        log.debug("debug log={}", name); // debug log=Spring
        log.info(" info log={}", name); //  info log=Spring
        log.warn(" warn log={}", name); //  warn log=Spring
        log.error("error log={}", name);

        // 로그를 사용하지 않아도 a+b 계산 로직이 먼저 실행됨
        // 아래와 같은 방식으로 사용하면 X
        log.debug("String concat log=" + name);
        return "ok";
    }
}
