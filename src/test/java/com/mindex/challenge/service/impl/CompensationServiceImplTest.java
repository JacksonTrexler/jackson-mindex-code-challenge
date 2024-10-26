package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    private String employeeUrl;
    private String compensationUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    CompensationService compensationService;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCompensationServiceImpl() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Jackson");
        testEmployee.setLastName("Trexler");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Software Engineer");
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        Compensation testCompensation = new Compensation(63000, new Date(), new Date(3000), createdEmployee.getEmployeeId());

        //I had some difficulty putting compensation in and getting all of / the correct values out
        restTemplate.put(compensationUrl, testCompensation, createdEmployee.getEmployeeId());
        Compensation retrievedCompensation = restTemplate.getForEntity(compensationUrl, Compensation.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals("empId mismatch", testCompensation.getEmpId(), retrievedCompensation.getEmpId());
        assertEquals("Salary mismatch", testCompensation.getSalary(), retrievedCompensation.getSalary());
        assertEquals("start date mismatch", testCompensation.getEffectiveDate(), retrievedCompensation.getEffectiveDate());
        assertEquals("end date mismatch", testCompensation.getStopDate(), retrievedCompensation.getStopDate());

        testCompensation = new Compensation(55500, new Date(), new Date(4000), createdEmployee.getEmployeeId());

        //Making sure the update works
        restTemplate.put(compensationUrl, testCompensation, createdEmployee.getEmployeeId());
        retrievedCompensation = restTemplate.getForEntity(compensationUrl, Compensation.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals("empId mismatch", testCompensation.getEmpId(), retrievedCompensation.getEmpId());
        assertEquals("Salary mismatch", testCompensation.getSalary(), retrievedCompensation.getSalary());
        assertEquals("start date mismatch", testCompensation.getEffectiveDate(), retrievedCompensation.getEffectiveDate());
        assertEquals("end date mismatch", testCompensation.getStopDate(), retrievedCompensation.getStopDate());
    }
}
