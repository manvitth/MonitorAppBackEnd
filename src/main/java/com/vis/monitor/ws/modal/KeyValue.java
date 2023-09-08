package com.vis.monitor.ws.modal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@lombok.Data
@Entity
@Table(name = "ws_keys_values")
public class KeyValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "kvIdSeq")
	private Long id;
	
	@Column(name = "ws_key")
	private String key;
	
	@Column(name = "ws_value")
	private String value;
}
