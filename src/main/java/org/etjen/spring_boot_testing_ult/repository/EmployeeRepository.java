package org.etjen.spring_boot_testing_ult.repository;

import org.etjen.spring_boot_testing_ult.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
