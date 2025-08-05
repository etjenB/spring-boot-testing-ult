package org.etjen.spring_boot_testing_ult.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.etjen.spring_boot_testing_ult.model.Employee;
import org.etjen.spring_boot_testing_ult.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;
    private Employee employee;

    @BeforeEach
    void setUp() {
        this.employee = Employee.builder().id(1L).firstName("Test").lastName("Test").email("test@gmail.com").build();
    }

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given
        //basically any employee object that saveEmployee gets is fine, and it will return that same object because we say it will answer and give first argument that was given to method
        BDDMockito.given(employeeService.saveEmployee(any(Employee.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then
        resultActions//.andDo(MockMvcResultHandlers.print())
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
                Employee.builder().id(2L).firstName("Test2").lastName("Test2").email("test2@gmail.com").build(),
                Employee.builder().id(3L).firstName("Test3").lastName("Test3").email("test3@gmail.com").build()
        );
        BDDMockito.given(employeeService.getAllEmployees()).willReturn(employeeList);

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
        BDDMockito.given(employeeService.getEmployeeById(any(Long.class))).willReturn(Optional.of(employee));

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
        Long employeeId = 1L;
        BDDMockito.given(employeeService.getEmployeeById(any(Long.class))).willReturn(Optional.empty());

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/employee/{id}", employeeId));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenEmployeeIdAndEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given
        Employee updatedEmployee = Employee.builder().firstName("Test2").lastName("Test2").email("test2@gmail.com").build();
        BDDMockito.given(employeeService.getEmployeeById(employee.getId())).willReturn(Optional.of(employee));
        BDDMockito.given(employeeService.updateEmployee(any(Employee.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

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
        Long employeeId = 1L;
        Employee updatedEmployee = Employee.builder().firstName("Test2").lastName("Test2").email("test2@gmail.com").build();
        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        BDDMockito.given(employeeService.updateEmployee(any(Employee.class))).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

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
        willDoNothing().given(employeeService).deleteEmployeeById(employee.getId());

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/employee/{id}", employee.getId()));

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
