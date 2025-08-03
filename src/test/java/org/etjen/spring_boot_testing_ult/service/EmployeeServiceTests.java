package org.etjen.spring_boot_testing_ult.service;

import org.etjen.spring_boot_testing_ult.exception.EmployeeAlreadyExistsException;
import org.etjen.spring_boot_testing_ult.model.Employee;
import org.etjen.spring_boot_testing_ult.repository.EmployeeRepository;
import org.etjen.spring_boot_testing_ult.service.custom.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee;

    @BeforeEach
    void setUp() {
        this.employee = Employee.builder().id(1L).firstName("Test").lastName("Test").email("test@gmail.com").build();
    }

    @Test
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        // when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(employee.getId());
    }

    @Test
    void givenExistingEmail_whenSaveEmployee_thenThrowEmployeeAlreadyExists() {
        // given
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // when

        // then
        assertThrows(EmployeeAlreadyExistsException.class, () -> employeeService.saveEmployee(employee));
        verify(employeeRepository, never()).save(employee);
    }

    @Test
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // given
        Employee employee2 = Employee.builder().id(2L).firstName("Test2").lastName("Test2").email("test2@gmail.com").build();
        Employee employee3 = Employee.builder().id(3L).firstName("Test3").lastName("Test3").email("test3@gmail.com").build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2, employee3));

        // when
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.size()).isEqualTo(3);
    }

    @Test
    void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // given
        given(employeeRepository.findAll()).willReturn(List.of());

        // when
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then
        assertThat(employeeList).isEmpty();
    }

    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // given
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when
        Optional<Employee> foundEmployee = employeeService.getEmployeeById(employee.getId());

        // then
        assertThat(foundEmployee).isPresent();
        // uses lombok generated equals method to compare
        assertThat(foundEmployee.get()).isEqualTo(employee);
        // passes only if both references point to the exact same object,
        // fails if the service returns a copy with identical fields
        assertThat(foundEmployee.get()).isSameAs(employee);
    }

    @Test
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        // given
        employee.setEmail("updatedTest@gmail.com");
        given(employeeRepository.save(employee)).willReturn(employee);

        // when
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then
        verify(employeeRepository, times(1)).save(employee);
        assertThat(updatedEmployee).isNotNull();
    }

    @Test
    void givenEmployeeId_whenDeleteEmployeeById_then() {
        // given
        willDoNothing().given(employeeRepository).deleteById(employee.getId());

        // when
        employeeService.deleteEmployeeById(employee.getId());

        // then
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }
}
