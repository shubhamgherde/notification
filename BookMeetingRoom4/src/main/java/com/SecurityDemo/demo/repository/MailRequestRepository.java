package com.SecurityDemo.demo.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SecurityDemo.demo.model.mail_request;

@Repository
public interface MailRequestRepository extends JpaRepository<mail_request, Integer> {

	@Query(value = "select * from mail_request where status=:status && role='[PM]'", nativeQuery = true)
	public List<mail_request> selectPendingmailPM(@Param("status") String status);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE mail_request c SET c.status =:status WHERE c.mailrequest_id = :mailrequest_id", nativeQuery = true)
	int updatestatus(@Param("mailrequest_id") int mailrequest_id, @Param("status") String status);

	@Query(value = "select * from mail_request where status=:status && department=:department && role='[USER]'", nativeQuery = true)
	public List<mail_request> selectPendingmailUser(@Param("department") String department,
			@Param("status") String status);

	@Query(value = "select * from mail_request where status=:status && role='[TL]'", nativeQuery = true)

	public List<mail_request> selectPendingmailTL(@Param("status") String status);

	@Query(value = "select * from mail_request where role='[PM]'", nativeQuery = true)
	public List<mail_request> listAllMail();

	@Query(value = "select * from mail_request where user_mail=:email && status=:status", nativeQuery = true)
	public List<mail_request> AllMailReq(@Param("email")String email, @Param("status") String status);

	@Query(value = "select * from mail_request where role='[USER]'", nativeQuery = true)

	public List<mail_request> listAllMailuser();

	
	@Query(value = "select * from mail_request where role='[TL]'", nativeQuery = true)

	public List<mail_request> listAllMailTl();

}
