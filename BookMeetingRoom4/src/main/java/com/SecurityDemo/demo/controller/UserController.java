package com.SecurityDemo.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.SecurityDemo.demo.model.Date;
import com.SecurityDemo.demo.model.Dept_request;
import com.SecurityDemo.demo.model.Feedback;
import com.SecurityDemo.demo.model.Room;
import com.SecurityDemo.demo.model.RoomBookingDetails;
import com.SecurityDemo.demo.model.User;
import com.SecurityDemo.demo.model.mail_request;
import com.SecurityDemo.demo.repository.FeedbackRepository;
import com.SecurityDemo.demo.repository.RoomRepository;
import com.SecurityDemo.demo.repository.tlRepository;
import com.SecurityDemo.demo.service.DeptRequestService;
import com.SecurityDemo.demo.service.MailRequestService;
import com.SecurityDemo.demo.service.RoomBookingService;
import com.SecurityDemo.demo.service.RoomService;
import com.SecurityDemo.demo.service.UserService;

@Controller
public class UserController {

	@Autowired
	RoomService roomSertvice;

	@Autowired
	RoomBookingService roomBookingService;

	@Autowired
	UserService userService;

	Authentication authentication;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	MailRequestService mailRequestService;

	@Autowired
	tlRepository tlRepository;
	
	@Autowired
	FeedbackRepository feedbackRepository; 
	
	@Autowired
	DeptRequestService deptRequestService;
	
	
	@RequestMapping(value = "/viewAllRoom", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewAllRoom(Model m) {

		
		List<Room> list = roomSertvice.listAll();
		m.addAttribute("list", list);
		System.out.println("in view all room");
		System.out.println(list);
		
		return "newUser/viewAllRooms";
	}


	@RequestMapping(value = "/book_room_date", method = RequestMethod.POST)

	public String view2(Model m, Date date) {
		String status = "CONFIRM";
		String date1 = date.getDate1();
		String date2 = date.getDate2();

		List<Room> list = roomSertvice.availableRoomByDate(date1, date2, status);

		m.addAttribute("date1", date.getDate1());
		m.addAttribute("date2", date.getDate2());

		m.addAttribute("list", list);
		return "newUser/viewAvailableRoom";

	}

	@RequestMapping(value = "/viewAvailableRoom", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView register1() {

		ModelAndView modelAndView = new ModelAndView();
		Date date = new Date();
		modelAndView.addObject("date", date);
		modelAndView.setViewName("newUser/viewAvailableRoom"); // resources/template/register.html
		return modelAndView;
	}
	
	@RequestMapping(value = "/AllStatus", method = RequestMethod.GET)

	public String AllStatus(Authentication authentication, Model m) {

		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.AllStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "All Request");
			return "newUser/error";
		} else {
			m.addAttribute("list", list);
			return "newUser/AllRequest";
		}
	}
	
	

	@RequestMapping(value = "/pendingStatus", method = RequestMethod.GET)

	public String pendingStatus(Authentication authentication, Model m) {

		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.pendingStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "Pending Request");
			return "newUser/error";
		} else {
			m.addAttribute("list", list);
			return "newUser/pendingRequest";
		}
	}

	@RequestMapping(value = "/confirmStatus", method = RequestMethod.GET)

	public String confirmStatus(Authentication authentication, Model m) {

		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.confirmStatus(user);

		if (list.isEmpty()) {
			m.addAttribute("message", "Confirm Request");
			return "newUser/error";
		} else {
			m.addAttribute("list", list);
			return "newUser/confirmRequest";
		}

	}

	@RequestMapping(value = "/cancelStatus", method = RequestMethod.GET)

	public String cancelStatus(Authentication authentication, Model m) {
		String user = authentication.getName();

		List<RoomBookingDetails> list = roomBookingService.cancelStatus(user);
		if (list.isEmpty()) {
			m.addAttribute("message", "Cancel Request");
			return "newUser/error";
		} else {
			m.addAttribute("list", list);
			return "newUser/cancelRequest";
		}

	}

	@RequestMapping(value = "/Change_emailuser", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView changeemail() {
		ModelAndView modelAndView = new ModelAndView();

		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		User user = userService.findByM(auth.getName());

		String role1 = user.getRoles().toString();
		String dept = user.getDepartment();
		String status="PENDING";
		
		List<mail_request> list=mailRequestService.AllMailReq(user.getEmail(),status);

		modelAndView.addObject("role", role1);
		modelAndView.addObject("dept", dept);
		
		if(list.isEmpty())
		{
		modelAndView.setViewName("newUser/edit_emailForm"); // resources/template/admin.html
		return modelAndView;
		}
		else
		{
			modelAndView.setViewName("newUser/errorForMailChange"); // resources/template/admin.html
			return modelAndView;
		}
	}

	@RequestMapping(value = "/edit_mailuser", method = RequestMethod.POST)

	public String bookRoomForm(mail_request mail) {

		mailRequestService.savemailreq(mail);
		return "userHome";

	}

	@RequestMapping(value = "/userProfile", method = RequestMethod.GET)

	public String userProfile(Authentication authentication, Model m) {

		String USER = authentication.getName();

		User userProfile = userService.findByEmail(USER);
		String dept = userProfile.getDepartment();
		String tlName = tlRepository.getTlName(dept);

		m.addAttribute("tlName", tlName);
		m.addAttribute("userProfile", userProfile);

		return "newUser/profile";
	}
	
	@RequestMapping(value = "/Change_deptuser", method = { RequestMethod.GET, RequestMethod.POST })
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

			modelAndView.setViewName("newUser/edit_deptForm"); // resources/template/admin.html
			return modelAndView;
		}
		else
		{
			modelAndView.addObject("dept", dept);
			modelAndView.setViewName("newUser/errorForDeptChange"); 
			return modelAndView;
		}
		
			}
	
	@RequestMapping(value = "/userFeedback", method = RequestMethod.GET)

	public String userFeedback() {

		return "newUser/Feedback";

	}
	
	@RequestMapping(value = "/saveFeedback", method = RequestMethod.POST)

	public String saveFeedback(Feedback feedback) {
		
		feedbackRepository.save(feedback);
		return "userHome";

	}
	
	
}
