package net.higherAchievers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.higherAchievers.entity.Employee;
import net.higherAchievers.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    // JUnit test for create employee REST API
    @DisplayName("JUnit test for create employee REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Moses")
                .lastName("Hunsu")
                .email("moses@gmail.com")
                .build();

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
        listOfEmployees.add(Employee.builder().firstName("Moses").lastName("Hunsu").email("gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Favour").lastName("Attah").email("favour.com").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        // when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));

    }


}
