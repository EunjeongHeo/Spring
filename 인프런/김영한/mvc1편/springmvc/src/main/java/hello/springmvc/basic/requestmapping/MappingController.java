package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class MappingController {
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * @RequestMapping 에 method 속성으로 HTTP 메서드를 지정하지 않으면 HTTP 메서드와 무관하게 호출된다.
     * 1. @RequestMapping("/hello-basic") //모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE
     * 2. @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET) // 특정 HTTP 메서드 GET 요청만 허용
     * 3. 특정 HTTP 메서드 요청만 허용하는 편리한 축약 애노테이션
     * * @GetMapping
     * * @PostMapping
     * * @PutMapping
     * * @DeleteMapping
     * * @PatchMapping
     */

    @RequestMapping("/hello-basic") //다중 설정도 가능. {"/hello-basic", "/hello-go"}
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }

    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }

    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }


    /**
     * PathVariable(경로 변수) 사용
     * 이 때,@PathVariable 의 이름과 파라미터 이름이 같으면 생략할 수 있다.
     * ㄴ> @PathVariable("userId") String userId -> @PathVariable String userId
     */
    @GetMapping("/mapping/{userId}") //ex. /mapping/userA
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "ok";
    }

    /**
     * PathVariable(경로 변수) 사용 다중
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}") //ex. /mapping/users/userA/orders/100
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }


    /**
     * 파라미터로 추가 매핑 //특정 파라미터가 있거나 없는 조건을 추가할 수 있다. 잘 사용하지는 않는다.
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    @GetMapping(value = "/mapping-param", params = "mode=debug") //ex. /mapping-param?mode=debug
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )

     * 파라미터 매핑과 비슷하지만, HTTP 헤더를 사용한다.
     * Postman으로 테스트 해야 한다.
     */
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }


    /**
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * Accept 헤더 기반 Media Type
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
    @PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }

}
