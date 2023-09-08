package com.vis.monitor.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vis.monitor.repository.KeyValueRepository;
import com.vis.monitor.service.KeyValueService;
import com.vis.monitor.ws.modal.KeyValue;

@Service
public class KeyValueServiceImpl implements KeyValueService {

	@Autowired
	private KeyValueRepository kvRepo;
	
	@Override
	public KeyValue addKeyValue(KeyValue keyValue) {
		return kvRepo.save(keyValue);
	}

	@Override
	public KeyValue updateKeyValue(KeyValue keyValue) {
		return kvRepo.save(keyValue);
	}

	@Override
	public List<KeyValue> getKeysValues() {
		return kvRepo.findAll();
	}

	@Override
	public KeyValue getKeyValue(Long id) {
		Optional<KeyValue> oKeyValue = kvRepo.findById(id);
		return oKeyValue != null && oKeyValue.isPresent() ? oKeyValue.get() : null;
	}

	@Override
	public KeyValue deleteKeyValue(Long id) {
		Optional<KeyValue> oKeyValue = kvRepo.findById(id);
		if(oKeyValue != null && oKeyValue.isPresent()) { kvRepo.deleteById(id); }
		return oKeyValue != null && oKeyValue.isPresent() ? oKeyValue.get() : null;
	}

}
