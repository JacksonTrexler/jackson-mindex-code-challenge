package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    //Pretty bare bones but I'm just linking compensation to empIds
    //Loose coupling makes my life easier and keeps code clean
    public Compensation update(Compensation compensation){
        LOG.debug("Updating compensation [{}]", compensation);

        if (compensation == null || compensation.getEmpId() == null){
            throw new RuntimeException("Compensation object is null or empty");
        }

        //Good to verify compensation information is being added to an actual employee
        if (employeeRepository.findByEmployeeId(compensation.getEmpId()) == null){
            throw new RuntimeException("Employee not found");
        }
        return compensationRepository.save(compensation);

    }

    @Override
    public Compensation read(String empId) {
        Compensation compensation = compensationRepository.findByEmpId(empId);

        if (compensation == null){
            throw new RuntimeException("Invalid empId: " + empId);
        }

        return compensation;
    }
}
