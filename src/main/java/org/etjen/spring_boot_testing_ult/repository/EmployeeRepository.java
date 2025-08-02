package org.etjen.spring_boot_testing_ult.repository;

import org.etjen.spring_boot_testing_ult.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    @NativeQuery("""
            SELECT *
            FROM employees
            WHERE first_name = :firstName and last_name = :lastName
            """)
    Optional<Employee> findByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
