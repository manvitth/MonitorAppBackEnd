package com.vis.monitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vis.monitor.React_Login_Modal.*;

import com.vis.monitor.service.LoginService;

@RestController
@RequestMapping("/api")

public class LoginController {
@Autowired
private LoginService lService;


@PostMapping("/addLogin")
public ResponseEntity<Login> addServer(@RequestBody Login login) {
    Login addedLogin = lService.addLogin(login);
    return ResponseEntity.ok(addedLogin);
}
    
@GetMapping("/getLogin/{id}")
public ResponseEntity<Login> getLoginById(@PathVariable  Long id) {
    Login login = lService.getLoginById(id);
    if (login != null) {
        return ResponseEntity.ok(login);
    } else {
        return ResponseEntity.notFound().build();
    }
}

@GetMapping("/getAllLogins")
public ResponseEntity<List<Login>> getAllLogins() {
    List<Login> logins = lService.getAllLogins();
    return ResponseEntity.ok(logins);
}
}



    


