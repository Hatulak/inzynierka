package database.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMProvider {
    private static EntityManager entityManager;

    private static EntityManager buildEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MRISimulatorPU");
        return emf.createEntityManager();
    }

    public static EntityManager getEntityManager() {
        if (entityManager == null) {
            entityManager = buildEntityManager();
        }
        return entityManager;
    }
}
