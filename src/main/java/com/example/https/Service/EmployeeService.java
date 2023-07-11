package com.example.https.Service;


import com.example.https.DTO.EmployeeDTO;
import com.example.https.DTO.EmployeeFullInfoDTO;
import com.example.https.Entity.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(int id);
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO updateEmployee(int id, EmployeeDTO employeeDTO);
    void deleteEmployee(int id);
     EmployeeDTO convertToDTO(Employee employee);

    List<EmployeeDTO> getEmployeesByPosition(int positionId);

    List<EmployeeDTO> getEmployeesWithHighestSalary();

    List<EmployeeDTO> findEmployeesByPosition(String position);
    EmployeeFullInfoDTO getEmployeeFullInfoById(int id);

    Page<EmployeeDTO> getAllEmployeesByPage(int page);

}