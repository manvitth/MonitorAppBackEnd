package com.vis.monitor.service;

import com.vis.monitor.modal.Server;

import java.util.List;

public interface ServerService {

	public Server addServer(Server server);

	public Server updateServer(Server server);

	public List<Server> getServers();

	public Server getServer(Long id);

	public Server deleteServer(Long id);

}