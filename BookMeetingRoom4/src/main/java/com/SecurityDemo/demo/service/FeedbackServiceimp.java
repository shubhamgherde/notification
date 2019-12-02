package com.SecurityDemo.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecurityDemo.demo.model.Feedback;
import com.SecurityDemo.demo.repository.FeedbackRepository;

@Service
public class FeedbackServiceimp implements FeedbackService {

	@Autowired
	FeedbackRepository feedbackRepository; 
	
	
	@Override
	public List<Feedback> listAll() {
		
		return feedbackRepository.findAll();
	}


	@Override
	public List<Feedback> listRespondAndNotRespond(String status) {
		
		return feedbackRepository.listRespondAndNotRespond(status);
	}

}
