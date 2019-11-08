package database.repository;

import database.model.Experiment;
import database.utils.EMProvider;

import javax.persistence.EntityManager;

public class ExperimentRepository {
    private static EntityManager em = EMProvider.getEntityManager();

    public static Experiment save(Experiment experiment) {
        em.getTransaction().begin();
        em.persist(experiment);
        em.flush();
        em.getTransaction().commit();
        return experiment;
    }

    public static void merge(Experiment experiment) {
        em.getTransaction().begin();
        em.merge(experiment);
        em.getTransaction().commit();
    }

}
