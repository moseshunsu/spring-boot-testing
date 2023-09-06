package net.higherAchievers.service.impl;

import net.higherAchievers.entity.Employee;
import net.higherAchievers.exception.ResourceNotFoundException;
import net.higherAchievers.repository.EmployeeRepository;
import net.higherAchievers.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceNotFoundException("Employee already exists with given email: " + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return employeeRepository.findById(id);
    }

}
