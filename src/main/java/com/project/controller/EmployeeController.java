package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.config.exception.ExceptionResponse;
import com.project.model.Employee;
import com.project.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Employee", description = "Employee management APIs")
@RequestMapping("/api/v1")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Operation(
		summary = "Insert Employee", 
		description = "Insert new employee."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@PostMapping("employee")
	public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
		log.info("Insert new employee: " + employee.toString());
		return new ResponseEntity<>(employeeService.saveOrUpdateEmployee(employee), HttpStatus.CREATED);
	}

	@Operation(
		summary = "Get All Employees", 
		description = "Retrieve all employees."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@GetMapping("employees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		log.info("get all employees");
		return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
	}


	@Operation(
		summary = "Get Employee", 
		description = "Retrieve an employee by Id."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@GetMapping("employee/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		log.info("get employee by id: " + id);
		return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
	}

	
	@Operation(
		summary = "Update Employee", 
		description = "Update an employee by Id."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@PutMapping("employee")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {

		log.info("Update employee by new values: " + employee.toString());
		return new ResponseEntity<>(employeeService.saveOrUpdateEmployee(employee), HttpStatus.OK);
	}

	@Operation(
		summary = "Delete Employee", 
		description = "Delete an employee by Id."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@DeleteMapping("employee/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {

		log.info("delete employee with id: " + id);
		employeeService.deleteEmployee(id);

		return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
	}
}
