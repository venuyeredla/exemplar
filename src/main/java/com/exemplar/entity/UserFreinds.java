package com.exemplar.entity;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

//@Entity
//@Table(name = "ecom_user_friends")
public class UserFreinds{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "friendship	_id", nullable = false)
	private Integer addressId;
	
	@Column
	private boolean isActive;

	@ManyToMany
	Set<User> friends;
	
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
