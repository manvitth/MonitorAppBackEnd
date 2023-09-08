package com.vis.monitor.service;

import java.util.List;

import com.vis.monitor.ws.modal.Data;

public interface DataService {
	
	public Data addData(Data data);
	
	public Data updateData(Data data);
	
	public List<Data> getDatas();
	
	public Data getData(Long id);
	
	public Data deleteData(Long id);

}
