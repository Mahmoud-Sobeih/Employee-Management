package com.project.config.security;

import com.project.model.Employee;
import com.project.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JWTUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public JWTUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(username + " is not exist"));

        return new JwtUserDetails(employee.getEmail(), employee.getPassword());
    }
}

