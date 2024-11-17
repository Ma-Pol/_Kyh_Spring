package hello.servlet.basic.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * http://localhost:8080/response-json
 */
@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Content-Type: application/json
        response.setContentType("application/json");
        // application/json은 스펙상 utf-8 형식을 사용하도록 정의되어있다.
        // 따라서 별도로 charset=utf-8 을 지정할 필요가 없다.

        HelloData data = new HelloData();
        data.setUsername("hello");
        data.setAge(20);

        // {"username": "kim","age": 20}
        String result = objectMapper.writeValueAsString(data);

        response.getWriter().write(result);
//        response.getOutputStream().write(result.getBytes());
    }
}
