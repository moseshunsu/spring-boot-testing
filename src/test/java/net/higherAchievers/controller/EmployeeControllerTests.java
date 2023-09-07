package net.higherAchievers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.higherAchievers.entity.Employee;
import net.higherAchievers.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("Moses")
                .lastName("Hunsu")
                .email("moses@yahoo.com")
                .build();
    }


    // JUnit test for create employee REST API
    @DisplayName("JUnit test for create employee REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(APPLICATION_JSON) //APPLICATION_JSON from MediaType
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    // JUnit test for Get All employees REST API
    @DisplayName("JUnit test for Get All employees REST API")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        // given - precondition or setup
        List<Employee> listOfEmployees = new LinkedList<>();
        listOfEmployees.add(employee);
        listOfEmployees.add(Employee.builder().firstName("Favour").lastName("Attah").email("favour.com").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        // when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));

    }

    // positive scenario  - valid employee id
    // JUnit test for GET employee by id REST API
    @DisplayName("JUnit test for GET employee by id REST API - Positive Scenario")
    @Test
    public void givenEmployeeId_whenGetEmployeeId_thenReturnEmployeeObject() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        // when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    // negative scenario  - valid employee id
    // JUnit test for GET employee by id REST API
    @DisplayName("JUnit test for GET employee by id REST API - Negative Scenario")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeId_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // JUnit test for update employee REST API - positive scenario
    @DisplayName("JUnit test for update employee REST API - positive scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee savedEmployee = employee;
        Employee updatedEmployee = Employee.builder()
                .firstName("Favour")
                .lastName("Attah")
                .email("favour@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(put("/api/employees?id={employeeId}", employeeId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));

    }

    // JUnit test for update employee REST API - negative scenario
    @DisplayName("JUnit test for update employee REST API - negative scenario")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee updatedEmployee = Employee.builder()
                .firstName("Favour")
                .lastName("Attah")
                .email("favour@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(put("/api/employees?id={employeeId}", employeeId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // JUnit test for delete employee REST API
    @DisplayName("JUnit test for delete employee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(delete("/api/employees?id={employeeId}", employeeId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }

}
