package com.vis.monitor.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vis.monitor.repository.DataRepository;
import com.vis.monitor.service.DataService;
import com.vis.monitor.ws.modal.Data;

@Service
public class DataServiceImpl implements DataService {

	@Autowired
	private DataRepository dRepo;
	
	@Override
	public Data addData(Data data) {
		return dRepo.save(data);
	}

	@Override
	public Data updateData(Data data) {
		return dRepo.save(data);
	}

	@Override
	public List<Data> getDatas() {
		return dRepo.findAll();
	}

	@Override
	public Data getData(Long id) {
		Optional<Data> oData = dRepo.findById(id);
		return oData != null && oData.isPresent() ? oData.get() : null;
	}

	@Override
	public Data deleteData(Long id) {
		Optional<Data> oData = dRepo.findById(id);
		if(oData != null && oData.isPresent()) { dRepo.deleteById(id); }
		return oData != null && oData.isPresent() ? oData.get() : null;		
	}

}
