package com.project.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
	private EmployeeRepository employeeRepository;

    public List<Department> getAllDepartments(){
        List<Department> departmentsList = null;
        try {
            departmentsList = departmentRepository.findAll();
        } catch (Exception e) {
            log.error("Error in get all departments", e);
        }
        return departmentsList;
    }

    public Department getDepartmentById(int id){
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department with ID: " + id + " Not Found"));
    }

    public List<Employee> getAllEmployeesByDepartment(int departmentId) {
        List<Employee> employeesList = null;
        try {
            employeesList = employeeRepository.getAllEmployeesByDepartmentId(departmentId);
        } catch (Exception e) {
            log.error("Error in get all employees in department with id: " + departmentId, e);
        }
		return employeesList;
	}

    public Department addOrUpdateDepartment(Department department){
        Department savedDepartment = null;
        try {
            savedDepartment = departmentRepository.save(department);
        } catch (Exception e) {
            log.error("Error in save or update a department", e);
        }
        return savedDepartment;
    }

    public void deleteDepartment(int id){
        try {
            departmentRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error in delete a department with id: " + id, e);
        }
    }

}
