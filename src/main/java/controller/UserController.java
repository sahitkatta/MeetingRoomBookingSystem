package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import constants.Status;
import entity.Login;
import entity.MeetingRequest;
import entity.MeetingRoom;
import entity.Resource;
import utility.UserGenerator;

public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserController() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String operation = request.getParameter("operation");
		HttpSession session = request.getSession();
		Login user = (Login) session.getAttribute("user");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:m");
		Client client = ClientBuilder.newClient(new ClientConfig());
		String apiURL = "http://localhost:8080/MRBS/webapi/user";
		WebTarget webTarget = null;
		Invocation.Builder invocationBuilder = null;//
		Response clientResponse = null;
		ArrayList<MeetingRoom> meetingRoomList = (ArrayList<MeetingRoom>) session.getAttribute("meetingRoomList");
		ArrayList<Resource> resourceList = (ArrayList<Resource>) session.getAttribute("resourceList");

		if (operation != null) {
			switch (operation) {

			case "requests":
				LocalDate startDate = LocalDate.parse(request.getParameter("startDate"), dateFormatter);
				LocalDate endDate = LocalDate.parse(request.getParameter("endDate"), dateFormatter);
				ArrayList<LocalDate> dates = UserGenerator.getDatesBetween(startDate, endDate);
				ArrayList<MeetingRequest> meetingRequests = new ArrayList<MeetingRequest>();
				for (LocalDate date : dates) {
					MeetingRequest meetingRequest = new MeetingRequest();
					meetingRequest.setDate(date);
					meetingRequest.setEndTime(LocalTime.parse(request.getParameter("endTime"), timeFormatter));
					meetingRequest.setRequestId(UserGenerator.generateID(user.getUsername()));
					meetingRequest.setResource(UserController
							.getResource(Integer.parseInt(request.getParameter("resource")), resourceList));
					meetingRequest.setStartTime(LocalTime.parse(request.getParameter("startTime"), timeFormatter));
					meetingRequest.setStatus(Status.NEW);
					meetingRequest.setUser(user);
					meetingRequest.setMeetingRoom(UserController.getMeetingRoom(
							Integer.parseInt(request.getParameter("meetingRoomNumber")), meetingRoomList));
					meetingRequest.setRequestedOn(LocalDateTime.now());
					meetingRequests.add(meetingRequest);
				}
				webTarget = client.target(apiURL).path("sendBulkRequest");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);//
				clientResponse = invocationBuilder.post(Entity.entity(meetingRequests, MediaType.APPLICATION_JSON));
				RequestDispatcher dispatcher = request.getRequestDispatcher("User?operation=view");
				dispatcher.forward(request, response);
				break;
			case "view":
				webTarget = client.target(apiURL).path("getAllRequest/" + user.getUsername());
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);//
				clientResponse = invocationBuilder.get();
				GenericType<ArrayList<MeetingRequest>> requestListType = new GenericType<ArrayList<MeetingRequest>>() {
				};
				ArrayList<MeetingRequest> requestList = clientResponse.readEntity(requestListType);
				session.setAttribute("requestList", requestList);
				dispatcher = request.getRequestDispatcher("User.jsp?operation=cancel");
				dispatcher.forward(request, response);
				break;
			case "cancel":
				String id = request.getParameter("id");
				webTarget = client.target(apiURL).path("cancelRequest");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);//
				MeetingRequest meetingRequest = new MeetingRequest();
				meetingRequest.setRequestId(id);
				clientResponse = invocationBuilder.put(Entity.entity(meetingRequest, MediaType.APPLICATION_JSON));
				dispatcher = request.getRequestDispatcher("User?operation=view");
				dispatcher.forward(request, response);
				break;
			case "history":
				webTarget = client.target(apiURL).path("getPastHistory/" + user.getUsername());
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);//
				clientResponse = invocationBuilder.get();
				GenericType<ArrayList<MeetingRequest>> historyListType = new GenericType<ArrayList<MeetingRequest>>() {
				};
				ArrayList<MeetingRequest> historyList = clientResponse.readEntity(historyListType);
				session.setAttribute("historyList", historyList);
				dispatcher = request.getRequestDispatcher("User.jsp?operation=history");
				dispatcher.forward(request, response);
				break;
			}

		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	public static Resource getResource(Integer resourceId, ArrayList<Resource> resources) {

		for (Resource resource : resources) {
			if (resource.getId() == resourceId) {
				return resource;
			}
		}
		return null;
	}

	public static MeetingRoom getMeetingRoom(Integer resourceId, ArrayList<MeetingRoom> meetingRooms) {

		for (MeetingRoom meetingRoom : meetingRooms) {
			if (meetingRoom.getId() == resourceId) {
				return meetingRoom;
			}
		}
		return null;
	}

}
