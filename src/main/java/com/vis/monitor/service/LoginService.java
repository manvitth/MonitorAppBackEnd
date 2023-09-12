package com.vis.monitor.service;

import com.vis.monitor.React_Login_Modal.Login;

import java.util.List;

public interface LoginService {

    Login addLogin(Login login);

    Login getLoginById(Long id); // New method for retrieving a single login by ID

    List<Login> getAllLogins(); // New method for retrieving all logins
}
