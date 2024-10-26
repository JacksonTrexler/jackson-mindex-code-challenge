package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingStructureImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureImpl.class);

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
}
