package dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import constants.Status;
import entity.Login;
import entity.MeetingRequest;
import entity.MeetingRoom;
import entity.Resource;

public class FacilityManagerDao implements IFacilityManagerDao{

	private EntityManager entityManager = JPAConnection.sharedConnection.getEntityManager();
	private String SQLgetAllPendingRequest= "SELECT request FROM entity.MeetingRequest request where ((request.status='NEW')) order by request.requestedOn";
	private String SQLgetAllRequest="SELECT request FROM entity.MeetingRequest request";
	private String SQLgetAllRequestForGivenDay = "SELECT request FROM entity.MeetingRequest request where request.date= :date";
	private String SQLisMeetingRoomAvailableForStartTime = "SELECT request from entity.MeetingRequest request where "
			+ "((request.requestId != :requestId) and ((request.meetingRoom.meetingRoomName= :meetingRoomNumber)or(request.resource.resourceName = :resource)) and (request.date= :date) and (request.status='"+Status.ACCEPT+"') and ( :startTime between (request.startTime) and (request.endTime)))";
	private String SQLisMeetingRoomAvailableForEndTime = "SELECT request from entity.MeetingRequest request where "
			+ "((request.requestId != :requestId) and ((request.meetingRoom.meetingRoomName= :meetingRoomNumber)or(request.resource.resourceName = :resource)) and (request.date= :date) and (request.status='"+Status.ACCEPT+"') and ( :endTime between (request.startTime) and (request.endTime)))";
	private String SQLviewUsers = "SELECT login FROM entity.Login login order by login.username";

	@Override
	public MeetingRoom addMeetingRoom(MeetingRoom meetingRoom) {

		entityManager.getTransaction().begin();
		entityManager.persist(meetingRoom);
		entityManager.getTransaction().commit();

		return meetingRoom;
	}

	@Override
	public Resource addResource(Resource resource) {

		entityManager.getTransaction().begin();
		entityManager.persist(resource);
		entityManager.getTransaction().commit();

		return resource;
	}

	@Override
	public Login createUser(String username, String password) {
		Login newUser = new Login();
		newUser.setRole("user");
		newUser.setUsername(username);
		newUser.setPassword(password);
		entityManager.getTransaction().begin();
		entityManager.persist(newUser);
		entityManager.getTransaction().commit();
		return newUser;
	}

	@Override
	public Login deleteUser(String username) {
		Login user = entityManager.find(Login.class, username);

		entityManager.getTransaction().begin();
		entityManager.remove(user);
		entityManager.getTransaction().commit();

		return user;
	}

	@Override
	public ArrayList<MeetingRequest> getAllPendingRequest() {

		return new ArrayList<MeetingRequest>(entityManager.createQuery(SQLgetAllPendingRequest).getResultList());
	}

	@Override
	public ArrayList<MeetingRequest> getAllRequest() {

		return new ArrayList<MeetingRequest>(entityManager.createQuery(SQLgetAllRequest).getResultList());
	}

	@Override
	public String acceptRequest(String requestID) {
		MeetingRequest request = entityManager.find(MeetingRequest.class, requestID);
		if(isMeetinRoomAvailable(request)) {
			entityManager.getTransaction().begin();
			request.setStatus(Status.ACCEPT);
			entityManager.persist(request);
			entityManager.getTransaction().commit();
			return "accepted";
		}
		return "declined";
	}

	@Override
	public String acceptHRRequest(String requestID) {
		MeetingRequest HRrequest = entityManager.find(MeetingRequest.class, requestID);
		if(isMeetinRoomAvailable(HRrequest)) {
			entityManager.getTransaction().begin();
			HRrequest.setStatus(Status.ACCEPT);
			entityManager.persist(HRrequest);
			entityManager.getTransaction().commit();
			return "accepted";
		}
		return "declined";

	}

