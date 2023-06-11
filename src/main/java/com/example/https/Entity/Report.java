package com.example.https.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

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
    private Department department;

    @Lob
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
}