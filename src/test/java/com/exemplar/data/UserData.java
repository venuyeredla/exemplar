package com.exemplar.data;

import java.util.List;

import com.exemplar.entity.User;
import com.exemplar.entity.UserAddress;

public class UserData {

	public static User getUser() {
			
		UserAddress userAddress = UserAddress.builder()
		.street("Venkateshwara")
		.area("Injapur")
		.city("Hyderabad")
		.state("Telangana")
		.country("Inida")
		.pincode("12334312").build();
		
		User user=User.builder()
					.firstName("ratan")
					.lastName("kumar")
					.email("ratan@ecom.com")
					.password("recom#24")
					.isActive(true)
					.userAddresses(List.of(userAddress)).build();
		return user;
	}
}