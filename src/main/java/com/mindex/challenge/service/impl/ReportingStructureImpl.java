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
import java.util.List;

@Service
public class ReportingStructureImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure generateReportingStructure(String id) {
        //Set employee and base ReportingStructure
        int count = 0;
        List<Employee> richReports = new ArrayList<>();
        Employee employee = employeeRepository.findByEmployeeId(id);

        //This is a little ugly, but keeps this recursive method a bit more self-contained
        if (employee == null){
            return null;
        }

        //If the employee doesn't have any reports, just return a structure with the full employee
        //To prevent NPE exceptions and unneeded code execution if an employee doesn't have reports
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()){
            return new ReportingStructure(employee, 0);
        }

        //For each report, create another report structure, count them as well as their reports, and update this
        //Employee's reports to have full information
        for (Employee report : employee.getDirectReports()){
            ReportingStructure reportStructure = generateReportingStructure(report.getEmployeeId());
            if (reportStructure == null){
                continue;
            }
            count += 1 + reportStructure.getNumberofReports();
            richReports.add(reportStructure.getEmployee());
        }
        employee.setDirectReports(richReports);
        //LOG.debug("Retrieving reporting structure from employee [{}]", employee.getFirstName()); //todo test null name
        return new ReportingStructure(employee, count);
    }

    private int countReports(Employee employee) {
        int count = 0;
        List<Employee> directReports = employee.getDirectReports();
        //Short-circuited null check on report list for employees without reports
        if (directReports != null && !directReports.isEmpty()) {
            for (Employee report : directReports) {
                report = employeeRepository.findByEmployeeId(report.getEmployeeId());
                count++;
                //Can't run count reports on invalid emp id, will create NPE
                //Whether or not this throws an exception, is not counted, or counts once is up to the business
                if (report != null) {
                    count += countReports(report);
                }
                else{
                    LOG.warn("Could not retrieve someone's report: {}", employee.getFirstName());
                }
            }
        }
        System.out.println(count);
        return count;
    }

    private ReportingStructure populateReports(Employee employee){
        int count = 0;
        List<Employee> directReports = employee.getDirectReports();

        if (directReports != null && !directReports.isEmpty()) {
            for (Employee report : directReports) {
                report = employeeRepository.findByEmployeeId(report.getEmployeeId());
                count++;
                if (report != null) {

                }
            }
        }
        return null;
    }
}
