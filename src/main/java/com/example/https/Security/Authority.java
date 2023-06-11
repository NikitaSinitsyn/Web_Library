package com.example.https.Security;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column(nullable = false, unique = true)
    private String role;

}