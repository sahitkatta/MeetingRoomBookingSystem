package resource;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entity.Login;
import entity.MeetingRequest;
import entity.MeetingRoom;
import entity.Resource;
import service.FacilityManagerService;
@Path("FacilityManager")

public class FacilityManagerResource {
	private FacilityManagerService facilityMangerService = new FacilityManagerService();
	//GET
	/* viewUsers
	 * getAllPendingRequest
	 * getAllRequest
	 * getAllRequestForGivenDay
	 * getMRFrequency
	 * getResourceFrequency
	 * mostResourceUsed --PlainText
	 * */
	@Path("viewUsers")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Login> viewUsers(){
		return facilityMangerService.viewUsers();
	}
	@Path("getAllPendingRequest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<MeetingRequest> getAllPendingRequest(){
		return facilityMangerService.getAllPendingRequest();
	}
	@Path("getAllRequest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<MeetingRequest> getAllRequest(){
		return facilityMangerService.getAllRequest();
	}
	@Path("getAllRequestForGivenDay/{date}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<MeetingRequest> getAllRequestForGivenDay(@PathParam("date") String date){
		
		return facilityMangerService.getAllRequestForGivenDay(date);
	}
	@Path("getMRFrequency")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> getMRFrequency(){
		return facilityMangerService.getMRFrequency();
	}
	@Path("getResourceFrequency")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<String> getResourceFrequency(){
		return facilityMangerService.getResourceFrequency();
	}
	@Path("mostResourceUsed")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String mostResourceUsed(){
		return facilityMangerService.mostResourceUsed();
	}

	//POST
	/* addMeetingRoom
	 * addResource
	 * createUser
	 * requestHR
	 * */
	@Path("createUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Login createUser(Login user){
		return facilityMangerService.createUser(user.getUsername(),user.getPassword());
	}
	@Path("addMeetingRoom")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public MeetingRoom addMeetingRoom(MeetingRoom meetingRoom){
		return facilityMangerService.addMeetingRoom(meetingRoom);
	}
	@Path("addResource")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Resource addResource(Resource resource){
		return facilityMangerService.addResource(resource);
	}
	@Path("requestHR")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<MeetingRequest> requestHR(ArrayList<MeetingRequest> HRrequests){
		return facilityMangerService.requestHR(HRrequests);
	}
	
	//PUT
	/* acceptRequest
	 * acceptHRRequest
	 * rejectRequest
	 * */
	@Path("acceptRequest")
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	public String acceptRequest(MeetingRequest request){
		return facilityMangerService.acceptRequest(request.getRequestId());
	}
	@Path("acceptHRRequest")
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	public String  acceptHRRequest(MeetingRequest request){
		return facilityMangerService.acceptHRRequest(request.getRequestId());
	}
	@Path("rejectRequest")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public MeetingRequest rejectRequest(MeetingRequest request){
		return facilityMangerService.rejectRequest(request.getRequestId());
	}
	
	//DELETE
	/* deleteUser
	 * */
	@Path("deleteUser/{username}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Login deleteUser(@PathParam("username")String username){
		return facilityMangerService.deleteUser(username);
	}
	
}
