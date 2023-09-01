package com.vis.monitor.service.impl;


import com.vis.monitor.service.IpPortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vis.monitor.modal.*;
import com.vis.monitor.repository.*;

import java.util.List;

@Service
public class IpPortServiceImpl implements IpPortService {

    private final IpPortRepository ipPortRepository;

    @Autowired
    public IpPortServiceImpl(IpPortRepository ipPortRepository) {
        this.ipPortRepository = ipPortRepository;
    }

    @Override
    public IpPort addIpPort(IpPort ipPort) {
        return ipPortRepository.save(ipPort);
    }

    @Override
    public List<IpPort> getAllIpPorts() {
        return ipPortRepository.findAll();
    }

    @Override
    public void deleteIpPort(Long id) {
        ipPortRepository.deleteById(id);
    }
}
