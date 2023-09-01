package com.vis.monitor.modal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
private String  token_id;
private String email;

@OneToOne
@JoinColumn(name = "ip_port_id")
private IpPort ipPort;

public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getToken_id() {
	return token_id;
}
public void setToken_id(String token_id) {
	this.token_id = token_id;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public IpPort getIpPort() {
	return ipPort;
}
public void setIpPort(IpPort ipPort) {
	this.ipPort = ipPort;
}
public UserDetails(long id, String token_id, String email, IpPort ipPort) {
	super();
	this.id = id;
	this.token_id = token_id;
	this.email = email;
	this.ipPort = ipPort;
}
public UserDetails() {
	super();
}
@Override
public String toString() {
	return "UserDetails [id=" + id + ", token_id=" + token_id + ", email=" + email + ", ipPort=" + ipPort + "]";
}



}