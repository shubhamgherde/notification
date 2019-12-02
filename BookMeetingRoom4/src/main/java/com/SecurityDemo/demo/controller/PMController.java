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
import com.SecurityDemo.demo.repository.MailRequestRepository;
import com.SecurityDemo.demo.service.DeptRequestService;
import com.SecurityDemo.demo.service.EmailSenderService;
import com.SecurityDemo.demo.service.MailRequestService;
import com.SecurityDemo.demo.service.NotificationService;
import com.SecurityDemo.demo.service.RoomBookingService;
import com.SecurityDemo.demo.service.RoomService;
import com.SecurityDemo.demo.service.UserService;

@Controller
public class PMController {

	String status;

	@Autowired
	NotificationService notificationService;

	@Autowired
	MailRequestService mailRequestService;

	@Autowired
	UserService userservice;

	@Autowired
	MailRequestRepository repo;

	@Autowired
	RoomService roomService;

	@Autowired
	RoomBookingService roomBookingService;

	@Autowired
	private EmailSenderService emailSenderService;

	@Autowired
	RoomService roomSertvice;

	@Autowired
	FeedbackRepository feedbackRepository;

	@Autowired
	DeptRequestService deptRequestService;

	@RequestMapping(value = "/pendingStatusPm", method = RequestMethod.GET)

