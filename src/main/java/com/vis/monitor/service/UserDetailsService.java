package com.vis.monitor.service;



import com.vis.monitor.modal.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserDetailsService {

    UserDetails addUserDetails(UserDetails userDetails);

    Optional<UserDetails> getUserDetailsById(long id);

    List<UserDetails> getAllUserDetails();

    void deleteUserDetails(long id);

    
}
