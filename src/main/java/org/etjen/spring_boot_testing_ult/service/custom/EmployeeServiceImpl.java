package org.etjen.spring_boot_testing_ult.service.custom;

import org.etjen.spring_boot_testing_ult.exception.EmployeeAlreadyExistsException;
import org.etjen.spring_boot_testing_ult.model.Employee;
import org.etjen.spring_boot_testing_ult.repository.EmployeeRepository;
import org.etjen.spring_boot_testing_ult.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> foundEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (foundEmployee.isPresent()) {
            throw new EmployeeAlreadyExistsException("Employee with same email already exists: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }
}