	public String pendingStatus(Authentication authentication, Model m) {

		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.pendingStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "Pending Request");
			return "pmPages/error";
		} else {
			m.addAttribute("list", list);
			return "pmPages/pendingRequestPm";
		}

	}

	@RequestMapping(value = "/pmProfile", method = RequestMethod.GET)

	public String userProfile(Authentication authentication, Model m) {

		String USER = authentication.getName();

		User userProfile = userservice.findByEmail(USER);

		m.addAttribute("userProfile", userProfile);

		return "pmPages/profile";

	}

	@RequestMapping(value = "/confirmStatusPm", method = RequestMethod.GET)

	public String confirmStatus(Authentication authentication, Model m) {

		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.confirmStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "Confirm Request");
			return "pmPages/error";
		} else {
			m.addAttribute("list", list);
			return "pmPages/confirmRequestPm";
		}
	}

	@RequestMapping(value = "/cancelStatusPm", method = RequestMethod.GET)

	public String cancelStatus(Authentication authentication, Model m) {
		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.cancelStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "Cancel Request");
			return "pmPages/error";
		} else {
			m.addAttribute("list", list);
			return "pmPages/cancelRequestPm";
		}
	}

	@RequestMapping(value = "/viewAvailableRoomForPm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView register1() {
		ModelAndView modelAndView = new ModelAndView();
		Date date = new Date();
		modelAndView.addObject("date", date);
		modelAndView.setViewName("pmPages/viewAvailableRoomForPm"); // resources/template/register.html
		return modelAndView;
	}

	@RequestMapping(value = "/book_room_datePm", method = RequestMethod.POST)

	public String view2(Model m, Date date) {

		String status = "CONFIRM";
		String date1 = date.getDate1();
		String date2 = date.getDate2();

		List<Room> list = roomService.availableRoomByDate(date1, date2, status);
		m.addAttribute("date1", date.getDate1());
		m.addAttribute("date2", date.getDate2());
		m.addAttribute("list", list);

		return "pmPages/viewAvailableRoomForPm";

	}

	@RequestMapping("/bookRoomFormPm/{id}/{date1}/{date2}")
	public ModelAndView bookRoomForm(@PathVariable(name = "id") int id, @PathVariable(name = "date1") String date1,
			@PathVariable(name = "date2") String date2) {

		ModelAndView mv = new ModelAndView("newBooking/bookRoomFormPm");
		Room room = roomService.get(id);
		mv.addObject("room", room);
		mv.addObject("date1", date1);
		mv.addObject("date2", date2);

		return mv;
	}

	@RequestMapping(value = "/bookRoomPm", method = RequestMethod.POST)
	public String bookRoomTl(Authentication auth, RoomBookingDetails room) {

		String user = auth.getName();

		int id = room.getId();

		String status = "PENDING";

		roomBookingService.saveBookingRoom(room);
		String type = "Room_Booking";
		notificationService.save(user, type);
		roomService.updateStatusPending(id, status);
		return "redirect:/pmHome";
	}

	@RequestMapping(value = "/Change_emailPm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView changeemail() {
		ModelAndView modelAndView = new ModelAndView();

		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userservice.findByM(auth.getName());

		String role1 = user.getRoles().toString();
		String dept = user.getDepartment();

		status = "PENDING";
		List<mail_request> list = mailRequestService.AllMailReq(user.getEmail(), status);

		modelAndView.addObject("role", role1);
		modelAndView.addObject("dept", dept);

		if (list.isEmpty()) {
			modelAndView.setViewName("pmPages/editEmailForm"); // resources/template/admin.html
			return modelAndView;

		} else {
			modelAndView.setViewName("pmPages/errorForMailChange"); // resources/template/admin.html
			return modelAndView;
		}

	}

	@RequestMapping(value = "/edit_mailPm", method = RequestMethod.POST)

	public String bookRoomForm(mail_request mail) {
		mailRequestService.savemailreq(mail);

		return "redirect:/pmHome";

	}

	@RequestMapping("/allMailReqPm")
	public String allmailrequset1(Model m) {

		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userservice.findByM(auth.getName());

		List<mail_request> mail = mailRequestService.listAllmailReqTl();

		if (mail.isEmpty()) {
			m.addAttribute("message", "All Mail Request");
			return "pmPages/errorForMailReq";

		} else {
			m.addAttribute("mail", mail);
			m.addAttribute("message", "All Mail Request");
			return "pmPages/confirmAndCancelMailrequesPm";
		}
	}

	@RequestMapping("/pendingMailReqPm")
	public String pendingmailrequset1(Model m) {

		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userservice.findByM(auth.getName());

		status = "PENDING";
		List<mail_request> mail = mailRequestService.listpendingTL(status);

		if (mail.isEmpty()) {
			m.addAttribute("message", "Pending Mail Request");
			return "pmPages/errorForMailReq";

		} else {
			m.addAttribute("mail", mail);
			return "pmPages/pendingMailrequesPm";
		}
	}

	@RequestMapping("/confirmMailReqPm")
	public String confirmmailrequset1(Model m) {

		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userservice.findByM(auth.getName());

		status = "CONFIRM";
		List<mail_request> mail = mailRequestService.listpendingTL(status);

		if (mail.isEmpty()) {
			m.addAttribute("message", "Confirm Mail Request");
			return "pmPages/errorForMailReq";

		} else {
			m.addAttribute("mail", mail);
			m.addAttribute("message", "Confirm Mail Request");
			return "pmPages/confirmAndCancelMailrequesPm";
		}
	}

	@RequestMapping("/cancelMailReqPm")
	public String cancelmailrequset1(Model m) {

		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userservice.findByM(auth.getName());

		status = "CANCEL";
		List<mail_request> mail = mailRequestService.listpendingTL(status);
		if (mail.isEmpty()) {
			m.addAttribute("message", "Cancel Mail Request");
			return "pmPages/errorForMailReq";

		} else {
			m.addAttribute("mail", mail);
			m.addAttribute("message", "Cancel Mail Request");

			return "pmPages/confirmAndCancelMailrequesPm";
		}
	}

	@RequestMapping(value = "/changeConfirmmailPm/{id}")
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
		return "redirect:/pendingMailReqPm";

	}

	@RequestMapping(value = "/changeCancelmmailPm/{id}")
	public String cancelmRoom(@PathVariable(name = "id") int id) {

		String status = "CANCEL";

		mailRequestService.updateStatusmail(id, status);

		return "redirect:/pendingMailReqPm";

	}

	@RequestMapping(value = "/viewAllRoomPm", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewAllRoom(Model m) {

		List<Room> list = roomSertvice.listAll();
		m.addAttribute("list", list);
		System.out.println("in view all room");
		System.out.println(list);

		return "pmPages/viewAllRooms";
	}

	@RequestMapping(value = "/AllStatusPm", method = RequestMethod.GET)

	public String AllStatus(Authentication authentication, Model m) {

		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.AllStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "All Request");
			return "pmPages/error";
		} else {
			m.addAttribute("list", list);
			return "pmPages/AllRequest";
		}
	}

	@RequestMapping(value = "/edit_deptsavePm", method = RequestMethod.POST)

	public String editDeptRequest(Dept_request dept) {

		deptRequestService.savemailreq(dept);
		return "redirect:/pmHome";

	}

	@RequestMapping(value = "/Change_deptPm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView changeDeptPm() {
		ModelAndView modelAndView = new ModelAndView();

		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userservice.findByM(auth.getName());
		String status = "PENDING";
		System.out.println(user.getEmail());

		List<Dept_request> list = deptRequestService.AllDeptReq(user.getEmail(), status);

		String role1 = user.getRoles().toString();
		String dept = user.getDepartment();
		if (list.isEmpty()) {
			modelAndView.addObject("role", role1);
			modelAndView.addObject("dept", dept);

			modelAndView.setViewName("pmPages/edit_deptForm"); // resources/template/admin.html
			return modelAndView;
		} else {
			modelAndView.addObject("dept", dept);
			modelAndView.setViewName("pmPages/errorForDeptChange");
			return modelAndView;
		}

	}

	@RequestMapping(value = "/PmFeedback", method = RequestMethod.GET)

	public String pmFeedback() {

		return "pmPages/Feedback";

	}

	@RequestMapping(value = "/saveFeedbackPm", method = RequestMethod.POST)

	public String saveFeedbackPM(Feedback feedback) {

		feedbackRepository.save(feedback);
		return "pmHome";

	}

	@RequestMapping(value = "/PmProfile", method = RequestMethod.GET)

	public String PmProfile(Authentication authentication, Model m) {

		String USER = authentication.getName();

		User userProfile = userservice.findByEmail(USER);

		m.addAttribute("userProfile", userProfile);

		return "pmPages/profile";

	}

	@RequestMapping("/viewNotificationpm")
	public ModelAndView viewNotification() {
		ModelAndView mv = new ModelAndView();

		List<Notification> list1 = notificationService.showNotificationMailpm();
		mv.addObject("listMail", list1);

		List<Notification> list2 = notificationService.showNotificationDeptpm();
		mv.addObject("listDept", list2);

		String type = "Mail_Change";
		List<Notification> list3 = notificationService.showOldNotificationpm(type);
		mv.addObject("oldMail", list3);

		String type1 = "Department_Change";

		List<Notification> list4 = notificationService.showOldNotificationpm(type1);
		mv.addObject("oldDept", list4);

		notificationService.updateStatus();

		mv.setViewName("tlPages/viewNotification");

		return mv;

	}
	
	@RequestMapping("/allDeptReqFromTl")
	public String allDeptReqPm1(Model m) {
		
		List<Dept_request> deptRequest = deptRequestService.userAllDeptReqTl();
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "All Request");
			return "pmPages/errorDeptReq";

		} else {
			m.addAttribute("message", "All Request");

			m.addAttribute("deptRequest", deptRequest);
			return "pmPages/allDeptrequesPm";

		}

	}
	
	
	
	@RequestMapping("/confirmDeptReqFromTl")
	public String confirmDeptReqPm(Model m) {
		
		status = "CONFIRM";
		List<Dept_request> deptRequest = deptRequestService.TlDeptReq(status);
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "Confirm Request");
			return "pmPages/errorDeptReq";

		} else {
			m.addAttribute("message", "Confirm Request");

			m.addAttribute("deptRequest", deptRequest);
			return "pmPages/allDeptrequesPm";

		}

	}
	
	@RequestMapping("/cancelDeptReqFromTl")
	public String cancelDeptReqPm(Model m) {
		
		status = "CANCEL";
		List<Dept_request> deptRequest = deptRequestService.TlDeptReq(status);
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "Cancel Request");
			return "pmPages/errorDeptReq";

		} else {
			m.addAttribute("message", "Cancel Request");

			m.addAttribute("deptRequest", deptRequest);
			return "pmPages/allDeptrequesPm";

		}

	}


	
	@RequestMapping("/pendingDeptReqFromTl")
	public String pendingDeptReqPm(Model m) {

		status = "PENDING";
		List<Dept_request> deptRequest = deptRequestService.TlDeptReq(status);
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "Pending  Request");
			return "pmPages/errorDeptReq";

		} else {
			m.addAttribute("deptRequest", deptRequest);
			return "pmPages/pendingDeptrequesPm";

		}

	}
	
	
	@RequestMapping(value = "/changeConfirmdeptOfTl/{id}")
	public String changeConfirmdeptuser(@PathVariable(name = "id") int id) {

		String status = "CONFIRM";
		deptRequestService.updateStatusDept(id, status);

		Dept_request dept = deptRequestService.get(id);
		String email = dept.getUser_mail();
		String newDept = dept.getNew_department();

		userservice.updateDept(email, newDept);

		return "redirect:/allDeptReqFromTl";

	}
	
	@RequestMapping("/changeCancelDeptOfTl/{id}")
	public String cancelDeptRequser(@PathVariable(name = "id") int id , Model m) {
		
		status = "CANCEL";
		
		 deptRequestService.updateStatusDept(id ,status);
		 return "redirect:/allDeptReqFromTl";

		}

	@RequestMapping("/viewNotificationPm")
	public ModelAndView viewNotificationPm()
	{
		ModelAndView mv=new ModelAndView();
		
		
		 
		 List<Notification> list1=notificationService.showNotificationMailpm();
		 mv.addObject("listMail", list1);
		 
		 
		 List<Notification> list2=notificationService.showNotificationDeptpm();
		 mv.addObject("listDept", list2);
		 
		 String type="Mail_Change";
		 List<Notification> list3=notificationService.showOldNotificationpm(type);
		 mv.addObject("oldMail", list3);
		 
		 String type1="Department_Change";

		 List<Notification> list4=notificationService.showOldNotificationpm(type1);
		 mv.addObject("oldDept", list4);
		 
		 
		 
		 
		notificationService.updateStatus();
		 
		 mv.setViewName("pmPages/viewNotification");
		
		return mv;
		
	}



}