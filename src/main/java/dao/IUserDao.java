package dao;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.MeetingRequest;
import entity.MeetingRoom;
import entity.Resource;

public interface IUserDao {
	public ArrayList<MeetingRequest> getAllRequest(String username);//view requested slot
	public MeetingRequest cancelRequest(String requestID);//view requested slot
	public void cancelBulkRequests(MeetingRequest request,LocalDate startDate,LocalDate endDate);
	public ArrayList<MeetingRequest> sendBulkRequests(ArrayList<MeetingRequest> requests);//request bulk slot
	public MeetingRequest sendRequest(MeetingRequest request);//request a slot
	public ArrayList<MeetingRequest> getPastHistory(String username);//requesthistory
	public ArrayList<MeetingRoom> getMeetingRoomList();
	public ArrayList<Resource> getResourceList();
}
