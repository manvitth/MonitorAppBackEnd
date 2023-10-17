package com.vis.monitor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vis.monitor.modal.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByeMailAndPassword(String eMail, String password);
	
	Optional<User> findByNameAndPassword(String name, String password);
   
}
