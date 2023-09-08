package com.vis.monitor.controller;



import com.vis.monitor.modal.Server;
import com.vis.monitor.service.ServerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class ServerController {
	
	@Autowired
    private ServerService sService;

    
    @PostMapping("/add-server")
    public ResponseEntity<Server> addServer(@RequestBody Server server) {
        Server addedServer = sService.addServer(server);
        return ResponseEntity.ok(addedServer);
    }
    
    @PutMapping("/update-server")
    public ResponseEntity<Server> updateServer(@RequestBody Server server) {
        Server addedServer = sService.updateServer(server);
        return ResponseEntity.ok(addedServer);
    }

    @GetMapping("/get-servers")
    public ResponseEntity<List<Server>> getServers() {
        List<Server> servers = sService.getServers();
        return ResponseEntity.ok(servers);
    }

    @DeleteMapping("/delete-server/{id}")
    public ResponseEntity<Server> deleteIpPort(@PathVariable Long id) {
        Server deletedServer = sService.deleteServer(id);
        return ResponseEntity.ok(deletedServer);
    }
}
