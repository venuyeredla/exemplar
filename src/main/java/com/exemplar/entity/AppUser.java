package com.exemplar.entity;

import javax.persistence.*;

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
