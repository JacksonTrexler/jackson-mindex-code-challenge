package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure generateReportingStructure(String id) {
        Employee employee = employeeRepository.findByEmployeeId(id);
        LOG.debug("Retrieving reporting structure from employee [{}]", employee.getFirstName()); //todo test null name
        return new ReportingStructure(employee, countReports(employee));
    }

    private int countReports(Employee employee) {
        int count = 0;
        for (Employee report : employee.getDirectReports()) {
            count += 1 + countReports(report);
        }
        return count;
    }
}
