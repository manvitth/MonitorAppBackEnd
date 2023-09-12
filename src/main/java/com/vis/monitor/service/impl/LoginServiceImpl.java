package com.vis.monitor.service.impl;

import com.vis.monitor.React_Login_Modal.Login;
import com.vis.monitor.repository.LoginRepository;
import com.vis.monitor.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRepository lRepo;

    @Override
    public Login addLogin(Login login) {
        return lRepo.save(login);
    }

    @Override
    public Login getLoginById(Long id) {
        Optional<Login> ologin = lRepo.findById(id);
        return ologin.orElse(null);
    }

    @Override
    public List<Login> getAllLogins() {
        return lRepo.findAll();
    }
}
