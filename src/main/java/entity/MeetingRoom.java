package entity;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MeetingRoom {
	
	@Id
	private Integer id;
	private String meetingRoomName;
	public Integer getId() {
		return id;
	}
	public String getMeetingRoomName() {
		return meetingRoomName;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}
	
	
}
