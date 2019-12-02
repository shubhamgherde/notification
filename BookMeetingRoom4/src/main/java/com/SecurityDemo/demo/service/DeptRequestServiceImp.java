package com.SecurityDemo.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecurityDemo.demo.model.Dept_request;
import com.SecurityDemo.demo.repository.DeptRequestRepository;

@Service
public class DeptRequestServiceImp implements DeptRequestService {

	@Autowired
	DeptRequestRepository deptRequestRepository;

	@Override
	public void savemailreq(Dept_request req) {

		deptRequestRepository.save(req);

	}

	@Override
	public List<Dept_request> listpendingUser(String department, String status) {
		
		 return deptRequestRepository.selectPendingDeptUser(department, status);
	}

	@Override
	public void updateStatusDept(int id, String status) {
		
		deptRequestRepository.updatestatus(id,status);
		
	}

	@Override
	public void updateStatus(int id, String status2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dept_request get(int id) {
		
		return deptRequestRepository.findById(id).get();
	}

	@Override
	public List<Dept_request> listpendingPM(String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dept_request> listpendingTL(String status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dept_request> pmAllDeptReq() {
		
		return deptRequestRepository.pmAllDeptReq();
	}

	@Override
	public List<Dept_request> pmDeptReq(String status) {
		
		return deptRequestRepository.pmDeptReq( status);
	}

	@Override
	public List<Dept_request> AllDeptReq(String email, String status) {
		
		System.out.println(email +" "+ status);
		
		return deptRequestRepository.AllDeptReq(email,status);
	}

	@Override
	public List<Dept_request> userDeptReq(String status) {
		return deptRequestRepository.userDeptReq( status);
	}

	@Override
	public List<Dept_request> userAllDeptReq() {
		return deptRequestRepository.userAllDeptReq();
	}

	@Override
	public List<Dept_request> userAllDeptReqTl() {
		return deptRequestRepository.userAllDeptReqTl();
	}

	
	@Override
	public List<Dept_request> TlDeptReq(String status) {
		return deptRequestRepository.TlDeptReq( status);
	}

}
