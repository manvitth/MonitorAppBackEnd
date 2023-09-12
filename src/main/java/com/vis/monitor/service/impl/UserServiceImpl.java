package com.vis.monitor.service.impl;

import com.vis.monitor.dto.LoginDTO;
import com.vis.monitor.modal.User;
import com.vis.monitor.repository.UserRepository;
import com.vis.monitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository uRepo;
	
	@Override
	public User addUser(User user) {
		// TODO Auto-generated method stub
		return uRepo.save(user);
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return uRepo.save(user);
	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return uRepo.findAll();
	}

	@Override
	public User getUser(Long id) {
		// TODO Auto-generated method stub
		Optional<User> oUser = uRepo.findById(id);
		return oUser.isPresent() ?  oUser.get() : null;
	}

	@Override
	public User deleteUser(Long id) {
		// TODO Auto-generated method stub
		Optional<User> oUser = uRepo.findById(id);
		if(oUser.isPresent()) 
		{
			uRepo.deleteById(id);
		}
		return oUser.isPresent() ? oUser.get() : null; 
	}

	@Override
	public User loginUser(LoginDTO login) {
		// TODO Auto-generated method stub
		
		String eMail = login.getUsername();
		String password = login.getPassword();
		
		Optional<User> oUser = uRepo.findByeMailAndPassword(eMail, password);
		
		return oUser.isPresent() ? oUser.get() : null;
	}



}
