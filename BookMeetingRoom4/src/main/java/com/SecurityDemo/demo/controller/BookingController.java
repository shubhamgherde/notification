package com.SecurityDemo.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.SecurityDemo.demo.model.Room;
import com.SecurityDemo.demo.model.RoomBookingDetails;
import com.SecurityDemo.demo.service.NotificationService;
import com.SecurityDemo.demo.service.RoomBookingService;
import com.SecurityDemo.demo.service.RoomService;

@Controller
public class BookingController {

	@Autowired
	RoomBookingService roomBookingService;

	@Autowired
	RoomService roomService;
	
	@Autowired
	NotificationService notificationService;

	@RequestMapping("/bookRoomForm/{id}/{date1}/{date2}")
	public ModelAndView bookRoomForm(@PathVariable(name = "id") int id, @PathVariable(name = "date1") String date1,
			@PathVariable(name = "date2") String date2) {

		ModelAndView mv = new ModelAndView("newBooking/book_roomForm");
		Room room = roomService.get(id);
		mv.addObject("room", room);
		mv.addObject("date1", date1);
		mv.addObject("date2", date2);

		return mv;
	}

	@RequestMapping(value = "/bookRoom", method = RequestMethod.POST)
	public String bookRoom(Authentication auth, RoomBookingDetails room) {

		String user = auth.getName();
		int id = room.getId();
		String status = "PENDING";
		
		roomBookingService.saveBookingRoom(room);
		String type="Room_Booking";
		notificationService.save(user,type);
		roomService.updateStatusPending(id, status);
		return "redirect:/userHome";
	}

	
	@RequestMapping("/confirmRequest/{booking_id}/{id}")
	public String confirmRequest(@PathVariable(name = "booking_id") int booking_id,
			@PathVariable(name = "id") int room_id) {

		String status = "CONFIRM";

		roomBookingService.updateStatus(booking_id, status);
		roomService.updateStatusPending(room_id, status);
		return "redirect:/pendingRequest";
	}

	@RequestMapping("/cancelRequest/{booking_id}/{id}")
	public String cancelRequest(@PathVariable(name = "booking_id") int booking_id,
			@PathVariable(name = "id") int room_id) {

		String status = "CANCEL";
		String roomStatus = "AVAILABLE";

		roomBookingService.updateStatus(booking_id, status);
		roomService.updateStatusPending(room_id, roomStatus);
		return "redirect:/pendingRequest";
	}

//	@RequestMapping(value = "/releaseRoom/{id}/{booking_id}")
//	public String releaseRoom(@PathVariable(name = "id") int id, @PathVariable(name = "booking_id") int booking_id) {
//
//		String status = "AVAILABLE";
//		String status2 = "MEETINGOVER";
//
//		roomBookingService.updateStatus(id, status2);
//
//		roomService.updateStatusPending(booking_id, status);
//		return "redirect:/confirmStatus";
//	}

}
