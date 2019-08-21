package service;

import java.util.ArrayList;

import dao.FacilityManagerDao;
import entity.Login;
import entity.MeetingRequest;
import entity.MeetingRoom;
import entity.Resource;

public class FacilityManagerService {
	private FacilityManagerDao facilityManagerDao = new FacilityManagerDao();

	public MeetingRoom addMeetingRoom(MeetingRoom meetingRoom) {

		return facilityManagerDao.addMeetingRoom(meetingRoom);
	}

	public Resource addResource(Resource resource) {

		return facilityManagerDao.addResource(resource);
	}

	public Login createUser(String username, String password) {

		return facilityManagerDao.createUser(username, password);
	}

	public Login deleteUser(String username) {

		return facilityManagerDao.deleteUser(username);
	}

	public ArrayList<Login> viewUsers() {

		return facilityManagerDao.viewUsers();
	}

	public ArrayList<MeetingRequest> getAllPendingRequest() {

		return facilityManagerDao.getAllPendingRequest();
	}

	public ArrayList<MeetingRequest> getAllRequest() {

		return facilityManagerDao.getAllRequest();
	}

	public String acceptRequest(String requestID) {

		return facilityManagerDao.acceptRequest(requestID);
	}

	public String acceptHRRequest(String requestID) {

		return facilityManagerDao.acceptHRRequest(requestID);
	}

	public MeetingRequest rejectRequest(String requestID) {

		return facilityManagerDao.rejectRequest(requestID);

	}
	public ArrayList<MeetingRequest> getAllRequestForGivenDay(String date) {

		return facilityManagerDao.getAllRequestForGivenDay(date);
	}

	public ArrayList<String> getMRFrequency() {

		return facilityManagerDao.getMRFrequency();
	}

	public ArrayList<String> getResourceFrequency() {

		return facilityManagerDao.getResourceFrequency();
	}

	public String mostResourceUsed() {

		return facilityManagerDao.mostResourceUsed();
	}
	public ArrayList<MeetingRequest> requestHR(ArrayList<MeetingRequest> HRrequests) {

		return facilityManagerDao.requestHR(HRrequests);
	}

}
