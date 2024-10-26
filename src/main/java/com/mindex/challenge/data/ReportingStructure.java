package com.mindex.challenge.data;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    //Constructor
    public ReportingStructure(Employee employee, int numberOfReports){
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    //Getters and setters
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee){
        this.employee = employee;
    }

    public int getNumberofReports(){
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports){
        this.numberOfReports = numberOfReports;
    }
}
