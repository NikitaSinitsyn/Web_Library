package com.example.https.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Department name must not be blank")
    private String departmentName;

    private int employeeCount;

    private double maxSalary;

    private double minSalary;

    private double averageSalary;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] content;

    @Override
    public String toString() {
        return "Report{" +
                "departmentName='" + departmentName + '\'' +
                ", employeeCount=" + employeeCount +
                ", maxSalary=" + maxSalary +
                ", minSalary=" + minSalary +
                ", averageSalary=" + averageSalary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id == report.id && employeeCount == report.employeeCount && Double.compare(report.maxSalary, maxSalary) == 0 && Double.compare(report.minSalary, minSalary) == 0 && Double.compare(report.averageSalary, averageSalary) == 0 && Objects.equals(departmentName, report.departmentName) && Objects.equals(department, report.department) && Arrays.equals(content, report.content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, departmentName, employeeCount, maxSalary, minSalary, averageSalary, department);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}