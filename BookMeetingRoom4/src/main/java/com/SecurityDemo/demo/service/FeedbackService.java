package com.SecurityDemo.demo.service;

import java.util.List;

import com.SecurityDemo.demo.model.Feedback;

public interface FeedbackService {
	
	public List<Feedback> listAll();
	
	public List<Feedback> listRespondAndNotRespond(String status);

}
