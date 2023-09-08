package com.vis.monitor.modal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "servers")
public class Server {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "serverIdSeq")
	private Long id;

	private String name; 

	private String host;
	
	private Integer port;

	private Boolean isActive = false;
}
