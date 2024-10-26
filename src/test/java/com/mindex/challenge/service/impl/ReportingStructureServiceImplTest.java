package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
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

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    private String employeeUrl;
    private String reportingStructureUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testGenerateReportingStructure() {
        Employee testEmployeeC = new Employee();
        testEmployeeC.setFirstName("Jack");
        testEmployeeC.setLastName("Doe");
        testEmployeeC.setDepartment("Engineering");
        testEmployeeC.setPosition("Villain");
        Employee createdEmployeeC = restTemplate.postForEntity(employeeUrl, testEmployeeC, Employee.class).getBody();

        Employee testEmployeeB = new Employee();
        testEmployeeB.setFirstName("Jane");
        testEmployeeB.setLastName("Doe");
        testEmployeeB.setDepartment("Engineering");
        testEmployeeB.setPosition("PM");
        testEmployeeB.setDirectReports(Arrays.asList(createdEmployeeC));
        Employee createdEmployeeB = restTemplate.postForEntity(employeeUrl, testEmployeeB, Employee.class).getBody();


        Employee testEmployeeA = new Employee();
        testEmployeeA.setFirstName("John");
        testEmployeeA.setLastName("Doe");
        testEmployeeA.setDepartment("Engineering");
        testEmployeeA.setPosition("Developer");

        //Test report of a report to make sure that it counts immediat reports and deeper ones recursively
        testEmployeeA.setDirectReports(Arrays.asList(createdEmployeeB));

        Employee createdEmployeeA = restTemplate.postForEntity(employeeUrl, testEmployeeA, Employee.class).getBody();

        restTemplate.put(employeeUrl + "/" + createdEmployeeB.getEmployeeId(), testEmployeeB);
        restTemplate.put(employeeUrl + "/" + createdEmployeeA.getEmployeeId(), testEmployeeA);

        ReportingStructure testReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, createdEmployeeA.getEmployeeId()).getBody();
        assertNotNull(testReportingStructure);


        assertEquals(2, testReportingStructure.getNumberOfReports());
    }
}
