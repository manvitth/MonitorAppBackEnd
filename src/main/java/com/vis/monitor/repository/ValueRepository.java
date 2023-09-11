package com.vis.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vis.monitor.ws.modal.Value;

@Repository
public interface ValueRepository extends JpaRepository<Value, Long> {

}
