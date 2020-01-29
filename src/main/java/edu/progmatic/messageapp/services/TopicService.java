package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.repositories.CustomTopicRepository;
import edu.progmatic.messageapp.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicService {

    private TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository){
        this.topicRepository = topicRepository;
    }

    /*
    //Ez nem fog kelleni ide:
    @PersistenceContext
    EntityManager em;
     */

    public List<Topic> getAllTopics(){
        return topicRepository.findAll();
    }

    @Transactional
    public void creatingTopic(Topic t){
        List<Topic> topics = getAllTopics();
        List<String> topicNames = topics.stream().map(topic -> topic.getTopicName()).collect(Collectors.toList());
        if(topicNames.contains(t.getTopicName())){
            return;
        }
        //em.persist(t);
        topicRepository.save(t);
    }


    @Transactional
    public void delete(Long ID) {
        List<Topic> topics = getAllTopics();
        for(Topic t : topics){
            if(t.getId().equals(ID)){
                topicRepository.delete(t);
            }
        }
    }




}
