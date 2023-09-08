package com.vis.monitor.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vis.monitor.modal.MonitorStatus;
import com.vis.monitor.modal.User;
import com.vis.monitor.repository.MonitorStatusRepository;
import com.vis.monitor.service.MonitorStatusService;

@Service
public class MonitorStatusServiceImpl implements MonitorStatusService {

	@Autowired
	private MonitorStatusRepository msRepo;
	
	@Override
	public List<MonitorStatus> getMonitorStatusesByUser(User user) {
		// TODO Auto-generated method stub
		return msRepo.findByUser(user);
	}

	@Override
	public MonitorStatus addMonitorStatus(MonitorStatus monitorStatus) {
		// TODO Auto-generated method stub
		return msRepo.save(monitorStatus);
	}

	@Override
	public MonitorStatus updateMonitorStatus(MonitorStatus monitorStatus) {
		// TODO Auto-generated method stub
		return msRepo.save(monitorStatus);
	}

	@Override
	public List<MonitorStatus> getMonitorStatuses() {
		// TODO Auto-generated method stub
		return msRepo.findAll();
	}

	@Override
	public MonitorStatus getMonitorStatus(Long id) {
		// TODO Auto-generated method stub
		Optional<MonitorStatus> oMonitorStatus = msRepo.findById(id);
		return oMonitorStatus.isPresent() ? oMonitorStatus.get() : null;
	}

	@Override
	public MonitorStatus deleteMonitorStatus(Long id) {
		// TODO Auto-generated method stub
		Optional<MonitorStatus> oMonitorStatus = msRepo.findById(id);
		if(oMonitorStatus.isPresent()) {
			msRepo.deleteById(id);
		}
		return oMonitorStatus.isPresent() ? oMonitorStatus.get() : null;
	}

}
