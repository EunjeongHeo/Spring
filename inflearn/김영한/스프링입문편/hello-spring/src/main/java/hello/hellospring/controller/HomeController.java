package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/") //localhost8080진입시 처음!
    public String home() {
        return "home"; //home.html호출
    }
}
