package com.vis.monitor.service;

import java.util.Date;
import java.util.List;

import com.vis.monitor.modal.Server;
import com.vis.monitor.modal.ServerStatus;

public interface ServerStatusService {
	
	public ServerStatus addServerStatus(ServerStatus server);

	public ServerStatus updateServerStatus(ServerStatus server);

	public List<ServerStatus> getServerStatuses();

	public ServerStatus getServerStatusById(Long id);
	
	public ServerStatus getServerStatusByServer(Server server);
	
	public ServerStatus getTopServerStatusByServer(Server server);

	public ServerStatus deleteServerStatus(Long id);
	
	public List<ServerStatus> getServerStatusByServerAndStartTimeAndEndTime(Server server, Date startDate, Date endDate);

}
