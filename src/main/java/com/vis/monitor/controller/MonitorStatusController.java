package com.vis.monitor.controller;

import com.vis.monitor.modal.MonitorStatus;
import com.vis.monitor.service.MonitorStatusService;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MonitorStatusController {

	@Autowired
    private MonitorStatusService msService;

    @PostMapping("/add-monitor-status")
    public ResponseEntity<MonitorStatus> addMonitorStatus(@RequestBody MonitorStatus monitorStatus) {
        MonitorStatus savedMonitorStatus = msService.addMonitorStatus(monitorStatus); 
        
        return new ResponseEntity<>(savedMonitorStatus, HttpStatus.CREATED);
    }
    
    @PutMapping("/update-monitor-status")
    public ResponseEntity<MonitorStatus> updateMonitorStatus(@RequestBody MonitorStatus monitorStatus) {
        MonitorStatus updatedMonitorStatus = msService.updateMonitorStatus(monitorStatus); 
        
        return new ResponseEntity<>(updatedMonitorStatus, HttpStatus.OK);
    }

    @GetMapping("/get-monitor-statuses")
    public ResponseEntity<List<MonitorStatus>> getMonitorStatuss() {
        List<MonitorStatus> MonitorStatuss = msService.getMonitorStatuses();  
        return new ResponseEntity<>(MonitorStatuss, HttpStatus.OK);
    }
    
    @GetMapping("/get-monitor-status/{id}")
    public ResponseEntity<MonitorStatus> getMonitorStatus(@PathVariable Long id) {
        MonitorStatus MonitorStatus = msService.getMonitorStatus(id);  
        return new ResponseEntity<>(MonitorStatus, HttpStatus.OK);
    }

    @DeleteMapping("/delete-monitor-status/{id}")
    public ResponseEntity<MonitorStatus> deleteMonitorStatusDetails(@PathVariable Long id) {
        MonitorStatus dMonitorStatus = msService.deleteMonitorStatus(id);
        return new ResponseEntity<>(dMonitorStatus, HttpStatus.OK);
    }
}
