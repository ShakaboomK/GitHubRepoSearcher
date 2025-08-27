package com.KKStands.GitHubExtends.GitHubRepoKK.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class Health {
    @GetMapping
    public ResponseEntity<String> check() {
        return new ResponseEntity<>("<h1>Hello There!</h1>",  HttpStatus.OK);
    }
}
