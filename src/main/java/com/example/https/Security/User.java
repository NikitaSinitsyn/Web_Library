package com.example.https.Security;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    private boolean isEnabled;
    @JoinColumn(name = "user_id")
    @OneToMany(fetch = FetchType.EAGER)

    private List<Authority> authorityList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isEnabled == user.isEnabled && Objects.equals(Id, user.Id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(authorityList, user.authorityList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, username, password, isEnabled, authorityList);
    }
}
