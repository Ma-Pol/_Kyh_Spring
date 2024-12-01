package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {
    /**
     * ModelAndView 객체를 생성하면서 목표 뷰 템플릿 경로를 지정 <br />
     * 이후 ModelAndView 객체를 반환하면 해당 객체 내 데이터를 뷰 템플릿에서 조회 가능
     */
    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView mav = new ModelAndView("response/hello") // templates/response/hello.html
                .addObject("data", "hello!");

        return mav;
    }

    /**
     * Model 객체를 생성해 객체 내에 데이터를 저장 <br />
     * 이후 목표 뷰 템플릿 경로를 String으로 반환하면 Model 객체 내 데이터를 뷰 템플릿에서 조회 가능 <br />
     * <br />
     * 반환 타입이 String 인 경우 <br />
     * - @ResponseBody 가 없으면 String 경로로 뷰 리졸버가 실행되어 뷰를 찾고 렌더링 <br />
     * - @ResponseBody 가 있으면 HTTP 메시지 바디에 직접 문자열을 입력해 전달
     */
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello!");

        return "response/hello"; // templates/response/hello.html 실행
    }
}
