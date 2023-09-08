package com.vis.monitor.service;


	
import com.vis.monitor.modal.IpPort;

import java.util.List;
public interface IpPortService {


    IpPort addIpPort(IpPort ipPortMonitor);
    
    List<IpPort> getAllIpPorts();
    
    void deleteIpPort(Long id);
}