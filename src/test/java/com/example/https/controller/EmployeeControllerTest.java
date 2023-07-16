package com.example.https.controller;

import com.example.https.AppConfig.JacksonConfig;
import com.example.https.Controller.AdminEmployeeController;
import com.example.https.Entity.Report;
import com.example.https.Repository.EmployeeRepository;
import com.example.https.Repository.PositionRepository;
import com.example.https.Repository.ReportRepository;
import com.example.https.Service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private WebApplicationContext context;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private AdminEmployeeController adminEmployeeController;




    @AfterEach
    public void cleanData() {
        employeeRepository.deleteAll();
    }

    @Test
    @Sql(scripts = {"/test-data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/clear-data.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", password = "Cjdtncrfz159753!", roles = "USER")
    public void getAllEmployees_ReturnsEmployeeList() throws Exception {



        // Выполнение GET-запроса к эндпоинту /employee/employees
        mockMvc.perform(get("/employee/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    @Sql(scripts = {"/test-data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/clear-data.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", password = "Cjdtncrfz159753!", roles = "USER")
    public void getEmployeeById_ReturnsEmployee() throws Exception {

        // Выполнение GET-запроса к эндпоинту /employee/{id}
        mockMvc.perform(get("/employee/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.salary").value(5000.0));
    }


    @Test
    @Sql(scripts = {"/test-data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/clear-data.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", password = "Cjdtncrfz159753!", roles = "USER")
    public void getEmployeesByPosition_ReturnsEmployeeList() throws Exception {

        // Выполнение GET-запроса к эндпоинту /employee/position/{positionId}
        mockMvc.perform(get("/employee/position/{positionId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    @Sql(scripts = {"/test-data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/clear-data.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", password = "Cjdtncrfz159753!", roles = "USER")
    public void getEmployeesWithHighestSalary_ReturnsEmployeeList() throws Exception {



        // Выполнение GET-запроса к эндпоинту /employee/withHighestSalary
        mockMvc.perform(get("/employee/withHighestSalary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].salary").value(5000.0));
    }
    @Test
    @Sql(scripts = {"/test-data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/clear-data.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", password = "Cjdtncrfz159753!", roles = "USER")
    public void findEmployeesByPosition_ReturnsEmployeeList() throws Exception {


        // Выполнение GET-запроса к эндпоинту /employee/FindByPosition
        mockMvc.perform(get("/employee/FindByPosition")
                        .param("position","Software Developer" )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }
    @Test
    @Sql(scripts = {"/test-data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/clear-data.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", password = "Cjdtncrfz159753!", roles = "USER")
    public void getEmployeeFullInfoById_ReturnsEmployeeFullInfo() throws Exception {



        // Выполнение GET-запроса к эндпоинту /employee/{id}/fullInfo
        mockMvc.perform(get("/employee/{id}/fullInfo",1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.salary").value(5000.0))
                .andExpect(jsonPath("$.department").value("IT"));
    }
    @Test
    @Sql(scripts = {"/test-data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/clear-data.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", password = "Cjdtncrfz159753!", roles = "USER")
    public void getEmployeesByPage_ReturnsPagedEmployeeList() throws Exception {


        // Выполнение GET-запроса к эндпоинту /employee/page
        mockMvc.perform(get("/employee/page")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));
    }
    @Test
    @Sql(scripts = {"/test-data.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/clear-data.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", password = "Cjdtncrfz159753!", roles = "ADMIN")
    public void getReportById_ReturnsReportFile() throws Exception {


        ResponseEntity<Integer> responseEntity = adminEmployeeController.generateReport();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        int fileId = responseEntity.getBody();

        MvcResult mvcResult = mockMvc.perform(get("/employee/report/{id}", fileId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        byte[] responseBytes = mvcResult.getResponse().getContentAsByteArray();
        Report report = reportRepository.findById(fileId).orElseThrow(() -> new EntityNotFoundException("Report not found"));

        // Проверка содержимого файла
        assertArrayEquals(report.getContent(), responseBytes);
    }


}
