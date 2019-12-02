package com.SecurityDemo.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SecurityDemo.demo.model.Dept_request;

@Repository
public interface DeptRequestRepository extends JpaRepository<Dept_request, Integer> {

	@Query(value = "select * from dept_request where status=:status && department=:department && role='[USER]'", nativeQuery = true)
	public List<Dept_request> selectPendingDeptUser(@Param("department") String department,
			@Param("status") String status);
	
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE dept_request c SET c.status =:status WHERE c.id = :id", nativeQuery = true)
	int updatestatus(@Param("id") int id, @Param("status") String status);

	@Query(value = "select * from dept_request where role='[PM]'", nativeQuery = true)
	public List<Dept_request> pmAllDeptReq();


	@Query(value = "select * from dept_request where status=:status && role='[PM]'", nativeQuery = true)
	public List<Dept_request> pmDeptReq(@Param("status") String status);

	@Query(value = "select * from dept_request where status=:status && user_mail=:email", nativeQuery = true)
	public List<Dept_request> AllDeptReq(@Param("email") String email,@Param("status") String status);

	@Query(value = "select * from dept_request where status=:status && role='[USER]'", nativeQuery = true)

	public List<Dept_request> userDeptReq(String status);

	@Query(value = "select * from dept_request where role='[USER]'", nativeQuery = true)
	public List<Dept_request> userAllDeptReq();
	
	@Query(value = "select * from dept_request where role='[TL]'", nativeQuery = true)
	public List<Dept_request> userAllDeptReqTl();

	
	@Query(value = "select * from dept_request where status=:status && role='[TL]'", nativeQuery = true)

	public List<Dept_request> TlDeptReq(String status);


}
