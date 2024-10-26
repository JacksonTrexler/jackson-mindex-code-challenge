package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class ReportingStructureImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureImpl.class);


    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure generateReportingStructure(String id) {
        //Initialize with empty HashSet to keep track of ids that have already been counted
        LOG.debug("Retrieving reporting structure from employee [{}]", id);
        return generateReportingStructure(id, new HashSet<>());
    }

    @Override
    public ReportingStructure generateReportingStructure(String id, HashSet<String> visited) {
        //Set employee and base ReportingStructure
        int count = 0;
        List<Employee> richReports = new ArrayList<>();
        Employee employee = employeeRepository.findByEmployeeId(id);

        //This is a little ugly, but keeps this recursive method a bit more self-contained
        //Don't want any problems with cyclical / duplicate reports, reports that show up again won't be counted
        if (employee == null || visited.contains(employee.getEmployeeId())) {
            return null;
        }
        visited.add(employee.getEmployeeId());
        //If the employee doesn't have any reports, just return a structure with the full employee
        //To prevent NPE exceptions and unneeded code execution if an employee doesn't have reports
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            return new ReportingStructure(employee, 0);
        }

        //For each report, create another report structure, count them as well as their reports, and update this
        //Employee's reports to have full information
        for (Employee report : employee.getDirectReports()) {
            ReportingStructure reportStructure = generateReportingStructure(report.getEmployeeId(), visited);
            if (reportStructure == null) {
                continue;
            }
            count += 1 + reportStructure.getNumberofReports();
            richReports.add(reportStructure.getEmployee());
        }
        employee.setDirectReports(richReports);
        return new ReportingStructure(employee, count);
    }
}
