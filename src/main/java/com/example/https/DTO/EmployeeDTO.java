package com.example.https.DTO;


import com.example.https.Entity.Position;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EmployeeDTO {
    private String name;
    private double salary;
    private Position position;

    public EmployeeDTO() {

    }


}