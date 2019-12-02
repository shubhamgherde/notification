package com.SecurityDemo.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SecurityDemo.demo.model.Notification;
import com.SecurityDemo.demo.repository.NotificationRepository;

@Service
public class NotificationServiceImp implements NotificationService {

	@Autowired
	NotificationRepository notificationRepository;
	
	@Override
	public int countUnread() {
		return notificationRepository.countUnread();
		
	}

	@Override
	public List<Notification> showNotification() {
		
		return notificationRepository.showAll();
		
	}

	@Override
	public void save(String user, String type) {
		
		notificationRepository.saveNotification(user,type);
	}

	@Override
	public void updateStatus() {
		notificationRepository.saveStatus();

	}

	@Override
	public List<Notification> showNotificationMail() {
		return notificationRepository.showMail();
	}

	@Override
	public List<Notification> showNotificationDept() {
		return notificationRepository.showDept();
	}

	@Override
	public List<Notification> showOldNotification(String type) {
		return notificationRepository.showOld(type);
	}

	@Override
	public List<Notification> showNotificationMailtl() {
		return notificationRepository.showMailtl();
	}

	@Override
	public List<Notification> showNotificationDepttl() {
		return notificationRepository.showDepttl();
	}

	@Override
	public List<Notification> showOldNotificationtl(String type) {
		return notificationRepository.showOldtl(type);
	}

	@Override
	public void savetl(String user, String type, String role) {
		notificationRepository.savetlNotification(user,type,role);
		
	}

	@Override
	public int countUnreadtl() {
		return notificationRepository.countUnreadtl();
	}

	@Override
	public List<Notification> showNotificationMailpm() {
		return notificationRepository.showMailpm();
	}

	@Override
	public List<Notification> showNotificationDeptpm() {
		return notificationRepository.showDeptpm();
	}

	@Override
	public List<Notification> showOldNotificationpm(String type) {
		return notificationRepository.showOldpm(type);
	}

	

}
