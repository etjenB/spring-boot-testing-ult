package org.etjen.spring_boot_testing_ult.service;

import org.etjen.spring_boot_testing_ult.model.Employee;
import org.etjen.spring_boot_testing_ult.repository.EmployeeRepository;
import org.etjen.spring_boot_testing_ult.service.custom.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeServiceTests {
    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        this.employeeRepository = Mockito.mock(EmployeeRepository.class);
        this.employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given
        Employee employee = Employee.builder().id(1L).firstName("Test").lastName("Test").email("test@gmail.com").build();
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isEqualTo(employee.getId());
    }
}
