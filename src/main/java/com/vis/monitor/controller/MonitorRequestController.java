package com.vis.monitor.controller;

import com.vis.monitor.modal.MonitorRequest;
import com.vis.monitor.service.MonitorRequestService;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MonitorRequestController {

	@Autowired
    private MonitorRequestService mrService;

    @PostMapping("/add-monitor-request")
    public ResponseEntity<MonitorRequest> addMonitorRequest(@RequestBody MonitorRequest monitorRequest) {
        MonitorRequest savedMonitorRequest = mrService.addMonitorRequest(monitorRequest); 
        
        return new ResponseEntity<>(savedMonitorRequest, HttpStatus.CREATED);
    }
    
    @PutMapping("/update-monitor-request")
    public ResponseEntity<MonitorRequest> updateMonitorRequest(@RequestBody MonitorRequest monitorRequest) {
        MonitorRequest updatedMonitorRequest = mrService.updateMonitorRequest(monitorRequest); 
        
        return new ResponseEntity<>(updatedMonitorRequest, HttpStatus.OK);
    }

    @GetMapping("/get-monitor-requests")
    public ResponseEntity<List<MonitorRequest>> getMonitorRequests() {
        List<MonitorRequest> monitorRequests = mrService.getMonitorRequests();  
        return new ResponseEntity<>(monitorRequests, HttpStatus.OK);
    }
    
    @GetMapping("/get-monitor-request/{id}")
    public ResponseEntity<MonitorRequest> getMonitorRequest(@PathVariable Long id) {
        MonitorRequest monitorRequest = mrService.getMonitorRequest(id);  
        return new ResponseEntity<>(monitorRequest, HttpStatus.OK);
    }

    @DeleteMapping("/delete-monitor-request/{id}")
    public ResponseEntity<MonitorRequest> deleteMonitorRequestDetails(@PathVariable Long id) {
        MonitorRequest dMonitorRequest = mrService.deleteMonitorRequest(id);
        return new ResponseEntity<>(dMonitorRequest, HttpStatus.OK);
    }
}
