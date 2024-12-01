package hello.springmvc.basic.requestmapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@RestController
public class MappingController {
    /**
     * 기본 요청 <br />
     * /hello-basic, /hello-basic/ 요청 경로 모두 허용 (스프링부트 3.0 이전) <br />
     * HTTP 메서드 모두 허용(GET, POST, PUT, PATCH, DELETE, HEAD)
     */
    @RequestMapping("/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }

    /**
     * method: 특정 HTTP 메서드 요청만 허용 <br />
     * GET, POST, PUT, PATCH, DELETE, HEAD
     */
    @RequestMapping(value = "/mapping-get-v1", method = GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }

    /**
     * 편리한 축약 애노테이션
     * <br />
     * &#064;GetMapping <br />
     * &#064;PostMapping <br />
     * &#064;PutMapping <br />
     * &#064;PatchMapping <br />
     * &#064;DeleteMapping
     */
    @GetMapping("/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mappingGetV2");
        return "ok";
    }

    /**
     * PathVariable 사용 <br />
     * 변수명이 같으면 생략 가능 <br />
     * &#064;PathVariable("userId") String userId <br />
     * -> &#064;PathVariable String userId
     */
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "ok";
    }

    /**
     * PathVariable 다중 사용
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

    /**
     * 파라미터로 매핑 조건 추가
     * <br />
     * params="mode" <br />
     * params="!mode" <br />
     * params="mode=debug" <br />
     * params="mode!=debug" <br />
     * params={"mode=debug", "data"} <br />
     * params={"mode=debug", "data=good"}
     */
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 매핑 조건 추가
     * <br />
     * headers="mode" <br />
     * headers="!mode" <br />
     * headers="mode=debug" <br />
     * headers="mode!=debug" <br />
     * headers={"mode=debug", "data"} <br />
     * headers={"mode=debug", "data=good"}
     */
    @GetMapping(value = "/mapping-header", headers = {"mode=debug", "data=good"})
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }

    /**
     * Media Type 조건 매핑 1 - Content-Type 헤더로 매핑 조건 추가
     * <br />
     * consumes="application/json" <br />
     * consumes="!application/json" <br />
     * consumes="application/*" <br />
     * consumes="*\/*" <br />
     * MediaType.APPLICATION_JSON_VALUE <br />
     * <br />
     * 조건이 맞지 않으면 HTTP 415 상태 코드(Unsupported Media Type) 반환
     */
    @PostMapping(value = "/mapping-consume", consumes = "application/json")
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * Media Type 조건 매핑 2 - Accept 헤더로 매핑 조건 추가
     * <br />
     * produces="text/html" <br />
     * produces="!text/html" <br />
     * produces="text/*" <br />
     * produces="*\/*" <br />
     * <br />
     * 조건이 맞지 않으면 HTTP 406 상태 코드(Not Acceptable) 반환
     */
    @PostMapping(value = "/mapping-produce", produces = "text/html")
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
}
