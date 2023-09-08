package com.vis.monitor.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vis.monitor.modal.Server;
import com.vis.monitor.repository.ServerRepository;
import com.vis.monitor.service.ServerService;

@Service
public class ServerServiceImpl implements ServerService {

	@Autowired
	private ServerRepository sRepo;
	
	@Override
	public Server addServer(Server server) {
		// TODO Auto-generated method stub
		return sRepo.save(server);
	}

	@Override
	public Server updateServer(Server server) {
		// TODO Auto-generated method stub
		return sRepo.save(server);
	}

	@Override
	public List<Server> getServers() {
		// TODO Auto-generated method stub
		return sRepo.findAll();
	}

	@Override
	public Server getServer(Long id) {
		// TODO Auto-generated method stub
		Optional<Server> oServer = sRepo.findById(id);
		return oServer.isPresent() ? oServer.get() : null;
	}

	@Override
	public Server deleteServer(Long id) {
		// TODO Auto-generated method stub
		Optional<Server> oServer = sRepo.findById(id);
		if(oServer.isPresent()) {
			sRepo.deleteById(id);
		}
		return oServer.isPresent() ? oServer.get() : null;
	}	
	
}
