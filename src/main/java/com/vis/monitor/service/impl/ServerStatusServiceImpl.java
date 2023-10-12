package com.vis.monitor.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vis.monitor.modal.Server;
import com.vis.monitor.modal.ServerStatus;
import com.vis.monitor.repository.ServerStatusRepository;
import com.vis.monitor.service.ServerStatusService;

@Service
public class ServerStatusServiceImpl implements ServerStatusService {

	@Autowired
	private ServerStatusRepository sRepo;
	
	@Override
	public ServerStatus addServerStatus(ServerStatus serverStatus) {
		// TODO Auto-generated method stub
		return sRepo.save(serverStatus);
	}

	@Override
	public ServerStatus updateServerStatus(ServerStatus serverStatus) {
		// TODO Auto-generated method stub
		return sRepo.save(serverStatus);
	}

	@Override
	public List<ServerStatus> getServerStatuses() {
		// TODO Auto-generated method stub
		return sRepo.findAll();
	}

	@Override
	public ServerStatus getServerStatusById(Long id) {
		// TODO Auto-generated method stub
		Optional<ServerStatus> oServer = sRepo.findById(id);
		return oServer.isPresent() ? oServer.get() : null;
	}
	
	@Override
	public ServerStatus getServerStatusByServer(Server server) {
		// TODO Auto-generated method stub
		Optional<ServerStatus> oServer = sRepo.findByServerAndUpAtNull(server);
		return oServer.isPresent() ? oServer.get() : null;
	}
	
	@Override
	public ServerStatus getTopServerStatusByServer(Server server) {
		// TODO Auto-generated method stub
		Optional<ServerStatus> oServer = sRepo.findTopByServerOrderByDownAtDesc(server);
		return oServer.isPresent() ? oServer.get() : null;
	}

	@Override
	public ServerStatus deleteServerStatus(Long id) {
		// TODO Auto-generated method stub
		Optional<ServerStatus> oServer = sRepo.findById(id);
		if(oServer.isPresent()) {
			sRepo.deleteById(id);
		}
		return oServer.isPresent() ? oServer.get() : null;
	}
	
	public List<ServerStatus> getServerStatusByServerAndStartTimeAndEndTime(Server server, Date startDate, Date endDate){
		
		List<ServerStatus> serverStatuses = sRepo.findByServerAndUpAtBetweenOrderByDownAt(server, startDate, endDate);
		
		List<ServerStatus> serverUpAtNullStatuses = sRepo.findByServerAndUpAtNullOrderByDownAt(server);
		
		serverStatuses.addAll(serverUpAtNullStatuses);
		
		return serverStatuses; 
		
	}
	
}
