package com.vis.monitor.controller;

import com.vis.monitor.dto.LoginDTO;
import com.vis.monitor.modal.User;
import com.vis.monitor.service.UserService;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
    private UserService userService;

   

    @PostMapping("/add-user")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User savedUser = userService.addUser(user); 
        
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    
    @PutMapping("/update-user")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User savedUser = userService.updateUser(user); 
        
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();  
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping("/get-user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User dUser = userService.getUser(id);
        return new ResponseEntity<>(dUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User dUser = userService.deleteUser(id);
        return new ResponseEntity<>(dUser, HttpStatus.OK);
    }
    
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(HttpServletResponse response, @RequestBody LoginDTO login) {
    	User dUser = userService.loginUser(login);

    	if(dUser != null) {
    		Cookie cookie = new Cookie("User", String.valueOf(dUser.getId()));
    		
//    		cookie.setHttpOnly(Boolean.TRUE);
    		cookie.setPath("/");
    		cookie.setMaxAge(1800);
    		
    		response.addCookie(cookie);
    		
	    	return new ResponseEntity<User>(dUser, HttpStatus.OK);
        }else {
        	return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
