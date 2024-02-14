package com.group.libraryapp.controller.calculator;

import com.group.libraryapp.controller.dto.claculator.request.CalculatorAddRequest;
import com.group.libraryapp.controller.dto.claculator.request.CalculatorMultiplyRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // API 를 개발하려고 하는 클래스 위에 적어주면 이 클래스를 Controller(= API 의 입구)로 등록한다.
                // 즉, 이 클래스를 API 의 진입 지점으로 만들어 준다.
public class CalculatorController {

    @GetMapping("/add") // GET /add
    public int addTwoNumbers(CalculatorAddRequest request){
        return request.getNumber1() + request.getNumber2();
    }

    @PostMapping("/multiply") // POST /multiply
    public int multiplyTwoNumbers(@RequestBody CalculatorMultiplyRequest request){
        return request.getNumber1() * request.getNumber2();
    }
}