	@Override
	public Boolean isMeetinRoomAvailable(MeetingRequest pendingRequest) {
		Boolean checkStartTime=true,checkEndTime=true;

		if(entityManager.createQuery(SQLisMeetingRoomAvailableForStartTime).setParameter("requestId", pendingRequest.getRequestId())
				.setParameter("meetingRoomNumber", pendingRequest.getMeetingRoom().getMeetingRoomName())
				.setParameter("resource", pendingRequest.getResource().getResourceName()).setParameter("date", pendingRequest.getDate())
				.setParameter("startTime", pendingRequest.getStartTime()).getResultList().size()>0) {
			checkStartTime = false;
		}
		if(entityManager.createQuery(SQLisMeetingRoomAvailableForEndTime).setParameter("requestId", pendingRequest.getRequestId())
				.setParameter("meetingRoomNumber", pendingRequest.getMeetingRoom().getMeetingRoomName())
				.setParameter("resource", pendingRequest.getResource().getResourceName()).setParameter("date", pendingRequest.getDate())
				.setParameter("endTime", pendingRequest.getEndTime()).getResultList().size()>0) {
			checkEndTime = false;
		}
		return checkStartTime && checkEndTime;
	}

	@Override
	public ArrayList<MeetingRequest> getAllRequestForGivenDay(String date) {
		System.out.println("date is:" + date);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		return new ArrayList<MeetingRequest>(entityManager.createQuery(SQLgetAllRequestForGivenDay).setParameter("date", LocalDate.parse(date, dateFormatter)).getResultList());
	}

	@Override
	public ArrayList<String> getMRFrequency() {
		List<Number> count =((entityManager.createQuery("SELECT COUNT(r.meetingRoom.id) FROM entity.MeetingRequest r  where r.status='ACCEPT' GROUP BY r.meetingRoom.id ").getResultList()));
		List<String> mr =((entityManager.createQuery("SELECT r.meetingRoom.meetingRoomName FROM entity.MeetingRequest r  where r.status='ACCEPT' GROUP BY r.meetingRoom.id").getResultList()));
		ArrayList<String> frequency =  new ArrayList<String>();
		for(Integer i =0;i<count.size();i++) {
			frequency.add(mr.get(i)+","+count.get(i));
		}
		return frequency;
	}

	@Override
	public ArrayList<String> getResourceFrequency() {
		List<Number> count =((entityManager.createQuery("SELECT COUNT(r.resource.id) FROM entity.MeetingRequest r  where r.status='ACCEPT' GROUP BY r.resource.id").getResultList()));
		List<String> resource =((entityManager.createQuery("SELECT r.resource.resourceName FROM entity.MeetingRequest r  where r.status='ACCEPT' GROUP BY r.resource.id ").getResultList()));
		ArrayList<String> frequency =  new ArrayList<String>();
		for(Integer i =0;i<count.size();i++) {
			frequency.add(resource.get(i)+","+count.get(i));
		}
		return frequency;
	}

	@Override
	public MeetingRequest rejectRequest(String requestID) {
		entityManager.getTransaction().begin();
		MeetingRequest request = entityManager.find(MeetingRequest.class, requestID);
		request.setStatus(Status.REJECT);
		entityManager.persist(request);
		entityManager.getTransaction().commit();	
		return request;
	}

	@Override
	public String mostResourceUsed() {
		String mostResourceUsed= "";

		List<Number> count =((entityManager.createQuery("SELECT COUNT(r.resource.id) FROM entity.MeetingRequest r  where r.status='ACCEPT' GROUP BY r.resource.id").getResultList()));
		List<String> resource =((entityManager.createQuery("SELECT r.resource.resourceName FROM entity.MeetingRequest r  where r.status='ACCEPT' GROUP BY r.resource.id  ").getResultList()));
		if(count.size()!=0) {
			Number max = count.get(0);
			mostResourceUsed = resource.get(0);
			for(Integer i=0;i<count.size();i++) {
				if(max.intValue()<count.get(i).intValue()) {
					max = count.get(i);
					mostResourceUsed = resource.get(i);
				}
			}
		}
		return mostResourceUsed;
	}

	@Override
	public ArrayList<Login> viewUsers() {

		return new ArrayList<Login>(entityManager.createQuery(SQLviewUsers).getResultList());
	}

	@Override
	public ArrayList<MeetingRequest> requestHR(ArrayList<MeetingRequest> HRrequests) {
		entityManager.getTransaction().begin();
		for(MeetingRequest HRrequest:HRrequests) {
			entityManager.persist(HRrequest);
		}
		entityManager.getTransaction().commit();

		return HRrequests;
	}

}
