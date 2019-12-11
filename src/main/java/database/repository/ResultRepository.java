package database.repository;

import database.model.Result;
import database.utils.EMProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.List;

public class ResultRepository {
    private static EntityManager em = EMProvider.getEntityManager();

    public static Result save(Result result) {
        em.getTransaction().begin();
        em.persist(result);
        em.flush();
        em.getTransaction().commit();
        return result;
    }

    public static void merge(Result result) {
        em.getTransaction().begin();
        em.merge(result);
        em.getTransaction().commit();
    }

    public static Result findById(long id) {
        Result result = em.find(Result.class, id);
        if (result == null)
            throw new EntityNotFoundException("Can't find result with ID: " + id);
        return result;
    }

    public static List<Result> getAll() {
        Query q = em.createQuery("SELECT r FROM Result r");
        return (List<Result>) q.getResultList();
    }

    public static List<Result> findByExperimentId(long id) {
        Query q = em.createQuery("SELECT r FROM Result r where r.experiment.id = :id ");
        return (List<Result>) q.setParameter("id", id).getResultList();
    }

    public static void delete(Result result) {
        em.getTransaction().begin();
        em.remove(result);
        em.getTransaction().commit();
    }

    private static void remove(Result result) {
        em.remove(result);
    }

    public static void deleteByExperimentId(long id) {
        findByExperimentId(id).forEach(ResultRepository::remove);
    }

}
