package dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;

import constants.Status;
import entity.MeetingRequest;
import entity.MeetingRoom;
import entity.Resource;

public class UserDao implements IUserDao {

	private EntityManager entityManager = JPAConnection.sharedConnection.getEntityManager();
	private String SQLgetAllRequest = "SELECT request FROM entity.MeetingRequest request where request.user.username = :username and ((request.status='"+Status.NEW+"') or (request.status='"+Status.ACCEPT+"')) order by request.date";
	private String SQLgetPastHistory = "SELECT request FROM entity.MeetingRequest request where request.user.username= :username  order by request.date";
	private String SQLgetMeetingRoomList = "SELECT room FROM entity.MeetingRoom room";
	private String SQLgetResourceList ="SELECT resource FROM entity.Resource resource";
	@Override
	public ArrayList<MeetingRequest> getAllRequest(String username) {
		
		return new ArrayList<MeetingRequest>(entityManager.createQuery(SQLgetAllRequest).setParameter("username", username).getResultList());
	}

	@Override
	public MeetingRequest cancelRequest(String requestID) {
		entityManager.getTransaction().begin();
		MeetingRequest request = entityManager.find(MeetingRequest.class, requestID);
		request.setStatus(Status.CANCEL);
		entityManager.persist(request);
		entityManager.getTransaction().commit();
		return request;
	}

	@Override
	public ArrayList<MeetingRequest> sendBulkRequests(ArrayList<MeetingRequest> requests) {
		entityManager.getTransaction().begin();
		for(MeetingRequest request:requests) {
			entityManager.persist(request);
		}
		entityManager.getTransaction().commit();
		return requests;
	}

	@Override
	public MeetingRequest sendRequest(MeetingRequest request) {
		entityManager.getTransaction().begin();
		entityManager.persist(request);
		entityManager.getTransaction().commit();
		return request;
	}

	@Override
	public ArrayList<MeetingRequest> getPastHistory(String username) {
		
		return new ArrayList<MeetingRequest>(entityManager.createQuery(SQLgetPastHistory).setParameter("username", username).getResultList());
	}

	@Override
	public ArrayList<MeetingRoom> getMeetingRoomList() {
		
		return new ArrayList<MeetingRoom>(entityManager.createQuery(SQLgetMeetingRoomList).getResultList());
	}

	@Override
	public ArrayList<Resource> getResourceList() {
		
		return new ArrayList<Resource>(entityManager.createQuery(SQLgetResourceList).getResultList());
	}
	

}
