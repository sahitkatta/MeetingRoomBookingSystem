package dao;

import java.util.ArrayList;

import entity.Login;
import entity.MeetingRequest;
import entity.MeetingRoom;
import entity.Resource;

public interface IFacilityManagerDao {
	public MeetingRoom addMeetingRoom(MeetingRoom meetingRoom);
	public Resource addResource(Resource resource);
	public Login createUser(String username,String password);
	public Login deleteUser(String username);
	public ArrayList<Login> viewUsers();
	public ArrayList<MeetingRequest> getAllPendingRequest();
	public ArrayList<MeetingRequest> getAllRequest();//use getStream() instead of list
	public String acceptRequest(String requestID);
	public String acceptHRRequest(String requestID);
	public MeetingRequest rejectRequest(String requestID);
	public Boolean isMeetinRoomAvailable(MeetingRequest pendingRequest);
	public ArrayList<MeetingRequest> getAllRequestForGivenDay(String date);
	public ArrayList<String> getMRFrequency();
	public ArrayList<String> getResourceFrequency();
	public String mostResourceUsed();
	public ArrayList<MeetingRequest> requestHR(ArrayList<MeetingRequest> HRrequests);
}
