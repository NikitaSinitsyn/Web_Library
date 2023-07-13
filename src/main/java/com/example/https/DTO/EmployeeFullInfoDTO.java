package com.example.https.DTO;

import com.example.https.Entity.Position;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeFullInfoDTO that = (EmployeeFullInfoDTO) o;
        return id == that.id && Double.compare(that.salary, salary) == 0 && Objects.equals(name, that.name) && Objects.equals(position, that.position);
    }

    @Override
    public String toString() {
        return "EmployeeFullInfoDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", position=" + position +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salary, position);
    }
}