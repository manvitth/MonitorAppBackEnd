package com.vis.monitor.repository;

import com.vis.monitor.modal.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpPortRepository extends JpaRepository<IpPort, Long> {
}