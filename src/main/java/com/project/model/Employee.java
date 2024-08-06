package com.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;



@Data
@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(name = "first_name")
	private String firstName;
	
	@NotEmpty
	@Column(name = "last_name")
	private String lastName;
	
	@NotEmpty
	@Column(name = "email")
	private String email;

	@NotEmpty
	@Column(name = "password")
	private String password;
	
	@NotEmpty
	@Column(name = "job_title")
	private String jobTitle;

	@NotEmpty
	@Column(name = "salary")
	private Double salary;

	@ManyToOne
	@JoinColumn(name = "departmentId")
	private Department department;

}
