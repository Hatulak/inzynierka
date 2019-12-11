package database.repository;

import database.model.Experiment;
import database.utils.EMProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.List;

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

    public static List<Experiment> getAll() {
        Query q = em.createQuery("SELECT e FROM Experiment e");
        return (List<Experiment>) q.getResultList();
    }

    public static Experiment findById(long id) {
        Experiment experiment = em.find(Experiment.class, id);
        if (experiment == null)
            throw new EntityNotFoundException("Can't find experiment with ID: " + id);
        return experiment;
    }

    public static void delete(Experiment experiment) {
        em.getTransaction().begin();
        ResultRepository.deleteByExperimentId(experiment.getId());
        em.remove(experiment);
        em.getTransaction().commit();
    }

}
