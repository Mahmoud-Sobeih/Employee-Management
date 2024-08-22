package com.project.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.project.config.exception.exceptions.ResourceNotFoundException;
import com.project.model.Employee;
import com.project.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@CachePut(value = "fetchEmployee", key = "#employee.id")
	public Employee saveOrUpdateEmployee(Employee employee) {
		Employee savedEmployee = null;
		try {
			savedEmployee = employeeRepository.save(employee);
		} catch (Exception e) {
			log.error("Error in saved or update employee", e);
		}
		return savedEmployee;
	}

	@Cacheable(value = "fetchAllEmployees")
	public List<Employee> getAllEmployees() {
		List<Employee> employeesList = null;
		try {
			employeesList = employeeRepository.findAll();
		} catch (Exception e) {
			log.error("Error in get all employees", e);
		}
		return employeesList;
	}

	@Cacheable(value = "fetchEmployee", key = "#id")
	public Employee getEmployeeById(Long id) {
		return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee with ID: " + id + " Not Found"));
	}

	@Caching(evict = {
			@CacheEvict("fetchAllEmployees"),
			@CacheEvict(value = "EmployeesByDepartment", allEntries = true),
			@CacheEvict(value="fetchEmployee", key="#id") })
	public String deleteEmployee(Long id) {
		try {
			employeeRepository.deleteById(id);
			return "Employee with ID: " + id + " Deleted";
		} catch (Exception e) {
			log.error("Error in delete an employee", e);
		}
		return "Employee with ID: " + id + " Not Found";
	}

}
