package database;

import database.model.Experiment;
import database.utils.EMProvider;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

class DatabaseTests {
    @Test
    void saveTest() {
        EntityManager em = EMProvider.getEntityManager();
        Experiment experiment = Experiment.builder()
                .name("test")
                .optionsFilePath("testPath")
                .build();
        em.getTransaction().begin();
        em.persist(Experiment.builder().name("asd").build());
        em.getTransaction().commit();
        em.close();
    }
}
