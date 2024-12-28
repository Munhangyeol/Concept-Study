package com.example.entityPractice;

import com.example.entityPractice.service.StudentService1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudentControllerBy {
    private final StudentService1 service;

    @GetMapping("/identity/1")
    public String saveWithIdentity(){
        service.saveIdentity();
        return "!!";
    }

    @GetMapping("/sequence/1")
    public String  saveWithSequence(){
       service.saveSequence();
        return "!!";
    }
}
