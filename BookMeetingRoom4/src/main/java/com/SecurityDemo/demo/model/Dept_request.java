package com.SecurityDemo.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Dept_request {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "user_mail")
	private String user_mail;

	private String role;

	private String department;
	
	@Column(name = "new_department")
	private String new_department;

	private String status = "PENDING";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_mail() {
		return user_mail;
	}

	public void setUser_mail(String user_mail) {
		this.user_mail = user_mail;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getNew_department() {
		return new_department;
	}

	public void setNew_department(String new_department) {
		this.new_department = new_department;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Dept_request [id=" + id + ", user_mail=" + user_mail + ", role=" + role + ", department=" + department
				+ ", new_department=" + new_department + ", status=" + status + "]";
	}
	
	


}