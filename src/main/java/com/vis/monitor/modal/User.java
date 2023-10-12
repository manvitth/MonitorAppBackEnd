package com.vis.monitor.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.vis.monitor.modal.enums.UserType;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "userIdSeq")
	private Long id;

	private String name; 

	private String phoneNumber;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@Column(unique = true)
	private String eMail;

	@Enumerated(EnumType.STRING)
	private UserType type;	
		
}
