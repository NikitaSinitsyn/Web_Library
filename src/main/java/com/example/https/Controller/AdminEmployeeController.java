package com.example.https.Controller;


import com.example.https.DTO.EmployeeDTO;
import com.example.https.Entity.Department;
import com.example.https.Entity.Employee;
import com.example.https.Entity.Report;
import com.example.https.Repository.DepartmentRepository;
import com.example.https.Repository.EmployeeRepository;
import com.example.https.Repository.ReportRepository;
import com.example.https.Service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/employees")
public class AdminEmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ReportRepository reportRepository;

    public AdminEmployeeController(EmployeeService employeeService,  EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, ReportRepository reportRepository) {
        this.employeeService = employeeService;

        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.reportRepository = reportRepository;
    }

    @GetMapping("/all")
    public List<EmployeeDTO> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.createEmployee(employeeDTO);
    }

    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable int id, @RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(id, employeeDTO);
    }


    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable int id) {
        employeeService.deleteEmployee(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadEmployees() {
        try {
            String filePath = "path/to/employees.json";

            FileInputStream fileInputStream = new FileInputStream(filePath);
            List<Employee> employees = readEmployeesFromJson(fileInputStream);


            employeeRepository.saveAll(employees);

            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error uploading file");
        }
    }




    private List<Employee> readEmployeesFromJson(FileInputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Employee>> typeReference = new TypeReference<List<Employee>>() {};
        List<Employee> employees = objectMapper.readValue(inputStream, typeReference);
        return employees;
    }



}