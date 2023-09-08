package com.vis.monitor.service;

import java.util.List;


import com.vis.monitor.modal.MonitorStatus;
import com.vis.monitor.modal.User;


public interface MonitorStatusService {
	
	public List<MonitorStatus> getMonitorStatusesByUser(User user);
	
	public MonitorStatus addMonitorStatus(MonitorStatus monitorStatus);

	public MonitorStatus updateMonitorStatus(MonitorStatus monitorStatus);

	public List<MonitorStatus> getMonitorStatuses();

	public MonitorStatus getMonitorStatus(Long id);

	public MonitorStatus deleteMonitorStatus(Long id);

}
