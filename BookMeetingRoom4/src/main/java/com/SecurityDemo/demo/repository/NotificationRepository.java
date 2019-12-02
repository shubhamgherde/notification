package com.SecurityDemo.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.SecurityDemo.demo.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	@Query(value = "select * from notification where status= 'UNREAD' && type='Room_Booking'", nativeQuery = true)
	List<Notification> showAll();

	@Modifying
	@Query(value = "insert into notification(mail,type) values(:user,:type)", nativeQuery = true)
	@Transactional
	void saveNotification(@Param("user") String user, @Param("type") String type);

	@Modifying
	@Transactional
	@Query(value = "UPDATE notification c SET c.status = 'READ'", nativeQuery = true)
	void saveStatus();

	@Query(value = "select count(status) from notification where status= 'UNREAD' && (role='PM' || role='ALL')", nativeQuery = true)

	int countUnread();

	@Query(value = "select * from notification where status= 'UNREAD' && role= 'PM' && type='Department_Change'", nativeQuery = true)

	List<Notification> showMail();

	@Query(value = "select * from notification where status= 'UNREAD' && role= 'PM' && type='Mail_Change'", nativeQuery = true)

	List<Notification> showDept();

	@Query(value = "select * from notification where type= :type && status= 'READ' && (role='PM' || role='ALL')", nativeQuery = true)

	List<Notification> showOld(@Param("type") String type);

	@Query(value = "select * from notification where status= 'UNREAD' && role= 'USER' && type='Department_Change'", nativeQuery = true)

	List<Notification> showDepttl();

	@Query(value = "select * from notification where status= 'UNREAD' && role= 'USER' && type='Mail_Change'", nativeQuery = true)

	List<Notification> showMailtl();

	@Query(value = "select * from notification where status= 'READ' &&  role= 'USER' && type=:type", nativeQuery = true)

	List<Notification> showOldtl(@Param("type") String type);

	@Modifying
	@Query(value = "insert into notification(mail,type,role) values(:user,:type,:role)", nativeQuery = true)
	@Transactional
	void savetlNotification(@Param("user") String user, @Param("type") String type, @Param("role") String role);

	@Query(value = "select count(status) from notification where status= 'UNREAD' && role='USER'", nativeQuery = true)

	int countUnreadtl();

	
	
	
	@Query(value = "select * from notification where status= 'UNREAD' && role= 'TL' && type='Mail_Change'", nativeQuery = true)

	List<Notification> showMailpm();
	
	@Query(value = "select * from notification where status= 'UNREAD' && role= 'TL' && type='Department_Change'", nativeQuery = true)


	List<Notification> showDeptpm();

	@Query(value = "select * from notification where status= 'READ' &&  role= 'TL' && type=:type", nativeQuery = true)

	List<Notification> showOldpm(String type);

}
