package hello.thymeleaf.basic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic")
public class BasicController {
    @GetMapping("/text-basic")
    public String textBasic(Model model) {
        model.addAttribute("data", "Hello Spring!");

        return "basic/text-basic"; // resources/templates/basic/text-basic.html
    }

    @GetMapping("/text-unescaped")
    public String textUnescaped(Model model) {
        model.addAttribute("data", "Hello <b>Spring!</b>");

        return "basic/text-unescaped";
    }

    @GetMapping("/variable")
    public String variable(Model model) {
        User userA = new User("userA", 10);
        model.addAttribute("user", userA);

        User.addUserListToModel(model);
        User.addUserMapToModel(model);

        return "basic/variable";
    }

    @GetMapping("/basic-objects")
    public String basicObjects(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) {
        session.setAttribute("sessionData", "Hello Session");

        // 스프링부트 3.0 이상부터는 request, response, servletContext 객체를 기본 객체로 제공하지 않는다.
        // 따라서 필요한 경우 아래와 같이 모델에 직접 추가해야 한다.
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("servletContext", request.getServletContext());

        return "basic/basic-objects";
    }

    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "Hello" + data;
        }
    }

    @GetMapping("/date")
    public String date(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());

        return "basic/date";
    }

    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");

        return "basic/link";
    }

    @GetMapping({"/hello", "/hello/{path1}", "/hello/{path1}/{path2}"})
    public String linkTest1(
            @RequestParam(required = false) String param1,
            @RequestParam(required = false) String param2,
            @PathVariable(required = false) String path1,
            @PathVariable(required = false) String path2,
            Model model
    ) {
        Map<String, String> paramMap = new HashMap<>(2);
        Map<String, String> pathMap = new HashMap<>(2);

        paramMap.put("param1", param1);
        paramMap.put("param2", param2);
        pathMap.put("path1", path1);
        pathMap.put("path2", path2);

        model.addAttribute("paramMap", paramMap);
        model.addAttribute("pathMap", pathMap);

        return "basic/hello";
    }

    @GetMapping("/literal")
    public String literal(Model model) {
        model.addAttribute("data", "Spring!");

        return "basic/literal";
    }

    @GetMapping("/operation")
    public String operation(Model model) {
        model.addAttribute("nullData", null);
        model.addAttribute("data", "Spring!");

        return "basic/operation";
    }

    @GetMapping("/attribute")
    public String attribute() {
        return "basic/attribute";
    }

    @GetMapping("/each")
    public String each(Model model) {
        User.addUserListToModel(model);

        return "basic/each";
    }

    @GetMapping("/condition")
    public String condition(Model model) {
        User.addUserListToModel(model);

        return "basic/condition";
    }

    @GetMapping("/comments")
    public String comments(Model model) {
        model.addAttribute("data", "Spring!");

        return "basic/comments";
    }

    @GetMapping("/block")
    public String block(Model model) {
        User.addUserListToModel(model);

        return "basic/block";
    }

    @GetMapping("/javascript")
    public String javascript(Model model) {
        model.addAttribute("user", new User("userA", 10));
        User.addUserListToModel(model);

        return "basic/javascript";
    }

    @Data
    @AllArgsConstructor
    private static class User {
        private String username;
        private Integer age;

        private static void addUserListToModel(Model model) {
            List<User> list = new ArrayList<>();
            list.add(new User("userA", 10));
            list.add(new User("userB", 20));
            list.add(new User("userC", 30));

            model.addAttribute("userList", list);
        }

        private static void addUserMapToModel(Model model) {
            Map<String, User> map = new HashMap<>();
            map.put("userA", new User("userA", 10));
            map.put("userB", new User("userB", 20));
            map.put("userC", new User("userC", 30));

            model.addAttribute("userMap", map);
        }
    }
}
