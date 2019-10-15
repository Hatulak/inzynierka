package database;

import database.model.Experiment;
import database.utils.EMFProvider;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

class DatabaseTests {
    @Test
    void saveTest() {
        EntityManagerFactory factory = EMFProvider.getEntityManagerFactory();
        EntityManager em = factory.createEntityManager();
        Experiment experiment = Experiment.builder()
                .name("test")
                .optionsFilePath("testPath")
                .build();
        em.getTransaction().begin();
        em.persist(new Experiment("testowy", "testttt"));
        em.getTransaction().commit();
        em.close();
    }
}
