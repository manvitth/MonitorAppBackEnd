package com.vis.monitor.service;

import java.util.List;

import com.vis.monitor.dto.LoginDTO;
import com.vis.monitor.modal.User;


public interface UserService {  
	
	public User addUser(User user);
	
	public User updateUser(User user);
	
	public List<User> getUsers();
	
	public User getUser(Long id);
	
	public User deleteUser(Long id);
	
	public User loginUser(LoginDTO login);

}
