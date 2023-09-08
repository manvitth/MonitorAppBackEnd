package com.vis.monitor.service;

import java.util.List;

import com.vis.monitor.ws.modal.KeyValue;

public interface KeyValueService {
	
	public KeyValue addKeyValue(KeyValue keyValue);
	
	public KeyValue updateKeyValue(KeyValue keyValue);
	
	public List<KeyValue> getKeysValues();
	
	public KeyValue getKeyValue(Long id);
	
	public KeyValue deleteKeyValue(Long id);

}
