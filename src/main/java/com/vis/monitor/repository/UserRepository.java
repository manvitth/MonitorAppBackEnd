package com.vis.monitor.repository;




import com.vis.monitor.modal.UserDetails;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    

   
}
