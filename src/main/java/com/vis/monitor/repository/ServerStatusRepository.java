package com.vis.monitor.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vis.monitor.modal.Server;
import com.vis.monitor.modal.ServerStatus;

public interface ServerStatusRepository extends JpaRepository<com.vis.monitor.modal.ServerStatus, Long> {

	Optional<ServerStatus> findByServerAndUpAtNull(Server server);
	
	Optional<ServerStatus> findTopByServerOrderByDownAtDesc(Server server);
	
	List<ServerStatus> findByServerAndUpAtBetweenOrderByDownAt(Server server, Date startDate, Date endDate);
	
	List<ServerStatus> findByServerAndUpAtNullOrderByDownAt(Server server);
	
	
}
