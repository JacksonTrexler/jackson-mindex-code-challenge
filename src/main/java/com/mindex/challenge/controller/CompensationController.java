package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private CompensationService compensationService;

    @PutMapping("/compensation/{empId}")
    public Compensation update(@PathVariable String empId, @RequestBody Compensation compensation){
        LOG.debug("Received employee update request for id [{}] and employee [{}]", empId, compensation);

        compensation.setEmpId(empId);

        return compensationService.update(compensation);
    }

    @GetMapping("/compensation/{empId}")
    public Compensation read(@PathVariable String empId){
        LOG.debug("Compensation read request received");

        return compensationService.read(empId);
    }
}
