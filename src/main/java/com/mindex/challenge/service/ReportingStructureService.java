package com.mindex.challenge.service;

//todo unused imports
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

import java.util.HashSet;

public interface ReportingStructureService {
    ReportingStructure generateReportingStructure(String id);

    ReportingStructure generateReportingStructure(String id, HashSet<String> visited);
}
