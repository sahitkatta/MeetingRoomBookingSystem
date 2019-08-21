package resource;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entity.MeetingRequest;
import entity.MeetingRoom;
import entity.Resource;
import service.UserService;;
@Path("user")
public class UserResource {
	UserService userService = new UserService();
	
	//GET
	
	@Path("getAllRequest/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<MeetingRequest> getAllRequest(@PathParam("username") String username){
		return userService.getAllRequest(username);
	}
	@Path("getPastHistory/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<MeetingRequest> getPastHistory(@PathParam("username") String username){
		return userService.getPastHistory(username);
	}
	
	@Path("getMeetingRoomList")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<MeetingRoom> getMeetingRoomList(){
		return userService.getMeetingRoomList();
	}
	
	@Path("getResourceList")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Resource> getResourceList(){
		return userService.getResourceList();
	}
	
	//POST
	
	@Path("sendRequest")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MeetingRequest sendRequest(MeetingRequest request) {
		userService.sendRequest(request);
		return request;
	}
	@Path("sendBulkRequest")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<MeetingRequest> sendBulkRequest(ArrayList<MeetingRequest> requests) {
		userService.sendBulkRequests(requests);
		return requests;
	}
	//PUT
	
	@Path("cancelRequest")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public MeetingRequest cancelRequest(MeetingRequest request) {
		
		return userService.cancelRequest(request.getRequestId());
	}

	
}
