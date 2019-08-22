package resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import entity.Login;
import service.LoginService;

@Path("MRBS")
public class LoginResource {
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Login authenticate(Login user) {
		LoginService service = new LoginService();
		user.setRole(service.authenticate(user.getUsername(), user.getPassword()));
		return user;
	}
}
