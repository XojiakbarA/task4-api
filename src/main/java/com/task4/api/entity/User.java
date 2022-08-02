package com.task4.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @NotEmpty
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @NotEmpty
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_non_locked", nullable = false)
    private boolean isNonLocked = true;

    @Column(name = "logged_in_at")
    private Date loggedInAt;

    @CreatedDate
    @Column(name = "created_at")
    private Date createAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

}
