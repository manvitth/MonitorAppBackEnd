package com.vis.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vis.monitor.modal.MonitorRequest;

@Repository
public interface MonitorRequestRepository extends JpaRepository<MonitorRequest, Long> {

}
