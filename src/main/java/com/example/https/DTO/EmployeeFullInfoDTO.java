package com.example.https.DTO;

import com.example.https.Entity.Position;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EmployeeFullInfoDTO {
    private int id;
    private String name;
    private double salary;
    private Position position;

    public EmployeeFullInfoDTO(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }
}