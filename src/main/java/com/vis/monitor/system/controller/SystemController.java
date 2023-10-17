package com.vis.monitor.system.controller;

import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.vis.monitor.system.modal.System; 
import com.vis.monitor.system.service.SystemService;

@RestController
@RequestMapping("/system-metrics")
public class SystemController {
    private final SystemService metricsService;

    @Autowired
    public SystemController(SystemService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping
    public System getSystemMetrics() {
        try {
            return metricsService.getSystemMetrics();
        } catch (SigarException e) {
           
            e.printStackTrace(); 
            return new System();
        }
    }
}
