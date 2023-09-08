package com.vis.monitor.controller;

import com.vis.monitor.ws.modal.Data;
import com.vis.monitor.service.DataService;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {

	@Autowired
    private DataService dService;

    @PostMapping("/add-ws-data")
    public ResponseEntity<Data> addData(@RequestBody Data data) {
    	Data sData = dService.addData(data);         
        return new ResponseEntity<>(sData, HttpStatus.CREATED);
    }
    
    @PutMapping("/update-ws-data")
    public ResponseEntity<Data> updateData(@RequestBody Data data) {
    	Data sData = dService.updateData(data);         
        return new ResponseEntity<>(sData, HttpStatus.OK);
    }

    @GetMapping("/get-ws-datas")
    public ResponseEntity<List<Data>> getDatas() {
        List<Data> datas = dService.getDatas();  
        return new ResponseEntity<>(datas, HttpStatus.OK);
    }

    @GetMapping("/get-ws-data/{id}")
    public ResponseEntity<Data> getData(@PathVariable Long id) {
    	Data gData = dService.getData(id);
        return new ResponseEntity<>(gData, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete-ws-data/{id}")
    public ResponseEntity<Data> deleteData(@PathVariable Long id) {
    	Data dData = dService.deleteData(id);
        return new ResponseEntity<>(dData, HttpStatus.OK);
    }
}
