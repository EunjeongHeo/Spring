package org.example.springjwt.controller;

import lombok.RequiredArgsConstructor;
import org.example.springjwt.dto.JoinDTO;
import org.example.springjwt.service.JoinService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO){

        System.out.println(joinDTO.getUsername());
        boolean success = joinService.joinProcess(joinDTO);

        if (success) {
            return "회원가입 성공";
        } else {
            return "회원가입 실패";
        }
    }
}