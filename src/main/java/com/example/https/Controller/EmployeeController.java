package com.example.https.Controller;


import com.example.https.CustomExceptionHandler.PositionNotFoundException;
import com.example.https.CustomExceptionHandler.ResourceNotFoundException;
import com.example.https.DTO.EmployeeDTO;
import com.example.https.DTO.EmployeeFullInfoDTO;
import com.example.https.Entity.Department;
import com.example.https.Entity.Employee;
import com.example.https.Entity.Report;
import com.example.https.Repository.DepartmentRepository;
import com.example.https.Repository.EmployeeRepository;
import com.example.https.Repository.PositionRepository;
import com.example.https.Repository.ReportRepository;
import com.example.https.Service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final PositionRepository positionRepository;
    private final ReportRepository reportRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;



    @GetMapping("/employees")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable int id) {
        return employeeService.getEmployeeById(id);
    }


    @GetMapping("/position/{positionId}")
    public List<EmployeeDTO> getEmployeesByPosition(@PathVariable int positionId) {
        positionRepository.findById(positionId)
                .orElseThrow(() -> new PositionNotFoundException("Position not found with id: " + positionId));

        return employeeService.getEmployeesByPosition(positionId);
    }
    @GetMapping("/withHighestSalary")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesWithHighestSalary() {
        List<EmployeeDTO> employees = employeeService.getEmployeesWithHighestSalary();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/FindByPosition")
    public ResponseEntity<List<EmployeeDTO>> findEmployeesByPosition(@RequestParam(value = "position", required = false) String position) {
        List<EmployeeDTO> employees = employeeService.findEmployeesByPosition((position));
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}/fullInfo")
    public ResponseEntity<EmployeeFullInfoDTO> getEmployeeFullInfo(@PathVariable int id) {
        EmployeeFullInfoDTO employeeFullInfo = employeeService.getEmployeeFullInfoById(id);
        return ResponseEntity.ok(employeeFullInfo);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<EmployeeDTO>> getEmployeesByPage(@RequestParam(value = "page", defaultValue = "0") int page) {
        Page<EmployeeDTO> employeesPage = employeeService.getAllEmployeesByPage(page);
        return ResponseEntity.ok(employeesPage);
    }



    @GetMapping("/report/{id}")
    public ResponseEntity<byte[]> getReportById(@PathVariable int id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        byte[] fileContent = report.getContent();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "report.json");

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
    @PostMapping("/report")
    public ResponseEntity<Integer> generateReport() {
        try {
            List<Report> reports = generateDepartmentStatistics();
            String jsonReport = convertToJson(reports);

            int fileId = saveReportToDatabase(jsonReport);

            return ResponseEntity.ok(fileId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<Employee> readEmployeesFromJson(FileInputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Employee>> typeReference = new TypeReference<List<Employee>>() {};
        List<Employee> employees = objectMapper.readValue(inputStream, typeReference);
        return employees;
    }

    private List<Report> generateDepartmentStatistics() {
        List<Report> reports = new ArrayList<>();

        // Получение списка всех отделов из базы данных
        List<Department> departments = departmentRepository.findAll();

        for (Department department : departments) {
            Report report = new Report();
            report.setDepartment(department);
            report.setDepartmentName(department.getName());

            // Получение списка сотрудников для текущего отдела из базы данных
            List<Employee> employees = employeeRepository.findByDepartment(department);

            report.setEmployeeCount(employees.size());

            // Расчет максимальной, минимальной и средней зарплаты
            double maxSalary = Double.MIN_VALUE;
            double minSalary = Double.MAX_VALUE;
            double totalSalary = 0.0;

            for (Employee employee : employees) {
                double salary = employee.getSalary();
                maxSalary = Math.max(maxSalary, salary);
                minSalary = Math.min(minSalary, salary);
                totalSalary += salary;
            }

            double averageSalary = totalSalary / employees.size();

            report.setMaxSalary(maxSalary);
            report.setMinSalary(minSalary);
            report.setAverageSalary(averageSalary);

            reports.add(report);
        }

        return reports;
    }
    private String convertToJson(List<Report> reports) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(reports);
    }

    private int saveReportToDatabase(String jsonReport) {
        Report report = new Report();
        report.setContent(jsonReport.getBytes());


        Report savedReport = reportRepository.save(report);
        return savedReport.getId();
    }


}