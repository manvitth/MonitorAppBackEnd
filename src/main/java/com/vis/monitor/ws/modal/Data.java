package com.vis.monitor.ws.modal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@lombok.Data
@Entity
@Table(name = "ws_datas")
public class Data {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "dataIdSeq")
	private Long id;
	
	private String name;
	
	private String description;
	
	private String type;
	
	@OneToMany(cascade = {CascadeType.DETACH,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE, CascadeType.ALL})
    @JoinColumn(name = "data_values", referencedColumnName = "id")
	private List<Value> dataValues;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	public List<Value> getDataValues(){
		return this.dataValues;
	}
	
	@JsonProperty(access = Access.WRITE_ONLY)
	public String getValue() {
		return dataValues != null && dataValues.size() >= 1 ? dataValues.get(0) != null ? dataValues.get(0).getValue() : null : null;
	}
	
	public List<String> getValues(){
		List<String> strValues = new ArrayList<String>();
		
		if(this.dataValues != null) {
			this.dataValues.forEach(vl -> {
				strValues.add(vl.getValue());
			});
		}
		
		return strValues;
	}
	
	public void setValue(String value) {
		if(this.dataValues == null ) {
			this.dataValues = new ArrayList<Value>();
		}
		
		Value v = new Value();
		
		v.setValue(value);
		
		this.dataValues.add(v);
	}
	
	public void setValue(List<String> values) {
		if(this.dataValues == null ) {
			this.dataValues = new ArrayList<Value>();
		}
		
		values.forEach(v -> {
			Value vl = new Value();
			vl.setValue(v);
			this.dataValues.add(vl);
			});
		
	}

}
