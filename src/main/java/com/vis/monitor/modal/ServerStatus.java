package com.vis.monitor.modal;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
@Table(name = "server_statuses")
public class ServerStatus {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "ssIdSeq")
    private Long id;
	
	@OneToOne(cascade = {CascadeType.DETACH,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE, CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "serverId", referencedColumnName = "id")
	private Server server;
	
	@JsonFormat(timezone = "IST")
	private Date downAt;
	
	@JsonFormat(timezone = "IST")
	private Date upAt;
	
	private Long duration;

}
