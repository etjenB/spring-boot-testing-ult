package org.etjen.spring_boot_testing_ult.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.etjen.spring_boot_testing_ult.model.Employee;
import org.etjen.spring_boot_testing_ult.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        this.employee = Employee.builder().firstName("Test").lastName("Test").email("test@gmail.com").build();
    }

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() throws Exception {
        // given
        List<Employee> employeeList = List.of(
                employee,
                Employee.builder().firstName("Test2").lastName("Test2").email("test2@gmail.com").build(),
                Employee.builder().firstName("Test3").lastName("Test3").email("test3@gmail.com").build()
        );
        employeeRepository.saveAllAndFlush(employeeList);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/employee"));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
    }

    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // given
        employeeRepository.saveAndFlush(employee);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/employee/{id}", employee.getId()));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    void givenNonExistentEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        // given
        Long employeeId = 999_999_999L;

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/employee/{id}", employeeId));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenEmployeeIdAndEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given
        employeeRepository.saveAndFlush(employee);
        Employee updatedEmployee = Employee.builder().firstName("Test2").lastName("Test2").email("test2@gmail.com").build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/employee/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));
    }

    @Test
    void givenNonExistentEmployeeIdAndEmployeeObject_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        // given
        Long employeeId = 999_999_999L;
        Employee updatedEmployee = Employee.builder().firstName("Test2").lastName("Test2").email("test2@gmail.com").build();

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenEmployeeId_whenDeleteEmployee_thenReturnOk() throws Exception {
        // given
        employeeRepository.saveAndFlush(employee);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/employee/{id}", employee.getId()));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
