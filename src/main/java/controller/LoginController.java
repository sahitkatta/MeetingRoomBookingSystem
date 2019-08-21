package controller;

import java.io.IOException;
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

import entity.Login;
import entity.MeetingRoom;
import entity.Resource;


public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public LoginController() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Login user = new Login();
		user.setUsername(username);
		user.setPassword(password);
		HttpSession session = request.getSession();
		
		String role=null ;

		Client client = ClientBuilder.newClient( new ClientConfig() );

		String apiURL = "http://localhost:8080/MRBS/webapi/MRBS";
		WebTarget webTarget = client.target(apiURL).path("login");
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);//
		Response clientResponse = invocationBuilder.post(Entity.entity(user, MediaType.APPLICATION_JSON));
		System.out.println("clientResponse:\n"+clientResponse);
		user = clientResponse.readEntity(Login.class);

		role = user.getRole();
		System.out.println("role: "+role+";");
		client = ClientBuilder.newClient( new ClientConfig() );

		apiURL = "http://localhost:8080/MRBS/webapi/user";
		webTarget = client.target(apiURL).path("getMeetingRoomList");
		invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);//
		clientResponse = invocationBuilder.get();
		GenericType<ArrayList<MeetingRoom>> meetingRoomListType = new GenericType<ArrayList<MeetingRoom>>(){};
		ArrayList<MeetingRoom> meetingRoomList = clientResponse.readEntity(meetingRoomListType);
		webTarget = client.target(apiURL).path("getResourceList");
		invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);//
		clientResponse = invocationBuilder.get();
		GenericType<ArrayList<Resource>> resourceListType = new GenericType<ArrayList<Resource>>(){};
		ArrayList<Resource> resourceList = clientResponse.readEntity(resourceListType);
		session.setAttribute("meetingRoomList", meetingRoomList);
		session.setAttribute("resourceList", resourceList);

		if(role.equals("user")) {
			user.setRole(role);
			request.setAttribute("user", user);
			session.setAttribute("user", user);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("User.jsp?operation=requests");
			requestDispatcher.forward(request, response);
		}else if(role.equals("FM")) {
			user.setRole(role);
			request.setAttribute("admin", user);
			session.setAttribute("admin", user);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("FacilityManager.jsp?operation=addMeetingRoom");
			requestDispatcher.forward(request, response);
		}else {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp?operation=error");
			requestDispatcher.forward(request, response);
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}