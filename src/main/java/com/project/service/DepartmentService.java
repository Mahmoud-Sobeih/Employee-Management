package com.project.service;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.project.repository.DepartmentRepository;
import com.project.repository.EmployeeRepository;

import lombok.extern.slf4j.Slf4j;

import com.project.config.exception.exceptions.ResourceNotFoundException;
import com.project.model.Department;
import com.project.model.Employee;


@Service
@Slf4j
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Cacheable(value = "fetchAllDepartments")
    public List<Department> getAllDepartments(){
        List<Department> departmentsList = null;
        try {
            departmentsList = departmentRepository.findAll();
        } catch (Exception e) {
            log.error("Error in get all departments", e);
        }
        return departmentsList;
    }

    @Cacheable(value = "fetchDepartment", key = "#id")
    public Department getDepartmentById(int id){
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department with ID: " + id + " Not Found"));
    }

    @Cacheable(value = "EmployeesByDepartment", key = "#departmentId")
    public List<Employee> getAllEmployeesByDepartment(int departmentId) {
        List<Employee> employeesList = null;
        try {
            employeesList = employeeRepository.getAllEmployeesByDepartmentId(departmentId);
        } catch (Exception e) {
            log.error("Error in get all employees in department with id: {}", departmentId, e);
        }
		return employeesList;
	}

    @CachePut(value = "fetchDepartment", key = "#department.id")
    public Department addOrUpdateDepartment(Department department){
        Department savedDepartment = null;
        try {
            savedDepartment = departmentRepository.save(department);
        } catch (Exception e) {
            log.error("Error in save or update a department", e);
        }
        return savedDepartment;
    }

    @Caching(evict = {
            @CacheEvict("fetchAllDepartments"),
            @CacheEvict(value = "EmployeesByDepartment", key = "#id"),
            @CacheEvict(value="fetchDepartment", key="#id") })
    public void deleteDepartment(int id){
        try {
            departmentRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error in delete a department with id: {}", id, e);
        }
    }

}
