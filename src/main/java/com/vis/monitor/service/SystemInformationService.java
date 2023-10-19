package com.vis.monitor.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.vis.monitor.modal.DiskInformation;
import com.vis.monitor.modal.MemoryInformation;
import com.vis.monitor.modal.SystemInformation;

public interface SystemInformationService {
	
	static Queue<SystemInformation> queueSysInfo = new LinkedList<SystemInformation>();
	
	public static void addSystemInformation(SystemInformation sysInfo) {
		if(queueSysInfo.size() >= 60) {
			queueSysInfo.poll();
		}
		queueSysInfo.add(sysInfo);
	}
	
	public List<Double> getLast60SecondsUsageOfCPU();
	
	public List<MemoryInformation> getLast60SecondsUasageOfRAM();
	
	public DiskInformation getLastUasageOfROM();

}
