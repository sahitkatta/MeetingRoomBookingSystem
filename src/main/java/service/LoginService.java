package service;

import dao.LoginDao;

public class LoginService {
	private LoginDao loginDao = new LoginDao();
	public String authenticate(String username, String password) {
		return loginDao.authenticate(username, password);
	}
}
