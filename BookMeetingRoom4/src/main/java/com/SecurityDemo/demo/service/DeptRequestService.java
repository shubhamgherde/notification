package com.SecurityDemo.demo.service;

import java.util.List;

import com.SecurityDemo.demo.model.Dept_request;

public interface DeptRequestService {

	public void savemailreq(Dept_request req);

	public List<Dept_request> listpendingUser(String department, String status);

	public void updateStatusDept(int id, String status);

	public void updateStatus(int id, String status2);

	public Dept_request get(int id);

	public List<Dept_request> listpendingPM(String status);

	public List<Dept_request> listpendingTL(String status);

	public List<Dept_request> pmAllDeptReq();

	public List<Dept_request> pmDeptReq(String status);
	
	public List<Dept_request> AllDeptReq(String email,String status);

	public List<Dept_request> userDeptReq(String status);

	public List<Dept_request> userAllDeptReq();
	
	public List<Dept_request> userAllDeptReqTl();
	
	public List<Dept_request> TlDeptReq(String status);



}
