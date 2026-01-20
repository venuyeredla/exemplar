package com.exemplar.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "ecom_user")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(name = "first_name", length = 25, nullable = false)
    private String firstName;
    
    @Column(name = "last_name", length = 25, nullable = false)
    private String lastName;


    @Column(unique = true, length = 50, nullable = false)
    private String email;

    @Column(name="pwd",nullable = false)
    private String password;

	@Column(nullable = false)
	private boolean isActive;
    
    @OneToMany(mappedBy="user")
    private List<UserAddress> userAddresses;
    
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;
    
    @ManyToMany
    @JoinTable(name = "user_friends", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "friend_id"))
    
    private  List<User> friends = null;
    
    @ManyToMany(mappedBy = "friends")
    protected List<User> befriended = null;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}