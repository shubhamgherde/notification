package com.SecurityDemo.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.SecurityDemo.demo.model.Date;
import com.SecurityDemo.demo.model.Dept_request;
import com.SecurityDemo.demo.model.Feedback;
import com.SecurityDemo.demo.model.Notification;
import com.SecurityDemo.demo.model.Room;
import com.SecurityDemo.demo.model.RoomBookingDetails;
import com.SecurityDemo.demo.model.User;
import com.SecurityDemo.demo.model.mail_request;
import com.SecurityDemo.demo.repository.FeedbackRepository;
import com.SecurityDemo.demo.service.DeptRequestService;
import com.SecurityDemo.demo.service.EmailSenderService;
import com.SecurityDemo.demo.service.MailRequestService;
import com.SecurityDemo.demo.service.NotificationService;
import com.SecurityDemo.demo.service.RoomBookingService;
import com.SecurityDemo.demo.service.RoomService;
import com.SecurityDemo.demo.service.UserService;

@Controller
public class TLController {

	String status;

	
	@Autowired
	NotificationService notificationService;

	
	@Autowired
	UserService userService;

	
	@Autowired
	RoomService roomService;

	@Autowired
	RoomBookingService roomBookingService;

	@Autowired
	UserService userservice;

	@Autowired
	MailRequestService mailRequestService;
	
	@Autowired
	DeptRequestService deptRequestService;

	@Autowired
	private EmailSenderService emailSenderService;
	
	
	@Autowired
	FeedbackRepository feedbackRepository; 
	
	@Autowired
	RoomService roomSertvice;

	
	
	
	
