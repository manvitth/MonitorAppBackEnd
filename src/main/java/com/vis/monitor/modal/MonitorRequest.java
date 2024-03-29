package com.vis.monitor.modal;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "monitor_requests")
public class MonitorRequest {
   @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "mrIdSeq")
	private Long id;

	private String name; 

	@OneToOne(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE,}, fetch = FetchType.EAGER)
    @JoinColumn(name = "serverId", referencedColumnName = "id")
	private Server server;
	
	@ManyToMany(cascade = {CascadeType.DETACH,CascadeType.REFRESH,CascadeType.REMOVE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "userIds", referencedColumnName = "id")
	private List<User> users;
	
}
