package com.exemplar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "APP_USER")
public class AppUser {
    @Id
    @GeneratedValue
    private  int id;
    @Column(name="username")
    private String userName;
    @Column(name="password")
    private String password;
}
