package com.vis.monitor.service;

import java.util.List;

import com.vis.monitor.modal.MonitorRequest;

public interface MonitorRequestService {
	

	public MonitorRequest addMonitorRequest(MonitorRequest monitorRequest);
	
	public MonitorRequest updateMonitorRequest(MonitorRequest monitorRequest);
	
	public List<MonitorRequest> getMonitorRequests();
	
	public MonitorRequest getMonitorRequest(Long id);
	
	public MonitorRequest deleteMonitorRequest(Long id);

}
