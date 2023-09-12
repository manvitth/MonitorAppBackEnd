package com.vis.monitor.controller;

import com.vis.monitor.dto.LoginDTO;
import com.vis.monitor.modal.User;
import com.vis.monitor.service.UserService;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User dUser = userService.deleteUser(id);
        return new ResponseEntity<>(dUser, HttpStatus.OK);
    }
    
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody LoginDTO login) {
    	User dUser = userService.loginUser(login);
    	
    	if(dUser != null) {
	    	return new ResponseEntity<>(dUser, HttpStatus.OK);
        }else {
        	return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
