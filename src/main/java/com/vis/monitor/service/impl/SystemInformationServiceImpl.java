package com.vis.monitor.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.vis.monitor.modal.DiskInformation;
import com.vis.monitor.modal.MemoryInformation;
import com.vis.monitor.modal.SystemInformation;
import com.vis.monitor.service.SystemInformationService;

@Service
public class SystemInformationServiceImpl implements SystemInformationService {

	
	@Override
	public List<Double> getLast60SecondsUsageOfCPU() {
		List<Double> cpuUsages = new ArrayList<Double>();
		
		Iterator<SystemInformation> iterator = queueSysInfo.iterator();
	    while (iterator.hasNext()) {
	        cpuUsages.add(iterator.next().getCpuUsage());
	    }
		
		return cpuUsages;
	}

	@Override
	public List<MemoryInformation> getLast60SecondsUasageOfRAM() {
		List<MemoryInformation> ramUsages = new ArrayList<MemoryInformation>();
		
		Iterator<SystemInformation> iterator = queueSysInfo.iterator();
	    while (iterator.hasNext()) {
	        ramUsages.add(iterator.next().getMemoryInfo());
	    }
		
		return ramUsages;
	}

	@Override
	public DiskInformation getLastUasageOfROM() {
		DiskInformation romUsage = new DiskInformation();
		
		Iterator<SystemInformation> iterator = queueSysInfo.iterator();
	    while (iterator.hasNext()) {
	        romUsage = iterator.next().getDiskInfo();
	    }
		
		return romUsage;
	}

}
