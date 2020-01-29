package edu.progmatic.messageapp.repositories;

import edu.progmatic.messageapp.modell.Topic;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CustomTopicRepository {

    //Ide szervezzük ki az entity manegert, és ebben az osztályban fognak folyni a lekérdezések a databaseből
    @PersistenceContext
    EntityManager em;

    public List<Topic> findAll(){
        return em.createQuery("SELECT t from Topic t").getResultList();
    }

    public void save(Topic topic){
        em.persist(topic);
    }

    public void delete(Topic topic){
        em.remove(topic);
    }
}