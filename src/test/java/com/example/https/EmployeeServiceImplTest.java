package com.example.https;
import com.example.https.CustomExceptionHandler.EmployeeNotFoundException;
import com.example.https.DTO.EmployeeDTO;
import com.example.https.DTO.EmployeeFullInfoDTO;
import com.example.https.Entity.Employee;
import com.example.https.Entity.Position;
import com.example.https.Repository.EmployeeRepository;
import com.example.https.Repository.PositionRepository;
import com.example.https.Service.EmployeeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Before
    public void setup() {

        Mockito.when(modelMapper.map(any(Employee.class), any())).thenReturn(new EmployeeDTO());
        Mockito.when(modelMapper.map(any(EmployeeDTO.class), any())).thenReturn(new Employee());
        Mockito.when(positionRepository.findById(anyInt())).thenReturn(Optional.of(new Position()));
    }

    @Test
    public void testGetAllEmployees() {

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        employees.add(new Employee());

        Mockito.when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeDTO> employeeDTOs = employeeService.getAllEmployees();

        assertEquals(employees.size(), employeeDTOs.size());
    }

    @Test
    public void testGetEmployeeById() {

        int employeeId = 1;
        Employee employee = new Employee();
        employee.setId(employeeId);

        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeId);

        assertEquals(employee.getId(), employeeDTO.getId());
    }

    @Test
    public void testCreateEmployee() {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John Doe");

        employeeService.createEmployee(employeeDTO);

        Mockito.verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee() {

        int employeeId = 1;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setName("John Doe");

        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);

        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        employeeService.updateEmployee(employeeId, employeeDTO);

        Mockito.verify(employeeRepository).save(any(Employee.class));
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void testUpdateEmployee_ThrowsException() {

        Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());

        employeeService.updateEmployee(1, new EmployeeDTO());
    }

    @Test
    public void testDeleteEmployee() {

        int employeeId = 1;
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);

        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));

        employeeService.deleteEmployee(employeeId);

        Mockito.verify(employeeRepository).delete(existingEmployee);
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void testDeleteEmployee_ThrowsException() {

        Mockito.when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());

        employeeService.deleteEmployee(1);
    }

    @Test
    public void testGetEmployeesByPosition() {

        int positionId = 1;
        Position position = Mockito.mock(Position.class);
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        employees.add(new Employee());

        Mockito.when(positionRepository.findById(positionId)).thenReturn(Optional.of(position));
        Mockito.when(position.getEmployees()).thenReturn(employees);

        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesByPosition(positionId);

        assertEquals(employees.size(), employeeDTOs.size());
    }

    @Test
    public void testGetEmployeesWithHighestSalary() {

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        employees.add(new Employee());

        Mockito.when(employeeRepository.getEmployeesWithHighestSalary()).thenReturn(employees);

        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesWithHighestSalary();

        assertEquals(employees.size(), employeeDTOs.size());
    }

    @Test
    public void testFindEmployeesByPosition() {
        // Create test data
        String position = "Manager";
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        employees.add(new Employee());

        Mockito.when(employeeRepository.findEmployeesByPosition(position)).thenReturn(employees);

        List<EmployeeDTO> employeeDTOs = employeeService.findEmployeesByPosition(position);

        assertEquals(employees.size(), employeeDTOs.size());
    }

    @Test
    public void testGetEmployeeFullInfoById() {

        int employeeId = 1;
        EmployeeFullInfoDTO employeeFullInfoDTO = new EmployeeFullInfoDTO();
        employeeFullInfoDTO.setId(employeeId);

        Mockito.when(employeeRepository.getEmployeeFullInfoById(employeeId)).thenReturn(Optional.of(employeeFullInfoDTO));

        EmployeeFullInfoDTO result = employeeService.getEmployeeFullInfoById(employeeId);

        assertEquals(employeeId, result.getId());
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void testGetEmployeeFullInfoById_ThrowsException() {

        Mockito.when(employeeRepository.getEmployeeFullInfoById(anyInt())).thenReturn(Optional.empty());

        employeeService.getEmployeeFullInfoById(1);
    }

    @Test
    public void testGetAllEmployeesByPage() {

        int page = 0;
        Page<Employee> employeePage = new PageImpl<>(new ArrayList<>());

        Mockito.when(employeeRepository.findAllEmployees(any())).thenReturn(employeePage);

        Page<EmployeeDTO> result = employeeService.getAllEmployeesByPage(page);

        assertEquals(employeePage.getTotalElements(), result.getTotalElements());
    }
}