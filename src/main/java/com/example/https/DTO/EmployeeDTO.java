package com.example.https.DTO;


import com.example.https.Entity.Position;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EmployeeDTO {
    private int id;
    private String name;
    private double salary;
    private Position position;
    private int positionId;

    public EmployeeDTO() {

    }

    public EmployeeDTO(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDTO that = (EmployeeDTO) o;
        return id == that.id && Double.compare(that.salary, salary) == 0 && positionId == that.positionId && Objects.equals(name, that.name) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, position, positionId);
    }

}