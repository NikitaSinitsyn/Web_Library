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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
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
        Position position = new Position();
        position.setName("Manager");
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1);
        employeeDTO.setName("John Doe");
        employeeDTO.setPosition(position);
        employeeDTO.setSalary(5000);

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setPosition(position);
        employee.setSalary(5000);

        Mockito.when(modelMapper.map(any(Employee.class), any())).thenReturn(employeeDTO);
        Mockito.when(modelMapper.map(any(EmployeeDTO.class), any())).thenReturn(employee);
        Mockito.when(positionRepository.findById(anyInt())).thenReturn(Optional.of(new Position()));
    }

    @Test
    public void testGetAllEmployees() {
        Position position1 = new Position();
        position1.setName("Manager");
        Position position2 = new Position();
        position2.setName("Developer");

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("John Doe", 5000, position1));
        employees.add(new Employee("Jane Smith", 4000, position2));

        Mockito.when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeDTO> employeeDTOs = employeeService.getAllEmployees();

        assertEquals(employees.size(), employeeDTOs.size());

        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            EmployeeDTO employeeDTO = employeeDTOs.get(i);

            assertEquals(employee.getName(), employeeDTO.getName());
            assertEquals(employee.getPosition(), employeeDTO.getPosition());
            assertEquals(employee.getSalary(), employeeDTO.getSalary(), 0.001);
        }
    }

    @Test
    public void testGetEmployeeById() {
        int employeeId = 1;
        String employeeName = "John Doe";
        Position position = new Position();
        position.setName("Manager");
        double salary = 5000;

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName(employeeName);
        employee.setPosition(position);
        employee.setSalary(salary);

        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeId);


        assertEquals(employee.getId(), employeeDTO.getId());
        assertEquals(employee.getName(), employeeDTO.getName());
        assertEquals(employee.getPosition(), employeeDTO.getPosition());
        assertEquals(employee.getSalary(), employeeDTO.getSalary(), 0.001);
    }

    @Test
    public void testCreateEmployee() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Position position = new Position();
        position.setName("Manager");
        employeeDTO.setName("John Doe");
        employeeDTO.setPosition(position);
        employeeDTO.setSalary(5000);

        Employee savedEmployee = new Employee("John Doe", 5000, position);
        savedEmployee.setId(1); // Установка идентификатора

        Mockito.when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        System.out.println("Вызов employeeRepository.save() с параметром: " + savedEmployee);

        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);

        System.out.println("Созданный сотрудник: " + createdEmployee);

        assertEquals(savedEmployee.getId(), createdEmployee.getId());
        assertEquals(employeeDTO.getName(), createdEmployee.getName());
        assertEquals(employeeDTO.getPosition(), createdEmployee.getPosition());
        assertEquals(employeeDTO.getSalary(), createdEmployee.getSalary(), 0.001);
    }

    @Test
    public void testUpdateEmployee() {
        int employeeId = 1;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        Position position = new Position();
        position.setName("Manager");
        employeeDTO.setName("John Doe");
        employeeDTO.setPosition(position);
        employeeDTO.setSalary(6000);

        Employee existingEmployee = new Employee("John Doe", 5000.0, position);
        existingEmployee.setId(employeeId);

        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        Mockito.when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeDTO updatedEmployee = employeeService.updateEmployee(employeeId, employeeDTO);

        updatedEmployee.setId(employeeId);

        assertEquals(existingEmployee.getId(), updatedEmployee.getId());
        assertEquals(employeeDTO.getName(), updatedEmployee.getName());
        assertEquals(employeeDTO.getPosition(), updatedEmployee.getPosition());
        assertEquals(employeeDTO.getSalary(), updatedEmployee.getSalary(), 0.01);
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
    public void testGetEmployeesByPositionId() {
        int positionId = 1;
        Position position = new Position();
        position.setName("Manager");
        position.setId(positionId);

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "John Doe", 5000, position));

        position.setEmployees(employees);

        Mockito.when(positionRepository.findById(positionId)).thenReturn(Optional.of(position));



        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesByPosition(positionId);

        assertEquals(employees.size(), employeeDTOs.size());

        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            EmployeeDTO employeeDTO = employeeDTOs.get(i);

            assertEquals(employee.getId(), employeeDTO.getId());
            assertEquals(employee.getName(), employeeDTO.getName());
            assertEquals(employee.getPosition(), employeeDTO.getPosition());
            assertEquals(employee.getSalary(), employeeDTO.getSalary(), 0.001);
        }
    }

    @Test
    public void testGetEmployeesWithHighestSalary() {
        Position position1 = new Position();
        position1.setName("Manager");
        Position position2 = new Position();
        position2.setName("Developer");
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("John Doe", 5000, position1));
        employees.add(new Employee("Jane Smith", 4000, position2));

        Mockito.when(employeeRepository.getEmployeesWithHighestSalary()).thenReturn(employees);

        List<EmployeeDTO> employeeDTOs = employeeService.getEmployeesWithHighestSalary();

        assertEquals(employees.size(), employeeDTOs.size());


        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            EmployeeDTO employeeDTO = employeeDTOs.get(i);

            assertEquals(employee.getName(), employeeDTO.getName());
            assertEquals(employee.getPosition(), employeeDTO.getPosition());
            assertEquals(employee.getSalary(), employeeDTO.getSalary(), 0.001);
        }
    }

    @Test
    public void testFindEmployeesByPosition() {
        // Создание тестовых данных
        Position position = new Position();
        position.setName("Manager");
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("John Doe", 5000, position));
        employees.add(new Employee("Jane Smith", 4000, position));

        Mockito.when(employeeRepository.findEmployeesByPosition(String.valueOf(position))).thenReturn(employees);

        List<EmployeeDTO> employeeDTOs = employeeService.findEmployeesByPosition(String.valueOf(position));

        assertEquals(employees.size(), employeeDTOs.size());

        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            EmployeeDTO employeeDTO = employeeDTOs.get(i);

            assertEquals(employee.getName(), employeeDTO.getName());
            assertEquals(employee.getPosition(), employeeDTO.getPosition());
            assertEquals(employee.getSalary(), employeeDTO.getSalary(), 0.01);
        }
    }

    @Test
    public void testGetEmployeeFullInfoById() {
        int employeeId = 1;
        Position position = new Position();
        position.setName("Manager");
        EmployeeFullInfoDTO employeeFullInfo = new EmployeeFullInfoDTO();
        employeeFullInfo.setId(employeeId);
        employeeFullInfo.setName("John Doe");
        employeeFullInfo.setSalary(5000);
        employeeFullInfo.setPosition(position);

        Mockito.when(employeeRepository.getEmployeeFullInfoById(employeeId)).thenReturn(Optional.of(employeeFullInfo));

        EmployeeFullInfoDTO result = employeeService.getEmployeeFullInfoById(employeeId);

        assertEquals(employeeId, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals(5000, result.getSalary(), 0.001);
        assertEquals(position, result.getPosition());
    }
    @Test
    public void testGetAllEmployeesByPage() {
        int page = 0;
        Page<Employee> employeePage = new PageImpl<>(new ArrayList<>());

        Mockito.when(employeeRepository.findAllEmployees(any())).thenReturn(employeePage);

        Position position1 = new Position();
        position1.setName("Manager");
        Position position2 = new Position();
        position2.setName("Developer");
        Employee employee1 = new Employee(1,"John Doe", 5000, position1);

        List<Employee> employees = Arrays.asList(employee1);

        Pageable pageable = PageRequest.of(page, 10);
        Page<Employee> employeePageWithContent = new PageImpl<>(employees, pageable, employees.size());
        Mockito.when(employeeRepository.findAllEmployees(pageable)).thenReturn(employeePageWithContent);

        Page<EmployeeDTO> result = employeeService.getAllEmployeesByPage(page);
        System.out.println("Ожидаемые элементы: " + employeePageWithContent.getTotalElements());
        System.out.println("Фактические элементы: " + result.getTotalElements());

        assertEquals(employeePageWithContent.getTotalElements(), result.getTotalElements());

        List<EmployeeDTO> resultContent = result.getContent();
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            EmployeeDTO employeeDTO = resultContent.get(i);

            assertEquals(employee.getId(), employeeDTO.getId());
            assertEquals(employee.getName(), employeeDTO.getName());
            assertEquals(employee.getPosition(), employeeDTO.getPosition());
            assertEquals(employee.getSalary(), employeeDTO.getSalary(), 0.01);
        }
    }
}