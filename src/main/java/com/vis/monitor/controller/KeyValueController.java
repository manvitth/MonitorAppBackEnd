package com.vis.monitor.controller;

import com.vis.monitor.ws.modal.KeyValue;
import com.vis.monitor.service.KeyValueService;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class KeyValueController {

	@Autowired
    private KeyValueService kvService;

    @PostMapping("/add-key-value")
    public ResponseEntity<KeyValue> addKeyValue(@RequestBody KeyValue keyValue) {
    	KeyValue savedKeyValue = kvService.addKeyValue(keyValue);         
        return new ResponseEntity<>(savedKeyValue, HttpStatus.CREATED);
    }
    
    @PutMapping("/update-key-value")
    public ResponseEntity<KeyValue> updateKeyValue(@RequestBody KeyValue keyValue) {
    	KeyValue savedKeyValue = kvService.updateKeyValue(keyValue);         
        return new ResponseEntity<>(savedKeyValue, HttpStatus.OK);
    }

    @GetMapping("/get-keys-values")
    public ResponseEntity<List<KeyValue>> getKeysValues() {
        List<KeyValue> users = kvService.getKeysValues();  
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/get-key-value/{id}")
    public ResponseEntity<KeyValue> getKeyValue(@PathVariable Long id) {
    	KeyValue gKeyValue = kvService.getKeyValue(id);
        return new ResponseEntity<>(gKeyValue, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete-key-value/{id}")
    public ResponseEntity<KeyValue> deleteKeyValue(@PathVariable Long id) {
    	KeyValue dKeyValue = kvService.deleteKeyValue(id);
        return new ResponseEntity<>(dKeyValue, HttpStatus.OK);
    }
}
