package com.example.attendance;

import com.example.attendance.model.Employee;
import com.example.attendance.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDatabase(EmployeeRepository employeeRepository) {
        return args -> {
            if (employeeRepository.count() == 0) {
                employeeRepository.save(new Employee("E001", "Ayesha Khan", "Software Engineer"));
                employeeRepository.save(new Employee("E002", "Bilal Ahmed", "HR Executive"));
                employeeRepository.save(new Employee("E003", "Sara Malik", "Project Manager"));
            }
        };
    }
}
