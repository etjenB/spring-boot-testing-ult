package org.etjen.spring_boot_testing_ult.service;

import org.etjen.spring_boot_testing_ult.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    Employee updateEmployee(Employee employee);
    void deleteEmployeeById(Long id);
}
