package com.example.https.controller;

import com.example.https.DTO.EmployeeDTO;
import com.example.https.DTO.EmployeeFullInfoDTO;
import com.example.https.Entity.Department;
import com.example.https.Entity.Employee;
import com.example.https.Entity.Position;
import com.example.https.Entity.Report;
import com.example.https.Repository.EmployeeRepository;
import com.example.https.Repository.PositionRepository;
import com.example.https.Repository.ReportRepository;
import com.example.https.Service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private ReportRepository reportRepository;

    @BeforeEach
    public void cleanData() {
        employeeRepository.deleteAll();
    }

    @Test
    public void getAllEmployees_ReturnsEmployeeList() throws Exception {
        // Создание объектов Department, Position и Employee
        Department department = new Department();
        department.setName("IT");

        Position position = new Position();
        position.setName("Software Developer");

        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setSalary(5000.0);
        employee.setPosition(position);
        employee.setDepartment(department);

        // Задание ожидаемого поведения сервиса
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeDTOList.add(new EmployeeDTO(employee.getId(), employee.getName(), employee.getSalary()));
        when(employeeService.getAllEmployees()).thenReturn(employeeDTOList);

        // Выполнение GET-запроса к эндпоинту /employee/employees
        mockMvc.perform(get("/employee/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    public void getEmployeeById_ReturnsEmployee() throws Exception {
        Position position = new Position();
        position.setName("Manager");
        positionRepository.save(position);
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setSalary(5000.0);
        employee.setPosition(position);

        employeeService.createEmployee(employee);

        // Задание ожидаемого поведения сервиса
        EmployeeDTO employeeDTO = new EmployeeDTO(employee.getId(), employee.getName(), employee.getSalary());
        when(employeeService.getEmployeeById(employee.getId())).thenReturn(employeeDTO);

        // Выполнение GET-запроса к эндпоинту /employee/{id}
        mockMvc.perform(get("/employee/{id}", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.salary").value(5000.0));
    }


    @Test
    @Sql(scripts = {"/test-data.sql"})
    public void getEmployeesByPosition_ReturnsEmployeeList() throws Exception {
        // Задание ожидаемого поведения сервиса
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeDTOList.add(new EmployeeDTO(1, "John Doe", 5000.0));
        when(positionRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(new Position(1, "Software Developer")));
        when(employeeService.getEmployeesByPosition(1)).thenReturn(employeeDTOList);

        // Выполнение GET-запроса к эндпоинту /employee/position/{positionId}
        mockMvc.perform(get("/employee/position/{positionId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    public void getEmployeesWithHighestSalary_ReturnsEmployeeList() throws Exception {
        // Создание объекты Employee
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setName("John Doe");
        employee1.setSalary(5000.0);

        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setName("Jane Smith");
        employee2.setSalary(6000.0);

        // Задание ожидаемого поведения сервиса
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeDTOList.add(new EmployeeDTO(employee2.getId(), employee2.getName(), employee2.getSalary()));
        when(employeeService.getEmployeesWithHighestSalary()).thenReturn(employeeDTOList);

        // Выполнение GET-запроса к эндпоинту /employee/withHighestSalary
        mockMvc.perform(get("/employee/withHighestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Jane Smith"))
                .andExpect(jsonPath("$[0].salary").value(6000.0));
    }
    @Test
    public void findEmployeesByPosition_ReturnsEmployeeList() throws Exception {
        // Создание объектов Department, Position и Employee
        Department department = new Department();
        department.setId(1);
        department.setName("IT");

        Position position = new Position();
        position.setId(1);
        position.setName("Software Developer");

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setSalary(5000.0);
        employee.setPosition(position);
        employee.setDepartment(department);

        // Задание ожидаемого поведения сервиса
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        employeeDTOList.add(new EmployeeDTO(employee.getId(), employee.getName(), employee.getSalary()));
        when(employeeService.findEmployeesByPosition(String.valueOf(position.getId()))).thenReturn(employeeDTOList);

        // Выполнение GET-запроса к эндпоинту /employee/FindByPosition
        mockMvc.perform(get("/employee/FindByPosition")
                        .param("position", String.valueOf(position.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }
    @Test
    public void getEmployeeFullInfo_ReturnsEmployeeFullInfo() throws Exception {
        // Создание объекта Employee
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setSalary(5000.0);

        // Задание ожидаемого поведения сервиса
        EmployeeFullInfoDTO employeeFullInfoDTO = new EmployeeFullInfoDTO(employee.getId(), employee.getName(), employee.getSalary());
        when(employeeService.getEmployeeFullInfoById(employee.getId())).thenReturn(employeeFullInfoDTO);

        // Выполнение GET-запроса к эндпоинту /employee/{id}/fullInfo
        mockMvc.perform(get("/employee/{id}/fullInfo", employee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.salary").value(5000.0))
                .andExpect(jsonPath("$.department").value("IT"));
    }
    @Test
    public void getEmployeesByPage_ReturnsPagedEmployeeList() throws Exception {
        // Создание объекты Employee
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setName("John Doe");
        employee1.setSalary(5000.0);

        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setName("Jane Smith");
        employee2.setSalary(6000.0);

        // Задание ожидаемого поведения сервиса
        Page<EmployeeDTO> employeeDTOPage = new PageImpl<>(Arrays.asList(
                new EmployeeDTO(employee1.getId(), employee1.getName(), employee1.getSalary()),
                new EmployeeDTO(employee2.getId(), employee2.getName(), employee2.getSalary())
        ));
        when(employeeService.getAllEmployeesByPage(0)).thenReturn(employeeDTOPage);

        // Выполнение GET-запроса к эндпоинту /employee/page
        mockMvc.perform(get("/employee/page")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.content[1].name").value("Jane Smith"));
    }
    @Test
    public void getReportById_ReturnsReportFile() throws Exception {
        // Создание объекта Report
        Report report = new Report();
        report.setId(1);
        report.setContent("Test report content".getBytes());

        // Задание ожидаемого поведения репозитория
        when(reportRepository.findById(report.getId())).thenReturn(Optional.of(report));

        // Выполнение GET-запроса к эндпоинту /employee/report/{id}
        MvcResult mvcResult = mockMvc.perform(get("/employee/report/{id}", report.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        byte[] responseBytes = mvcResult.getResponse().getContentAsByteArray();

        // Проверка содержимого файла
        assertArrayEquals(report.getContent(), responseBytes);
    }



}
