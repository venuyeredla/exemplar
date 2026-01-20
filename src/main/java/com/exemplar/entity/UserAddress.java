package com.exemplar.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ecom_user_address")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class UserAddress{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "address_id", nullable = false)
	private Integer addressId;

	@Column(name = "street", length = 50, nullable = true)
	private String street;
	
	@Column(name = "area", length = 50, nullable = true)
	private String area;
	
	
	@Column(name = "city", length = 50, nullable = true)
	private String city;

	
	@Column(name = "district", length = 50, nullable = true)
	private String district;
	
	
	@Column(name = "state", length = 50, nullable = true)
	private String state;
	
	@Column(name = "country", length = 50, nullable = true)
	private String country;
	
	
	@Column(name = "pincode", length = 50, nullable = true)
	private String pincode;
	
	@Column
	private boolean isActive;
	
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

	@ManyToOne
	@JoinColumn(name = "id", nullable = false)
	private User user;

}
