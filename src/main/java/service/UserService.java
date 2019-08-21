package service;

import java.util.ArrayList;

import dao.UserDao;
import entity.MeetingRequest;
import entity.MeetingRoom;
import entity.Resource;

public class UserService {
	private UserDao userDao = new UserDao();
	
	public ArrayList<MeetingRequest> getAllRequest(String username) {
		
		return userDao.getAllRequest(username);
	}

	
	public MeetingRequest cancelRequest(String requestID) {
		
		return userDao.cancelRequest(requestID);
	}

	
	public ArrayList<MeetingRequest> sendBulkRequests(ArrayList<MeetingRequest> requests) {
		
		return userDao.sendBulkRequests(requests);
	}

	
	public MeetingRequest sendRequest(MeetingRequest request) {
		
		return userDao.sendRequest(request);
	}

	
	public ArrayList<MeetingRequest> getPastHistory(String username) {
		return userDao.getPastHistory(username);
		
	}

	
	public ArrayList<MeetingRoom> getMeetingRoomList() {
		
		return userDao.getMeetingRoomList();
	}

	
	public ArrayList<Resource> getResourceList() {
		
		return userDao.getResourceList();
	}

	
}
