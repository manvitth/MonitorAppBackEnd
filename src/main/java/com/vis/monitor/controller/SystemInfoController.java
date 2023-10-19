package com.vis.monitor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vis.monitor.modal.DiskInformation;
import com.vis.monitor.modal.MemoryInformation;
import com.vis.monitor.service.SystemInformationService;

@RestController
@RequestMapping("/system")
public class SystemInfoController {
	
	@Autowired
	private SystemInformationService sysInfoService;

    @GetMapping("/cpu")
    public List<Double> getCpuUsage() {
    	return sysInfoService.getLast60SecondsUsageOfCPU();
    }

    @GetMapping("/memory")
    public List<MemoryInformation> getMemoryUsage() {
        return sysInfoService.getLast60SecondsUasageOfRAM();
    }
    
    @GetMapping("/disk")
    public DiskInformation getDiskUsage() {
        return sysInfoService.getLastUasageOfROM();
    }
}
