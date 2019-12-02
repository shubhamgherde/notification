package com.SecurityDemo.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.SecurityDemo.demo.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

	@Query(value = "select * from feedback where status=:status", nativeQuery = true)
	List<Feedback> listRespondAndNotRespond(@Param("status") String status);

}
