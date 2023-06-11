package com.example.https.Service;

import com.example.https.CustomExceptionHandler.EmployeeNotFoundException;
import com.example.https.CustomExceptionHandler.PositionNotFoundException;
import com.example.https.DTO.EmployeeDTO;
import com.example.https.DTO.EmployeeFullInfoDTO;
import com.example.https.Entity.Employee;
import com.example.https.Entity.Position;
import com.example.https.Repository.EmployeeRepository;
import com.example.https.Repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return convertToDTO(employee);
    }

    @Override
    public void createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        employeeRepository.save(employee);
    }

    @Override
    public void updateEmployee(int id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        Employee updatedEmployee = convertToEntity(employeeDTO);
        updatedEmployee.setId(existingEmployee.getId());

        employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(int id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(employee);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        return modelMapper.map(employeeDTO, Employee.class);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByPosition(int positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new PositionNotFoundException("Position not found with id: " + positionId));

        List<Employee> employees = position.getEmployees();
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<EmployeeDTO> getEmployeesWithHighestSalary() {
        List<Employee> employees = employeeRepository.getEmployeesWithHighestSalary();
        return mapToEmployeeDTOs(employees);
    }

    @Override
    public List<EmployeeDTO> findEmployeesByPosition(String position) {
        List<Employee> employees = employeeRepository.findEmployeesByPosition(position);
        return mapToEmployeeDTOs(employees);
    }

    private List<EmployeeDTO> mapToEmployeeDTOs(List<Employee> employees) {
        return employees.stream()
                .map(this::mapToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeFullInfoDTO getEmployeeFullInfoById(int id) {
        Optional<EmployeeFullInfoDTO> employeeFullInfo = employeeRepository.getEmployeeFullInfoById(id);
        return employeeFullInfo.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Override
    public Page<EmployeeDTO> getAllEmployeesByPage(int page) {
        Pageable pageable = (Pageable) PageRequest.of(page, 10);
        Page<Employee> employeePage = employeeRepository.findAllEmployees((org.springframework.data.domain.Pageable) pageable);
        return employeePage.map(this::convertToDTO);
    }
    private EmployeeDTO mapToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName(employee.getName());
        employeeDTO.setSalary(employee.getSalary());
        employeeDTO.setPosition(employee.getPosition());
        return employeeDTO;
    }


    // Helper method to convert Employee entities to DTOs
    private List<EmployeeDTO> convertToDTOList(List<Employee> employees) {
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    // Helper method to convert Employee entity to FullInfoDTO
    private EmployeeDTO convertToFullInfoDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }
}