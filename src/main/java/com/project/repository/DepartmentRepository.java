package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Department;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer>{

    Optional<Department> getDepartmentByName(String name);
}
