package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAConnection {
	private EntityManager entityManager;
	public EntityManager getEntityManager() {
		return entityManager;
	}
	public static JPAConnection sharedConnection = new JPAConnection();
	public JPAConnection() {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MRBS_PU");
		entityManager = entityManagerFactory.createEntityManager();
	}
}
