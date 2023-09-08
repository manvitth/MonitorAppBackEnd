package com.vis.monitor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vis.monitor.ws.modal.WebService;

@Repository
public interface WebServiceRepository extends JpaRepository<WebService, Long> {

	Optional<WebService> findByName(String name);
}
