package com.vis.monitor.controller;

import com.vis.monitor.ws.modal.WebService;
import com.vis.monitor.ws.modal.WebServiceRequest;
import com.vis.monitor.ws.modal.WebServiceResponse;
import com.vis.monitor.service.WSService;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WebServiceController {

	@Autowired
    private WSService wsService;

    @PostMapping("/add-web-service")
    public ResponseEntity<WebService> addWebService(@RequestBody WebService webService) {
    	WebService sWebService = wsService.addWebService(webService);         
        return new ResponseEntity<>(sWebService, HttpStatus.CREATED);
    }
    
    @PutMapping("/update-web-service")
    public ResponseEntity<WebService> updateWebService(@RequestBody WebService webService) {
    	WebService sWebService = wsService.updateWebService(webService);         
        return new ResponseEntity<>(sWebService, HttpStatus.OK);
    }

    @GetMapping("/get-web-services")
    public ResponseEntity<List<WebService>> getWebServices() {
        List<WebService> webServices = wsService.getWebServices();  
        return new ResponseEntity<>(webServices, HttpStatus.OK);
    }

    @GetMapping("/get-web-service/{id}")
    public ResponseEntity<WebService> getWebService(@PathVariable Long id) {
    	WebService gWebService = wsService.getWebService(id);
        return new ResponseEntity<>(gWebService, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete-web-service/{id}")
    public ResponseEntity<WebService> deleteWebService(@PathVariable Long id) {
    	WebService dWebService = wsService.deleteWebService(id);
        return new ResponseEntity<>(dWebService, HttpStatus.OK);
    }
    
    @PostMapping("/execute-web-service")
    public ResponseEntity<WebServiceResponse> executeWebService(@RequestBody WebServiceRequest request) {
    	WebServiceResponse gWebService = wsService.executeWebService(request);
        return new ResponseEntity<>(gWebService, HttpStatus.OK);
    }
}