	@RequestMapping(value = "/viewAllRoomtl", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewAllRoom(Model m) {

		
		List<Room> list = roomSertvice.listAll();
		m.addAttribute("list", list);
		System.out.println("in view all room");
		System.out.println(list);
		
		return "tlPages/viewAllRooms";
	}
	
	
	
	
	@RequestMapping(value = "/AllStatustl", method = RequestMethod.GET)

	public String AllStatus(Authentication authentication, Model m) {

		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.AllStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "All Request");
			return "tlPages/error";
		} else {
			m.addAttribute("list", list);
			return "tlPages/AllRequest";
		}
	}
	
	

	@RequestMapping(value = "/pendingStatusTl", method = RequestMethod.GET)

	public String pendingStatus(Authentication authentication, Model m) {

		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.pendingStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "Pending Request");
			return "tlPages/error";
		} else {
			m.addAttribute("list", list);
			return "tlPages/pendingRequestTl";
		}
	}

	@RequestMapping(value = "/tlProfile", method = RequestMethod.GET)

	public String userProfile(Authentication authentication, Model m) {

		String USER = authentication.getName();

		User userProfile = userservice.findByEmail(USER);

		m.addAttribute("userProfile", userProfile);

		return "tlPages/profile";

	}

	@RequestMapping(value = "/confirmStatusTl", method = RequestMethod.GET)

	public String confirmStatus(Authentication authentication, Model m) {

		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.confirmStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "Confirm Request");
			return "tlPages/error";
		} else {
			m.addAttribute("list", list);
			return "tlPages/confirmRequestTl";
		}
	}

	@RequestMapping(value = "/cancelStatusTl", method = RequestMethod.GET)

	public String cancelStatus(Authentication authentication, Model m) {
		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.cancelStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "Cancel Request");
			return "tlPages/error";
		} else {
			m.addAttribute("list", list);
			return "tlPages/cancelRequestTl";
		}
	}

	@RequestMapping(value = "/viewAvailableRoomForTl", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView register1() {

		ModelAndView modelAndView = new ModelAndView();
		Date date = new Date();
		modelAndView.addObject("date", date);
		modelAndView.setViewName("tlPages/viewAvailableRoomForTl"); // resources/template/register.html
		return modelAndView;
	}

	@RequestMapping(value = "/book_room_dateTl", method = RequestMethod.POST)

	public String view2(Model m, Date date) {

		String status = "CONFIRM";
		String date1 = date.getDate1();
		String date2 = date.getDate2();

		List<Room> list = roomService.availableRoomByDate(date1, date2, status);
		m.addAttribute("date1", date.getDate1());
		m.addAttribute("date2", date.getDate2());
		m.addAttribute("list", list);
		return "tlPages/viewAvailableRoomForTl";

	}

	@RequestMapping("/bookRoomFormTl/{id}/{date1}/{date2}")
	public ModelAndView bookRoomForm(@PathVariable(name = "id") int id, @PathVariable(name = "date1") String date1,
			@PathVariable(name = "date2") String date2) {

		ModelAndView mv = new ModelAndView("newBooking/bookRoomFormtl");
		Room room = roomService.get(id);
		mv.addObject("room", room);
		mv.addObject("date1", date1);
		mv.addObject("date2", date2);

		return mv;
	}

	@RequestMapping(value = "/bookRoomTl", method = RequestMethod.POST)
	public String bookRoomTl(Authentication auth,RoomBookingDetails room) {
		
		String user=auth.getName();
		int id = room.getId();
		System.out.println(id);
		String status = "PENDING";

		roomBookingService.saveBookingRoom(room);
		
		String type="Room_Booking";
		notificationService.save(user,type);
		roomService.updateStatusPending(id, status);
		return "redirect:/tlHome";
	}

	@RequestMapping(value = "/releaseRoomTl/{id}/{booking_id}")
	public String releaseRoom(@PathVariable(name = "id") int id, @PathVariable(name = "booking_id") int booking_id) {

		String status = "AVAILABLE";
		String status2 = "MEETINGOVER";

		roomBookingService.updateStatus(id, status2);

		roomService.updateStatusPending(booking_id, status);
		return "redirect:/confirmStatusTl";
	}

	@RequestMapping(value = "/Change_emailTl", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView changeemail() {
		ModelAndView modelAndView = new ModelAndView();
org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
User user = userservice.findByM(auth.getName());
String role1 = user.getRoles().toString();
		String dept = user.getDepartment();
		status="PENDING";

		List<mail_request> list=mailRequestService.AllMailReq(user.getEmail(),status);

		System.out.println();

		modelAndView.addObject("role", role1);
		modelAndView.addObject("dept", dept);
		if(list.isEmpty())
		{
			modelAndView.setViewName("tlPages/editEmailForm"); // resources/template/admin.html
			return modelAndView;
			
		}
		else
		{
			modelAndView.setViewName("tlPages/errorForMailChange"); // resources/template/admin.html
			return modelAndView;
		}
	

		
	}
	

	@RequestMapping(value = "/edit_mailTl", method = RequestMethod.POST)

	public String bookRoomForm(Authentication auth,mail_request mail) {
		System.out.println();
		mailRequestService.savemailreq(mail);

		String user=auth.getName();
		
		String type="Department_Change";
		String role="TL";
		notificationService.savetl(user,type,role);
		return "tlPages/tlHomeForAll";


	}
	
	
	
	
	@RequestMapping("/allDeptRequser")
	public String allDeptReqPm(Model m) {
		
		List<Dept_request> deptRequest = deptRequestService.userAllDeptReq();
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "All Request");
			return "tlPages/errorDeptReq";

		} else {
			m.addAttribute("message", "All Request");

			m.addAttribute("deptRequest", deptRequest);
			return "tlPages/allDeptrequesPm";

		}

	}
	
	
	
	@RequestMapping("/confirmDeptRequser")
	public String confirmDeptRequser(Model m) {
		
		status = "CONFIRM";
		List<Dept_request> deptRequest = deptRequestService.userDeptReq(status);
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "Confirm Request");
			return "tlPages/errorDeptReq";

		} else {
			m.addAttribute("message", "Confirm Request");

			m.addAttribute("deptRequest", deptRequest);
			return "tlPages/allDeptrequesPm";

		}

	}
	
	@RequestMapping("/cancelDeptRequser")
	public String cancelDeptRequser(Model m) {
		
		status = "CANCEL";
		List<Dept_request> deptRequest = deptRequestService.userDeptReq(status);
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "Cancel Request");
			return "tlPages/errorDeptReq";

		} else {
			m.addAttribute("message", "Cancel Request");

			m.addAttribute("deptRequest", deptRequest);
			return "tlPages/allDeptrequesPm";

		}

	}


	
	@RequestMapping("/pendingDeptRequser")
	public String pendingDeptReqPm(Model m) {

		status = "PENDING";
		List<Dept_request> deptRequest = deptRequestService.userDeptReq(status);
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "Pending  Request");
			return "tlPages/errorDeptReq";

		} else {
			m.addAttribute("deptRequest", deptRequest);
			return "tlPages/pendingDeptrequesTl";

		}

	}
	
	
	@RequestMapping(value = "/changeConfirmdeptuser/{id}")
	public String changeConfirmdeptuser(@PathVariable(name = "id") int id) {

		String status = "CONFIRM";
		deptRequestService.updateStatusDept(id, status);

		Dept_request dept = deptRequestService.get(id);
		String email = dept.getUser_mail();
		String newDept = dept.getNew_department();

		userService.updateDept(email, newDept);

		return "redirect:/allDeptRequser";

	}
	
	@RequestMapping("/changeCancelDeptuser/{id}")
	public String cancelDeptRequser(@PathVariable(name = "id") int id , Model m) {
		
		status = "CANCEL";
		
		 deptRequestService.updateStatusDept(id ,status);
		 return "redirect:/allDeptRequser";

		}


	
	
	
	
	
	@RequestMapping("/allMailRequser")
	public String allMailRequest(Model m) {
		
		List<mail_request> mail = mailRequestService.listAllMailuser();
		if(mail.isEmpty())
		{
			m.addAttribute("message", "All Mail Request");
			return "tlPages/errorForMailReq";
			
		}
		else
		{
			m.addAttribute("mail", mail);
			m.addAttribute("message", "All Mail Request");
			return "tlPages/confirmAndCancelMailrequest";
		}
		
	}


	
	

	@RequestMapping("/pendingMailRequser")
	public String mailrequset(Model m) {
		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userservice.findByM(auth.getName());

		status = "PENDING";
		List<mail_request> mail = mailRequestService.listpendingUser(user.getDepartment(), status);

		if (mail.isEmpty()) {
			m.addAttribute("message", "Pending Mail Request");
			return "tlPages/errorForMailReq";

		} else {
			m.addAttribute("mail", mail);
			return "tlPages/pendingMailrequesTl";
		}

	}

	@RequestMapping("/confirmMailRequser")
	public String confirmMailRequset(Model m) {
		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userservice.findByM(auth.getName());

		status = "CONFIRM";
		List<mail_request> mail = mailRequestService.listpendingUser(user.getDepartment(), status);

		if (mail.isEmpty()) {
			m.addAttribute("message", "Confirm Mail Request");
			return "tlPages/errorForMailReq";

		} else {
			m.addAttribute("mail", mail);
			m.addAttribute("message", "Confirm Mail Request");
			return "tlPages/confirmAndCancelMailrequesTl";
		}
	}

	@RequestMapping("/cancelMailRequser")
	public String cancelMailRequset(Model m) {
		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userservice.findByM(auth.getName());

		status = "CANCEL";
		List<mail_request> mail = mailRequestService.listpendingUser(user.getDepartment(), status);

		if (mail.isEmpty()) {
			m.addAttribute("message", "Cancel Mail Request");
			return "tlPages/errorForMailReq";

		} else {
			m.addAttribute("mail", mail);
			m.addAttribute("message", "Cancel Mail Request");
			return "tlPages/confirmAndCancelMailrequesTl";
		}
	}

	@RequestMapping(value = "/changeConfirmmailuser/{id}")
	public String confirmRoom(@PathVariable(name = "id") int id) {

		String status = "CONFIRM";
		mailRequestService.updateStatusmail(id, status);

		mail_request mail = mailRequestService.get(id);
		String email = mail.getUser_mail();
		String nemail = mail.getNew_user_mail();

		userservice.updateemail(email, nemail);
		roomBookingService.updateemail(email, nemail);

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(mail.getUser_mail());
		mailMessage.setSubject("Complete Password Reset!");
		mailMessage.setFrom("nairobley@gmail.com");
		mailMessage.setText("Mail has been changed successfully");

		emailSenderService.sendEmail(mailMessage);
		return "redirect:/pendingMailRequser";

	}

	@RequestMapping(value = "/changeCancelmmailuser/{id}")
	public String cancelmRoom(@PathVariable(name = "id") int id) {

		String status = "CANCEL";

		mailRequestService.updateStatusmail(id, status);

		return "redirect:/pendingMailReqTl";

	}
	
	@RequestMapping("/pendingDeptReqTl")
	public String deptrequest(Model m) {
		System.out.println("in dept req");
		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userservice.findByM(auth.getName());

		status = "PENDING";
		List<Dept_request> deptRequest = deptRequestService.listpendingUser(user.getDepartment(), status);
		
		System.out.println(deptRequest);
		m.addAttribute("deptRequest", deptRequest);
		return "tlPages/pendingDeptrequesTl";

//		if (deptRequest.isEmpty()) {
//			m.addAttribute("message", "Pending Mail Request");
//			return "tlPages/errorForMailReq";
//
//		} else {
//			m.addAttribute("deptRequest", deptRequest);
//			return "tlPages/pendingDeptrequesTl";
//		}

	}
	
	@RequestMapping(value = "/changeConfirmdeptTl/{id}")
	public String changeConfirmdeptTl(@PathVariable(name = "id") int id) {

		String status = "CONFIRM";
		deptRequestService.updateStatusDept(id, status);

		Dept_request dept = deptRequestService.get(id);
		String email = dept.getUser_mail();
		String newDept = dept.getNew_department();

		userservice.updateDept(email, newDept);

		return "redirect:/pendingDeptReqTl";

	}
	
	@RequestMapping(value = "/changeCancelDeptTl/{id}")
	public String canceDeptReq(@PathVariable(name = "id") int id) {

		String status = "CANCEL";

		deptRequestService.updateStatusDept(id, status);

		return "redirect:/pendingDeptReqTl";

	}
	
	
	
	@RequestMapping(value = "/Change_depttl", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView changeDept() {
		ModelAndView modelAndView = new ModelAndView();

		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userService.findByM(auth.getName());
		String status="PENDING";
		System.out.println(user.getEmail());
		
		List<Dept_request> list=deptRequestService.AllDeptReq(user.getEmail(),status);
		
		String role1 = user.getRoles().toString();
		String dept = user.getDepartment();
		if(list.isEmpty())
		{
			modelAndView.addObject("role", role1);
			modelAndView.addObject("dept", dept);

			modelAndView.setViewName("tlPages/edit_deptForm"); // resources/template/admin.html
			return modelAndView;
		}
		else
		{
			modelAndView.addObject("dept", dept);
			modelAndView.setViewName("tlPages/errorForDeptChange"); 
			return modelAndView;
		}
		
			}
	
	
	@RequestMapping(value = "/edit_deptsavetl", method = RequestMethod.POST)

	public String editDeptRequest(Authentication auth,Dept_request dept) {
		
		String user=auth.getName();
		
		String type="Department_Change";
		String role="TL";
		notificationService.savetl(user,type,role);
		
		deptRequestService.savemailreq(dept);
		return "tlPages/tlHomeForAll";

	}

	
	
	
	@RequestMapping(value = "/tlFeedback", method = RequestMethod.GET)

	public String userFeedback() {

		return "tlPages/Feedback";

	}
	
	@RequestMapping(value = "/saveFeedbacktl", method = RequestMethod.POST)

	public String saveFeedback(Feedback feedback) {
		
		feedbackRepository.save(feedback);
		return "tlPages/tlHomeForAll";

	}
	
	
	@RequestMapping("/viewNotificationtl")
	public ModelAndView viewNotification()
	{
		ModelAndView mv=new ModelAndView();
		
		
		 
		 List<Notification> list1=notificationService.showNotificationMailtl();
		 mv.addObject("listMail", list1);
		 
		 
		 List<Notification> list2=notificationService.showNotificationDepttl();
		 mv.addObject("listDept", list2);
		 
		 String type="Mail_Change";
		 List<Notification> list3=notificationService.showOldNotificationtl(type);
		 mv.addObject("oldMail", list3);
		 
		 String type1="Department_Change";

		 List<Notification> list4=notificationService.showOldNotificationtl(type1);
		 mv.addObject("oldDept", list4);
		 
		 
		 
		 
		notificationService.updateStatus();
		 
		 mv.setViewName("tlPages/viewNotification");
		
		return mv;
		
	}

	

}
