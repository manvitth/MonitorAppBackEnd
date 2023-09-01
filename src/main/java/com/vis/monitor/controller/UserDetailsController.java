package com.vis.monitor.controller;

import com.vis.monitor.modal.UserDetails;
import com.vis.monitor.service.UserDetailsService;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserDetailsController {

    private final UserDetailsService userDetailsService;

    @Autowired
    public UserDetailsController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/adddetails")
    public ResponseEntity<UserDetails> addUserDetails(@RequestBody UserDetails userDetails) {
        UserDetails savedUserDetails = userDetailsService.addUserDetails(userDetails); 
        
        return new ResponseEntity<>(savedUserDetails, HttpStatus.CREATED);
    }

    @GetMapping("/getdetails")
    public ResponseEntity<List<UserDetails>> getAllUserDetails() {
        List<UserDetails> userDetailsList = userDetailsService.getAllUserDetails();  
        return new ResponseEntity<>(userDetailsList, HttpStatus.OK);
    }

    @DeleteMapping("/deletedetails/{id}")
    public ResponseEntity<Void> deleteUserDetails(@PathVariable long id) {
        userDetailsService.deleteUserDetails(id);  // Use the service method
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
