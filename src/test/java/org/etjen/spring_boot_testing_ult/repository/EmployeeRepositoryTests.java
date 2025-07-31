package org.etjen.spring_boot_testing_ult.repository;

import org.etjen.spring_boot_testing_ult.model.Employee;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        Employee employee = Employee.builder().firstName("Test").lastName("Test").email("test@gmail.com").build();

        Employee savedEmployee = employeeRepository.save(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }

    @Test
    void givenEmployeeList_whenFindAll_thenReturnEmployeeList() {
        Employee employee1 = Employee.builder().firstName("Test1").lastName("Test1").email("test1@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Test2").lastName("Test2").email("test2@gmail.com").build();
        Employee employee3 = Employee.builder().firstName("Test3").lastName("Test3").email("test3@gmail.com").build();
        employeeRepository.saveAll(List.of(employee1, employee2, employee3));

        List<Employee> employeeList = employeeRepository.findAll();

        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.size()).isEqualTo(3);
    }
}
