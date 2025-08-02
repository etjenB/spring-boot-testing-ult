package org.etjen.spring_boot_testing_ult.repository;

import jakarta.persistence.EntityManager;
import org.etjen.spring_boot_testing_ult.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // given
        Employee employee = Employee.builder().firstName("Test").lastName("Test").email("test@gmail.com").build();

        // when
        Employee savedEmployee = employeeRepository.save(employee);

        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }

    @Test
    void givenEmployeeList_whenFindAll_thenReturnEmployeeList() {
        // given
        Employee employee1 = Employee.builder().firstName("Test1").lastName("Test1").email("test1@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Test2").lastName("Test2").email("test2@gmail.com").build();
        Employee employee3 = Employee.builder().firstName("Test3").lastName("Test3").email("test3@gmail.com").build();
        employeeRepository.saveAll(List.of(employee1, employee2, employee3));

        // when
        List<Employee> employeeList = employeeRepository.findAll();

        // then
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.size()).isEqualTo(3);
    }

    @Test
    void givenEmployeeObject_whenFindById_thenReturnSavedEmployee() {
        // given
        Employee employee = Employee.builder().firstName("Test").lastName("Test").email("test@gmail.com").build();
        Employee savedEmployee = employeeRepository.save(employee);

        // when
        Employee dbEmployee = employeeRepository.findById(savedEmployee.getId()).orElseThrow();

        // then
        assertThat(dbEmployee.getId()).isEqualTo(savedEmployee.getId());
    }

    @Test
    void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee() {
        // given
        Employee employee = Employee.builder().firstName("Test").lastName("Test").email("test@gmail.com").build();
        Employee savedEmployee = employeeRepository.save(employee);

        // when
        Optional<Employee> dbEmployee = employeeRepository.findByEmail(savedEmployee.getEmail());

        // then
        assertThat(dbEmployee).isPresent();
        assertThat(dbEmployee.get().getId()).isEqualTo(savedEmployee.getId());
    }

    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given
        Employee employee = Employee.builder().firstName("Test").lastName("Test").email("test@gmail.com").build();
        Employee savedEmployee = employeeRepository.save(employee);

        // when
        savedEmployee.setFirstName("TestNew");
        savedEmployee.setLastName("TestNew");
        savedEmployee.setEmail("testnew@gmail.com");
        employeeRepository.saveAndFlush(savedEmployee);
        entityManager.clear();

        // then
        Optional<Employee> updatedEmployee = employeeRepository.findById(savedEmployee.getId());
        assertThat(updatedEmployee).isPresent();
        assertThat(updatedEmployee.get().getId()).isEqualTo(savedEmployee.getId());
        assertThat(updatedEmployee.get().getFirstName()).isEqualTo(savedEmployee.getFirstName());
        assertThat(updatedEmployee.get().getLastName()).isEqualTo(savedEmployee.getLastName());
        assertThat(updatedEmployee.get().getEmail()).isEqualTo(savedEmployee.getEmail());
    }

    @Test
    void givenEmployeeObject_whenDelete_thenDeletedEmployeeObject() {
        // given
        Employee employee = Employee.builder().firstName("Test").lastName("Test").email("test@gmail.com").build();
        Employee savedEmployee = employeeRepository.saveAndFlush(employee);
        entityManager.clear();

        // when
        employeeRepository.deleteById(savedEmployee.getId());
        employeeRepository.flush();
        entityManager.clear();

        // then
        Optional<Employee> dbEmployee = employeeRepository.findById(savedEmployee.getId());
        assertThat(dbEmployee).isEmpty();
    }

    @Test
    void givenEmployeeFirstNameAndLastName_whenFindByFullName_thenReturnEmployeeObject() {
        // given
        Employee employee = Employee.builder().firstName("Test").lastName("Test").email("test@gmail.com").build();
        Employee savedEmployee = employeeRepository.saveAndFlush(employee);
        entityManager.clear();

        // when
        Optional<Employee> dbEmployee = employeeRepository.findByFullName(savedEmployee.getFirstName(), savedEmployee.getLastName());

        // then
        assertThat(dbEmployee).isPresent();
        assertThat(dbEmployee.get().getId()).isEqualTo(savedEmployee.getId());
        assertThat(dbEmployee.get().getFirstName()).isEqualTo(savedEmployee.getFirstName());
        assertThat(dbEmployee.get().getLastName()).isEqualTo(savedEmployee.getLastName());
        assertThat(employeeRepository.findByFullName("Nope", "Nobody")).isEmpty();
    }
}
