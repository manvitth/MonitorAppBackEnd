package com.vis.monitor.ws.modal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@lombok.Data
@Entity
@Table(name = "ws_data_values")
public class Value {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "dvIdSeq")
	private Long id;
	
	private String value;

}
