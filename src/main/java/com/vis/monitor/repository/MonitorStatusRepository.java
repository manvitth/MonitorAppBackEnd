package com.vis.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.vis.monitor.modal.MonitorStatus;
import com.vis.monitor.modal.User;

import java.util.List;

@Repository
public interface MonitorStatusRepository extends JpaRepository<MonitorStatus, Long> {
	List<MonitorStatus> findByUser(User user);
}
