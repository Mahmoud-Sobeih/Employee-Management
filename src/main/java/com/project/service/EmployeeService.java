package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.config.exception.exceptions.ResourceNotFoundException;
import com.project.model.Employee;
import com.project.repository.EmployeeRepository;
import com.project.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public Employee saveOrUpdateEmployee(Employee employee) {
		Employee savedEmployee = null;
		try {
			savedEmployee = employeeRepository.save(employee);
		} catch (Exception e) {
			log.error("Error in saved or update employee", e);
		}
		return savedEmployee;
	}

	public List<Employee> getAllEmployees() {
		List<Employee> employeesList = null;
		try {
			employeesList = employeeRepository.findAll();
		} catch (Exception e) {
			log.error("Error in get all employees", e);
		}
		return employeesList;
	}

	public Employee getEmployeeById(Long id) {
		return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee with ID: " + id + " Not Found"));
	}

	public void deleteEmployee(Long id) {
		try {
			employeeRepository.deleteById(id);
		} catch (Exception e) {
			log.error("Error in delete an employee", e);
		}
		
	}

}
