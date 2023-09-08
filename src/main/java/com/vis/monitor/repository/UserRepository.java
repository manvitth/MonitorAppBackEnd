package com.vis.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vis.monitor.modal.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   
}
