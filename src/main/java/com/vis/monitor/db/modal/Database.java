package com.vis.monitor.db.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "database_servers")
public class Database {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "dbIdSeq")
	private Long id;
	
	@Column(unique = true)
	private String name;
	
	private String host;
	
	private Integer port;
	
	private String type;
	
	private String url;
	
	private String userName;
	
	private String password;
	
	private String instance;
	
	private Boolean isActive = false;

}
