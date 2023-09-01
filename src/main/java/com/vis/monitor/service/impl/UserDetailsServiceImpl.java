package com.vis.monitor.service.impl;



import com.vis.monitor.modal.IpPort;
import com.vis.monitor.modal.UserDetails;
import com.vis.monitor.repository.IpPortRepository;
import com.vis.monitor.repository.UserDetailsRepository;
import com.vis.monitor.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final IpPortRepository ipPortRepository;
    
    @Autowired
    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository,IpPortRepository ipPortRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.ipPortRepository=ipPortRepository;
        
    }


   
    
    @Override
    public UserDetails addUserDetails(UserDetails userDetails) {
   IpPort ipPort =userDetails.getIpPort();
    
    	if(ipPort !=null && ipPort.getId()==null) {
    		userDetails.setIpPort(ipPortRepository.save(ipPort));
    	}
    	return  userDetailsRepository.save(userDetails);
    }
    
    @Override
    public Optional<UserDetails> getUserDetailsById(long id) {
        return userDetailsRepository.findById(id);
    }

    @Override
    public List<UserDetails> getAllUserDetails() {
        return userDetailsRepository.findAll();
    }

    @Override
    public void deleteUserDetails(long id) {
        userDetailsRepository.deleteById(id);
    }



    
    //delete this 
    
	}





	

