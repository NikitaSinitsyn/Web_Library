package com.example.https.Controller;


import com.example.https.CustomExceptionHandler.PositionNotFoundException;
import com.example.https.CustomExceptionHandler.ResourceNotFoundException;
import com.example.https.DTO.EmployeeDTO;
import com.example.https.DTO.EmployeeFullInfoDTO;
import com.example.https.Entity.Report;
import com.example.https.Repository.PositionRepository;
import com.example.https.Repository.ReportRepository;
import com.example.https.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final PositionRepository positionRepository;
    private final ReportRepository reportRepository;


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


}