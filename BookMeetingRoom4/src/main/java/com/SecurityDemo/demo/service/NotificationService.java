package com.SecurityDemo.demo.service;

import java.util.List;

import com.SecurityDemo.demo.model.Notification;

public interface NotificationService {

	public int countUnread();

	public List<Notification> showNotification();

	public void save(String user, String type);

	public void updateStatus();

	public List<Notification> showNotificationMail();
	
	public List<Notification> showNotificationDept();

	public List<Notification> showOldNotification(String type);

	public List<Notification> showNotificationMailtl();

	public List<Notification> showNotificationDepttl();

	public List<Notification> showOldNotificationtl(String type);

	public void savetl(String user, String type, String role);

	public int countUnreadtl();

	public List<Notification> showNotificationMailpm();
	public List<Notification> showNotificationDeptpm();
	public List<Notification> showOldNotificationpm(String type);




}
