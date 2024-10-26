package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Compensation {


    private int salary;
    private Date effectiveDate;
    private Date stopDate;

    @Id
    private String empId;

    public Compensation(int salary, Date effectiveDate, Date stopDate, String empId) {
        this.salary = salary;
        this.effectiveDate = effectiveDate;
        this.stopDate = stopDate;
        this.empId = empId;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


}
