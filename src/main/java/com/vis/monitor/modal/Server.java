package com.vis.monitor.modal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class IpPort {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String ipAddress;
    private int port;
    
    @OneToOne(mappedBy = "ipPort")
    private UserDetails userDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public IpPort(Long id, String ipAddress, int port, UserDetails userDetails) {
		super();
		this.id = id;
		this.ipAddress = ipAddress;
		this.port = port;
		this.userDetails = userDetails;
	}

	public IpPort() {
		super();
	}

	@Override
	public String toString() {
		return "IpPort [id=" + id + ", ipAddress=" + ipAddress + ", port=" + port + ", userDetails=" + userDetails
				+ "]";
	}

    
    
}