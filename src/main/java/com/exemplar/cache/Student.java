package com.exemplar.cache;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("Student")
public class Student {

	 public enum Gender { 
	        MALE, FEMALE
	    }

	    private String id;
	    private String name;
	    private Gender gender;
	    private int grade;
	    
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Gender getGender() {
			return gender;
		}
		public void setGender(Gender gender) {
			this.gender = gender;
		}
		public int getGrade() {
			return grade;
		}
		public void setGrade(int grade) {
			this.grade = grade;
		}
	    
	    
}
