package com.SecurityDemo.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.SecurityDemo.demo.model.Dept_request;
import com.SecurityDemo.demo.model.Feedback;
import com.SecurityDemo.demo.model.Notification;
import com.SecurityDemo.demo.model.Room;
import com.SecurityDemo.demo.model.RoomBookingDetails;
import com.SecurityDemo.demo.model.User;
import com.SecurityDemo.demo.model.mail_request;
import com.SecurityDemo.demo.repository.RoomBookingDetailRepo;
import com.SecurityDemo.demo.repository.RoomRepository;
import com.SecurityDemo.demo.repository.UserRepository;
import com.SecurityDemo.demo.service.DeptRequestService;
import com.SecurityDemo.demo.service.EmailSenderService;
import com.SecurityDemo.demo.service.FeedbackService;
import com.SecurityDemo.demo.service.MailRequestService;
import com.SecurityDemo.demo.service.NotificationService;
import com.SecurityDemo.demo.service.RoomBookingService;
import com.SecurityDemo.demo.service.UserService;

@Controller
public class AdminController {

	String status;
	
	@Autowired
	NotificationService notificationService;

	@Autowired
	UserService userService;

	@Autowired
	RoomBookingDetailRepo roomBookingDetailRepo;

	@Autowired
	UserRepository repo;

	@Autowired
	RoomBookingService roomBookingService;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	MailRequestService mailRequestService;

	@Autowired
	private EmailSenderService emailSenderService;
	
	@Autowired
	FeedbackService feedbackService;

	@Autowired
	DeptRequestService deptRequestService;
	
	@RequestMapping(value = "/adminProfile", method = RequestMethod.GET)

	public String adminProfile(Authentication authentication, Model m) {

	String USER = authentication.getName();

	User userProfile = userService.findByEmail(USER);

	m.addAttribute("Profile", userProfile);

	return "adminPages/profile";
	}



	@RequestMapping(value = "/userManagement/{page}", method = RequestMethod.GET)
	public ModelAndView userManagement(@PathVariable(value = "page") int page,
			@RequestParam(defaultValue = "id") String sortBy) {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findByEmail(auth.getName());

		PageRequest pageable = PageRequest.of(page - 1, 5, Sort.Direction.DESC, sortBy);
		Page<User> userPage = repo.findAll(pageable);
		int totalPages = userPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			modelAndView.addObject("pageNumbers", pageNumbers);
		}
		modelAndView.addObject("activeUserList", true);
		modelAndView.addObject("list", userPage.getContent());

