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
import com.project.model.Department;
import com.project.model.Employee;
import com.project.service.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "Department", description = "Department management APIs")
@RequestMapping("api/v1")
public class DepartmentController {
    
    private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@Operation(
		summary = "Insert Department", 
		description = "Insert new department."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@PostMapping("department")
	public ResponseEntity<Department> saveDepartment(@RequestBody Department department) {
		log.info("Insert new department: {}", department.toString());
		return new ResponseEntity<>(departmentService.addOrUpdateDepartment(department), HttpStatus.CREATED);
	}

	@Operation(
		summary = "Get All Departments", 
		description = "Retrieve all departments."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@GetMapping("departments")
	public ResponseEntity<List<Department>> getAllDepartments() {
		log.info("get all departments");
		return new ResponseEntity<>(departmentService.getAllDepartments(), HttpStatus.OK);
	}


	@Operation(
		summary = "Get Department", 
		description = "Retrieve an department by Id."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@GetMapping("department/{id}")
	public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
		log.info("get department by id: {}", id);
		return new ResponseEntity<>(departmentService.getDepartmentById(id), HttpStatus.OK);
	}


    @Operation(
		summary = "Get Employees", 
		description = "Retrieve all employees in a department by department Id."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@GetMapping("department/{id}/employees")
	public ResponseEntity<List<Employee>> getAllEmployeesInDepartment(@PathVariable int id) {
		log.info("get all employees in department with id: {}", id);
		return new ResponseEntity<>(departmentService.getAllEmployeesByDepartment(id), HttpStatus.OK);
	}

	
	@Operation(
		summary = "Update Department", 
		description = "Update an department by Id."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@PutMapping("department")
	public ResponseEntity<Department> updateDepartment(@RequestBody Department department) {

		log.info("Update department by new values: {}", department.toString());
		return new ResponseEntity<>(departmentService.addOrUpdateDepartment(department), HttpStatus.OK);
	}

	@Operation(
		summary = "Delete Department", 
		description = "Delete an department by Id."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application/json")}),
		@ApiResponse(responseCode = "500", content = {@Content(schema = @Schema(implementation = ExceptionResponse.class), mediaType = "application.json")})
	})
	@DeleteMapping("department/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable int id) {

		log.info("delete employee with id: {}", id);
		departmentService.deleteDepartment(id);

		return new ResponseEntity<>("Department deleted successfully", HttpStatus.OK);
	}
}
