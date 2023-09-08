package com.vis.monitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vis.monitor.ws.modal.Data;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {

}
