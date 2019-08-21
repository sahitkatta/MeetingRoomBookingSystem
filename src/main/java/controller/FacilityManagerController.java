package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

public class FacilityManagerController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FacilityManagerController() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {



		String operation= request.getParameter("operation");
		HttpSession session = request.getSession();
		ArrayList<MeetingRoom> meetingRoomList = (ArrayList<MeetingRoom>) session.getAttribute("meetingRoomList");
		ArrayList<Resource> resourceList = (ArrayList<Resource> )session.getAttribute("resourceList");


		Login user = (Login) session.getAttribute("admin");
		Client client = ClientBuilder.newClient( new ClientConfig() );
		String apiURL = "http://localhost:8080/MRBS/webapi/FacilityManager";
		WebTarget webTarget = null;
		Invocation.Builder invocationBuilder = null;//
		Response clientResponse = null;
	
		RequestDispatcher dispatcher = null;
		if(operation!=null) {
			switch(operation) {
			case "viewUsers":
				webTarget = client.target(apiURL).path("viewUsers");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.get();
				GenericType<ArrayList<Login>> usersListType = new GenericType<ArrayList<Login>>() {};
				ArrayList<Login> users = clientResponse.readEntity(usersListType);
				session.setAttribute("viewUsers", users);
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp?operation=viewUsers");
				dispatcher.forward(request, response);
				break;
			case "getAllPendingRequest":
				webTarget = client.target(apiURL).path("getAllPendingRequest");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.get();
				GenericType<ArrayList<MeetingRequest>> requestListType = new GenericType<ArrayList<MeetingRequest>>() {};
				ArrayList<MeetingRequest> requests = clientResponse.readEntity(requestListType);
				session.setAttribute("getAllPendingRequest", requests);
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp?operation=getAllPendingRequest");
				dispatcher.forward(request, response);
				break;
			case "getAllRequest":
				webTarget = client.target(apiURL).path("getAllRequest");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.get();
				requestListType = new GenericType<ArrayList<MeetingRequest>>() {};
				requests = clientResponse.readEntity(requestListType);
				session.setAttribute("getAllRequest", requests);
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp?operation=getAllRequest");
				dispatcher.forward(request, response);
				break;
			case "getAllRequestForGivenDay":
				String date = request.getParameter("date");
				System.out.println("controller date:"+ date);
				webTarget = client.target(apiURL).path("getAllRequestForGivenDay/"+date);
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.get();
				requestListType = new GenericType<ArrayList<MeetingRequest>>() {};
				requests = clientResponse.readEntity(requestListType);
				session.setAttribute("getAllRequestForGivenDay", requests);
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp?operation=allRequestForGivenDay");
				dispatcher.forward(request, response);
				break;
			case "getMRFrequency":
				webTarget = client.target(apiURL).path("getMRFrequency");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.get();
				GenericType<ArrayList<String>> frquencyListType = new GenericType<ArrayList<String>>() {};
				ArrayList<String> frequencyList = clientResponse.readEntity(frquencyListType);
				session.setAttribute("getMRFrequency", frequencyList);
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp?operation=getMRFrequency");
				dispatcher.forward(request, response);
				break;
			case "getResourceFrequency":
				webTarget = client.target(apiURL).path("getResourceFrequency");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.get();
				frquencyListType = new GenericType<ArrayList<String>>() {};
				frequencyList = clientResponse.readEntity(frquencyListType);
				session.setAttribute("getResourceFrequency", frequencyList);
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp?operation=getResourceFrequency");
				dispatcher.forward(request, response);
				break;
			case "mostResourceUsed":
				webTarget = client.target(apiURL).path("mostResourceUsed");
				invocationBuilder = webTarget.request(MediaType.TEXT_PLAIN);
				clientResponse = invocationBuilder.get();
				String mostResourceUsed = clientResponse.readEntity(String.class);
				session.setAttribute("mostResourceUsed", mostResourceUsed);
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp?operation=mostResourceUsed");
				dispatcher.forward(request, response);
				break;
			case "addMeetingRoom":
				Integer id = Integer.parseInt(request.getParameter("id"));
				String meetingRoomName = request.getParameter("meetingRoom");
				MeetingRoom newMeetingRoom = new MeetingRoom();
				newMeetingRoom.setId(id);
				newMeetingRoom.setMeetingRoomName(meetingRoomName);
				webTarget = client.target(apiURL).path("addMeetingRoom");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.post(Entity.entity(newMeetingRoom, MediaType.APPLICATION_JSON));
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp");
				dispatcher.forward(request, response);
				break;
			case "addResource":
				id = Integer.parseInt(request.getParameter("id"));
				String resourceName = request.getParameter("resource");
				Resource newResource = new Resource();
				newResource.setId(id);
				newResource.setResourceName(resourceName);
				webTarget = client.target(apiURL).path("addResource");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.post(Entity.entity(newResource, MediaType.APPLICATION_JSON));
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp");
				dispatcher.forward(request, response);
				break;
			case "createUser":
				String username =  request.getParameter("username");
				String password = request.getParameter("password");
				Login newUser = new Login();
				newUser.setUsername(username);
				newUser.setPassword(password);
				webTarget = client.target(apiURL).path("createUser");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.post(Entity.entity(newUser, MediaType.APPLICATION_JSON));
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp");
				dispatcher.forward(request, response);
				break;
			case "deleteUser":
				username =  request.getParameter("username");
				System.out.println("user:" +username);
				webTarget = client.target(apiURL).path("deleteUser/"+username);
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.delete();
				dispatcher = request.getRequestDispatcher("FacilityManager.jsp");
				dispatcher.forward(request, response);
				break;
			case "acceptRequest":
				String requestId = request.getParameter("id");
				webTarget = client.target(apiURL).path("acceptRequest");
				MeetingRequest meetingRequest = new MeetingRequest();
				meetingRequest.setRequestId(requestId);
				invocationBuilder = webTarget.request(MediaType.TEXT_PLAIN);
				clientResponse = invocationBuilder.put(Entity.entity(meetingRequest, MediaType.APPLICATION_JSON));
				String requestState = clientResponse.readEntity(String.class);
				if(requestState.equals("accepted")) {
					dispatcher = request.getRequestDispatcher("FacilityManager?operation=getAllPendingRequest");
				}else {
					dispatcher = request.getRequestDispatcher("FacilityManager?error=declined&operation=getAllPendingRequest");
				}
				dispatcher.forward(request, response);
				break;
			case "requestHR":
				Integer noOfMondays = 4;
				ArrayList<LocalDate> mondays = UserGenerator.getNextMondays(noOfMondays, LocalDate.now());
				ArrayList<MeetingRequest> HRrequests = new ArrayList<MeetingRequest>();
				for(LocalDate eachMonday: mondays) {
					MeetingRequest HRrequest = new MeetingRequest();
					HRrequest.setDate(eachMonday);
					HRrequest.setEndTime(LocalTime.of(11, 0));
					HRrequest.setMeetingRoom(meetingRoomList.get(0));
					HRrequest.setRequestId(UserGenerator.generateID("HR"));
					HRrequest.setResource(resourceList.get(0));
					HRrequest.setStartTime(LocalTime.of(10,0));
					HRrequest.setStatus(Status.NEW);
					HRrequest.setRequestedOn(LocalDateTime.now());
					Login userHR = new Login();
					userHR.setRole("HR");
					userHR.setUsername("HR");
					userHR.setPassword("HR");
					HRrequest.setUser(userHR);
					HRrequests.add(HRrequest);
				}
				webTarget = client.target(apiURL).path("requestHR");
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.post(Entity.entity(HRrequests, MediaType.APPLICATION_JSON));
				dispatcher = request.getRequestDispatcher("FacilityManager?operation=getAllPendingRequest");
				dispatcher.forward(request, response);
				break;
			case "rejectRequest":
				requestId = request.getParameter("id");
				webTarget = client.target(apiURL).path("rejectRequest");
				meetingRequest = new MeetingRequest();
				meetingRequest.setRequestId(requestId);
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				clientResponse = invocationBuilder.put(Entity.entity(meetingRequest, MediaType.APPLICATION_JSON));
				dispatcher = request.getRequestDispatcher("FacilityManager?operation=getAllPendingRequest");
				dispatcher.forward(request, response);
				break;
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
