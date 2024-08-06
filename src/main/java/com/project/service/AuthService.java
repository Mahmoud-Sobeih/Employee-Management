package com.project.service;

import com.project.DTO.LoginRequest;
import com.project.DTO.LoginResponse;
import com.project.DTO.SignupRequest;
import com.project.config.exception.exceptions.AuthenticationException;
import com.project.config.exception.exceptions.InternalException;
import com.project.config.security.JWTTokenUtil;
import com.project.model.Department;
import com.project.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    private final JWTTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JWTTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, EmployeeService employeeService, DepartmentService departmentService, PasswordEncoder passwordEncoder) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        LoginResponse response;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
            response = jwtTokenUtil.generateToken(request);

        } catch (DisabledException ex) {
            log.error("User is disabled", ex);
            throw new AuthenticationException("Disabled User!!");
        } catch (BadCredentialsException ex) {
            log.error("Bad credentials", ex);
            throw new AuthenticationException("Invalid userName or password!!");
        } catch (Exception ex) {
            log.error("Authentication Error", ex);
            throw new AuthenticationException("Something went wrong in Authentication!!");
        }

        return response;
    }

    public String signup(SignupRequest request) {
        try {
            Department department = null;
            if(request != null){
                if (request.getDepartment() != null && !request.getDepartment().isEmpty()) {
                    department = departmentService.getDepartmentByName(request.getDepartment());
                }

                Employee employee = new Employee();
                employee.setFirstName(request.getFirstName());
                employee.setLastName(request.getLastName());
                employee.setEmail(request.getEmail());
                employee.setPassword(passwordEncoder.encode(request.getPassword()));
                employee.setJobTitle(request.getJobTitle());
                employee.setSalary(request.getSalary());
                employee.setDepartment(department);

                employeeService.saveOrUpdateEmployee(employee);
                return "Success";
            }else {
                log.info("Request is null!");
                throw new BadCredentialsException("Bad request (Request must be not null)");
            }

        }catch (Exception ex){
            log.error("Signup Error", ex);
            throw new InternalException("Signup Error");
        }
    }
}
