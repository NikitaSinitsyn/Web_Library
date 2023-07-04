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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(EmployeeServiceImpl.class);


    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final ModelMapper modelMapper;



    @Override
    public List<EmployeeDTO> getAllEmployees() {
        logger.info("Invoking getAllEmployees method");
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::mapToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        logger.info("Invoking getEmployeeById method with id: " + id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return convertToDTO(employee);
    }
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        logger.info("Invoking createEmployee method with employee: " + employeeDTO);
        Employee employee = convertToEntity(employeeDTO);
        employeeRepository.save(employee);
        return convertToDTO(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(int id, EmployeeDTO employeeDTO) {
        logger.info("Invoking updateEmployee method with id: " + id + ", employee: " + employeeDTO);
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "Employee not found with id: " + id;
                    logger.error(errorMessage);
                    return new EmployeeNotFoundException(errorMessage);
                });

        Employee updatedEmployee = convertToEntity(employeeDTO);
        updatedEmployee.setId(existingEmployee.getId());

        employeeRepository.save(updatedEmployee);
        return employeeDTO;
    }

    @Override
    public void deleteEmployee(int id) {
        logger.info("Invoking deleteEmployee method with id: " + id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "Employee not found with id: " + id;
                    logger.error(errorMessage);
                    return new EmployeeNotFoundException(errorMessage);
                });

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
        logger.info("Invoking getEmployeesByPosition method with positionId: " + positionId);
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> {
                    String errorMessage = "Position not found with id: " + positionId;
                    logger.error(errorMessage);
                    return new PositionNotFoundException(errorMessage);
                });

        List<Employee> employees = position.getEmployees();
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<EmployeeDTO> getEmployeesWithHighestSalary() {
        logger.info("Invoking getEmployeesWithHighestSalary method");
        List<Employee> employees = employeeRepository.getEmployeesWithHighestSalary();
        return mapToEmployeeDTOs(employees);
    }

    @Override
    public List<EmployeeDTO> findEmployeesByPosition(String position) {
        logger.info("Invoking findEmployeesByPosition method with position: " + position);
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
        logger.info("Invoking getEmployeeFullInfoById method with id: " + id);
        Optional<EmployeeFullInfoDTO> employeeFullInfo = employeeRepository.getEmployeeFullInfoById(id);
        return employeeFullInfo.orElseThrow(() -> {
            String errorMessage = "Employee not found with id: " + id;
            logger.error(errorMessage);
            return new EmployeeNotFoundException(errorMessage);
        });
    }

    @Override
    public Page<EmployeeDTO> getAllEmployeesByPage(int page) {
        logger.info("Invoking getAllEmployeesByPage method with page: " + page);
        PageRequest pageable =  PageRequest.of(page, 10);
        Page<Employee> employeePage = employeeRepository.findAllEmployees((org.springframework.data.domain.Pageable) pageable);
        return employeePage.map(this::convertToDTO);
    }

    private EmployeeDTO mapToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
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