package net.higherAchievers.service;

import net.higherAchievers.entity.Employee;
import net.higherAchievers.exception.ResourceNotFoundException;
import net.higherAchievers.repository.EmployeeRepository;
import net.higherAchievers.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //This tells mockito that mockito annotations are been used to mock
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Moses")
                .lastName("Hunsu")
                .email("moses@gmail.com")
                .build();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);
        // when - action or the behaviour to be tested
        Employee savedEmployee = employeeService.saveEmployee(employee);

        System.out.println(savedEmployee);
        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for saveEmployee method which throws exception
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when - action or the behaviour to be tested
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,
                () -> employeeService.saveEmployee(employee));

        // then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Favour")
                .lastName("Attah")
                .email("favour@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        // when - action or the behaviour to be tested
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).size().isEqualTo(2);
    }

    // JUnit test for getAllEmployees method (negative scenario)
    @DisplayName("JUnit test for getAllEmployees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // given - precondition or setup

        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Favour")
                .lastName("Attah")
                .email("favour@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or the behaviour to be tested
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList).size().isEqualTo(0);
    }

    // JUnit test for getEmployeeById method
    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when - action or the behaviour to be tested
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for updateEmployee method
    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("hunsu@gmail.com");
        employee.setFirstName("Mos");

        // when - action or the behaviour to be tested
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("hunsu@gmail.com");
        assertThat(updatedEmployee.getFirstName().equals("Mos")).isTrue();
    }

    // JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        // given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or the behaviour to be tested
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

}
