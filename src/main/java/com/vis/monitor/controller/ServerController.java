package com.vis.monitor.controller;



import com.vis.monitor.modal.IpPort;
import com.vis.monitor.service.IpPortService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class IpPortController {
    private final IpPortService ipPortService;

    @Autowired
    public IpPortController(IpPortService ipPortService) {
        this.ipPortService = ipPortService;
    }

    @PostMapping("/addipport")
    public ResponseEntity<IpPort> addIpPort(@RequestBody IpPort ipPort) {
        IpPort addedIpPort = ipPortService.addIpPort(ipPort);
        return ResponseEntity.ok(addedIpPort);
    }

    @GetMapping("/getipport")
    public ResponseEntity<List<IpPort>> getAllIpPorts() {
        List<IpPort> ipPorts = ipPortService.getAllIpPorts();
        return ResponseEntity.ok(ipPorts);
    }

    @DeleteMapping("/deleteipport/{id}")
    public ResponseEntity<Void> deleteIpPort(@PathVariable Long id) {
        ipPortService.deleteIpPort(id);
        return ResponseEntity.noContent().build();
    }
}
