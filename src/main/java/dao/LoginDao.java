package dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;


import entity.Login;

public class LoginDao {
	private EntityManager entityManager = JPAConnection.sharedConnection.getEntityManager();

	public String authenticate(String username, String password) {
		String role="error";
		entityManager.getTransaction().begin();
		ArrayList<Login> users = new ArrayList<Login>(entityManager.createQuery("SELECT login FROM entity.Login login where login.username = :username and login.password = :password").setParameter("username", username).setParameter("password", password).getResultList());
		if(users.size()==1) {
			role = users.get(0).getRole();
		}
		entityManager.getTransaction().commit();
		return role;
	}

}