		modelAndView.addObject("username", "Welcome " + user.getName() + ")");
		modelAndView.setViewName("newUser/viewuser");
		return modelAndView;
	}

	@RequestMapping(value = "/roomManagement/{page}", method = RequestMethod.GET)
	public ModelAndView roomManagement(@PathVariable(value = "page") int page,
			@RequestParam(defaultValue = "id") String sortBy) {
		ModelAndView modelAndView = new ModelAndView();

		PageRequest pageable = PageRequest.of(page - 1, 5, Sort.Direction.DESC, sortBy);
		Page<Room> userPage = roomRepository.findAll(pageable);
		int totalPages = userPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			modelAndView.addObject("pageNumbers", pageNumbers);
		}
		modelAndView.addObject("activeUserList", true);
		modelAndView.addObject("list", userPage.getContent());

		modelAndView.setViewName("newRoom/viewroom");
		return modelAndView;
	}

	@RequestMapping("/allBookingReq")
	public String allRequest(Model m) {
	List<RoomBookingDetails> listRoom = roomBookingService.listAll();
	m.addAttribute("listRoom", listRoom);
	m.addAttribute("message", "All Request");
	return "newBooking/confirmRequest";
	}

	
	@RequestMapping("/confirmRequest")
	public String confirmRequest(Model m) {
		List<RoomBookingDetails> listRoom = roomBookingService.allConfirmRequest();
		m.addAttribute("listRoom", listRoom);
		m.addAttribute("message", "Confirm Request");
		return "newBooking/confirmRequest";
	}

	@RequestMapping("/pendingRequest")
	public String bookingRequest(Model m) {
		List<RoomBookingDetails> listRoom = roomBookingService.allPendingRequest();
		m.addAttribute("listRoom", listRoom);
		return "newBooking/pendingRequest";
	}

	@RequestMapping("/cancelRequest")
	public String cancelRequest(Model m) {
		List<RoomBookingDetails> listRoom = roomBookingService.allCancelRequest();
		m.addAttribute("listRoom", listRoom);
		m.addAttribute("message", "Cancel Request");
		return "newBooking/confirmRequest";
	}

	@RequestMapping("/allMailRequest")
	public String allMailRequest(Model m) {
		
		List<mail_request> mail = mailRequestService.listAllMail();
		if(mail.isEmpty())
		{
			m.addAttribute("message", "All Mail Request");
			return "adminPages/errorForMailReq";
			
		}
		else
		{
			m.addAttribute("mail", mail);
			m.addAttribute("message", "All Mail Request");
			return "adminPages/confirmAndCancelMailrequest";
		}
		
	}

	
	@RequestMapping("/pendingMailRequest")
	public String mailrequset(Model m) {
		status = "PENDING";
		List<mail_request> mail = mailRequestService.listpendingPM(status);
		if(mail.isEmpty())
		{
			m.addAttribute("message", "Pending Mail Request");
			return "adminPages/errorForMailReq";
			
		}
		else
		{
			m.addAttribute("mail", mail);
			m.addAttribute("message", "Pending Mail Request");
			return "adminPages/pendingMailrequest";
		}
		
	}

	@RequestMapping("/confirmMailRequest")
	public String confirmmailrequset(Model m) {
		status = "CONFIRM";
		List<mail_request> mail = mailRequestService.listpendingPM(status);
		if(mail.isEmpty())
		{
			m.addAttribute("message", "Confirm Mail Request");
			return "adminPages/errorForMailReq";
			
		}
		else
		{
			m.addAttribute("mail", mail);
			m.addAttribute("message", "Confirm Mail Request");
			return "adminPages/confirmAndCancelMailrequest";
		}
	}

	@RequestMapping("/cancelMailRequest")
	public String cancelmailrequset(Model m) {
		status = "CANCEL";
		List<mail_request> mail = mailRequestService.listpendingPM(status);
		if(mail.isEmpty())
		{
			m.addAttribute("message", "Cancel Mail Request");
			return "adminPages/errorForMailReq";
			
		}
		else
		{
			m.addAttribute("mail", mail);
		m.addAttribute("message", "Cancel Mail Request");
		return "adminPages/confirmAndCancelMailrequest";
		}
	}

	@RequestMapping(value = "/changeConfirmmailAdmin/{id}")
	public String confirmRoom(@PathVariable(name = "id") int id) {

		String status = "CONFIRM";
		mailRequestService.updateStatusmail(id, status);

		mail_request mail = mailRequestService.get(id);
		String email = mail.getUser_mail();
		String nemail = mail.getNew_user_mail();

		userService.updateemail(email, nemail);
		roomBookingService.updateemail(email, nemail);

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(mail.getUser_mail());
		mailMessage.setSubject("Complete Password Reset!");
		mailMessage.setFrom("nairobley@gmail.com");
		mailMessage.setText("Mail has been changed successfully");

		emailSenderService.sendEmail(mailMessage);
		return "redirect:/pendingMailRequest";

	}

	@RequestMapping(value = "/changeCancelmmailAdmin/{id}")
	public String cancelmRoom(@PathVariable(name = "id") int id) {

		String status = "CANCEL";

		mailRequestService.updateStatusmail(id, status);

		return "redirect:/allMailRequest";

	}
	

	
	@RequestMapping("/allDeptReqPm")
	public String allDeptReqPm(Model m) {
		
		List<Dept_request> deptRequest = deptRequestService.pmAllDeptReq();
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "All Request");
			return "adminPages/errorDeptReq";

		} else {
			m.addAttribute("message", "All Request");

			m.addAttribute("deptRequest", deptRequest);
			return "adminPages/allDeptrequesPm";

		}

	}
	
	
	@RequestMapping("/confirmDeptReqPm")
	public String confirmDeptReqPm(Model m) {
		
		status = "CONFIRM";
		List<Dept_request> deptRequest = deptRequestService.pmDeptReq(status);
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "Confirm Request");
			return "adminPages/errorDeptReq";

		} else {
			m.addAttribute("message", "Confirm Request");

			m.addAttribute("deptRequest", deptRequest);
			return "adminPages/allDeptrequesPm";

		}

	}
	
	@RequestMapping("/cancelDeptReqPm")
	public String cancelDeptReqPm(Model m) {
		
		status = "CANCEL";
		List<Dept_request> deptRequest = deptRequestService.pmDeptReq(status);
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "Cancel Request");
			return "adminPages/errorDeptReq";

		} else {
			m.addAttribute("message", "Cancel Request");

			m.addAttribute("deptRequest", deptRequest);
			return "adminPages/allDeptrequesPm";

		}

	}


	
	@RequestMapping("/pendingDeptReqPm")
	public String pendingDeptReqPm(Model m) {

		status = "PENDING";
		List<Dept_request> deptRequest = deptRequestService.pmDeptReq(status);
		
		
		if (deptRequest.isEmpty()) {
			m.addAttribute("message", "Pending  Request");
			return "adminPages/errorDeptReq";

		} else {
			m.addAttribute("deptRequest", deptRequest);
			return "adminPages/PendingDeptrequesPm";

		}

	}
	
	
	@RequestMapping(value = "/confirmChangeDeptReqPm/{id}")
	public String changeConfirmdeptTl(@PathVariable(name = "id") int id) {

		String status = "CONFIRM";
		deptRequestService.updateStatusDept(id, status);

		Dept_request dept = deptRequestService.get(id);
		String email = dept.getUser_mail();
		String newDept = dept.getNew_department();

		userService.updateDept(email, newDept);

		return "redirect:/allDeptReqPm";

	}
	
	@RequestMapping("/cancelChangePmDeptReq/{id}")
	public String cancelDeptReqPm(@PathVariable(name = "id") int id , Model m) {
		
		status = "CANCEL";
		
		 deptRequestService.updateStatusDept(id ,status);
		 return "redirect:/allDeptReqPm";

		}

	@RequestMapping(value = "/viewAllFeedback")
	public String viewAllFeedback(Model m) {
		List<Feedback> list = feedbackService.listAll();
		
		m.addAttribute("list",list);

		return "adminPages/viewFeedback";

	}

	@RequestMapping(value = "/viewRespondFeedback")
	public String viewRespondFeedback(Model m) {
		status="RESPONDED";
		List<Feedback> list = feedbackService.listRespondAndNotRespond(status);
		
		m.addAttribute("list",list);

		return "adminPages/viewFeedback";

	}

	@RequestMapping(value = "/viewNotRespondFeedback")
	public String viewNotRespondFeedback(Model m) {
		
		status="NOT_RESPONED";
		List<Feedback> list = feedbackService.listRespondAndNotRespond(status);
		
		m.addAttribute("list",list);

		return "adminPages/viewNotRespondFeedback";

	}

	@RequestMapping(value = "/sendReply", method = RequestMethod.POST)
	public ModelAndView feedbackReply(ModelAndView modelAndView,Feedback feedback)
	{
		System.out.println(feedback.getReply());
		System.out.println(feedback.getEmail());
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(feedback.getEmail());
			mailMessage.setSubject("Reply");
			mailMessage.setFrom("nairobley@gmail.com");

			mailMessage.setText(feedback.getReply());
			emailSenderService.sendEmail(mailMessage);

			modelAndView.setViewName("adminHome");
			return modelAndView;
	}

	@RequestMapping("/feedbackReply/{email}")
	public String feedbackReply(@PathVariable(name = "email") String email, Model m) 
	{

		m.addAttribute("email", email);
		return "adminPages/feedbackForm";
	}
	
	@RequestMapping("/viewNotification")
	public ModelAndView viewNotification()
	{
		String type;
		ModelAndView mv=new ModelAndView();
		
		List<Notification> list=notificationService.showNotification();
		 mv.addObject("listBooking", list);
		 
		 List<Notification> list1=notificationService.showNotificationMail();
		 mv.addObject("listMail", list1);
		 
		 
		 List<Notification> list2=notificationService.showNotificationDept();
		 mv.addObject("listDept", list2);
		 
		 type="Room_Booking";
		 List<Notification> list3=notificationService.showOldNotification(type);
		 mv.addObject("oldBooking", list3);
		 
		 type="Department_Change";

		 List<Notification> list4=notificationService.showOldNotification(type);
		 mv.addObject("oldDept", list4);
		 
		 type="Mail_Change";

		 List<Notification> list5=notificationService.showOldNotification(type);
		 mv.addObject("oldMail", list5);
		 
		 
		 
		 
		notificationService.updateStatus();
		 
		 mv.setViewName("adminPages/viewNotification");
		
		return mv;
		
	}

}

