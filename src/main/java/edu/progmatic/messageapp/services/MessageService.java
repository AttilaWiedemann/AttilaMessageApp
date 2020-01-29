package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service    //olyan, mint a component, csak külön annotációja van
public class MessageService {

    @PersistenceContext
    EntityManager em;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    /*
    private List<Message> messages = new ArrayList<>();

    {
        messages.add(new Message("Aladár", "Mz/x jelkezz, jelkezz", LocalDateTime.now().minusDays(10)));
        messages.add(new Message("Kriszta", "Bemutatom lüke Aladárt", LocalDateTime.now().minusDays(5)));
        messages.add(new Message("Blöki", "Vauuu", LocalDateTime.now()));
        messages.add(new Message("Maffia", "miauuu", LocalDateTime.now()));
        messages.add(new Message("Aladár", "Kapcs/ford", LocalDateTime.now().plusDays(5)));
        messages.add(new Message("Aladár", "Adj pénzt!", LocalDateTime.now().plusDays(10)));
        messages.add(new Message("Obi-Wan", "Hello there!", LocalDateTime.now().minusDays(15)));
        messages.add(new Message("Palpatine", "Did you ever hear the tragedy of Darth Plagueis The Wise?", LocalDateTime.now()));
        messages.add(new Message("Rick Astley", "Never gonna give you up", LocalDateTime.now().minusDays(20)));
    }*/

    public List<Message> filterMessages(Long id, String author, String text, LocalDateTime from, LocalDateTime to, Integer limit, String orderBy, String order, String deleted, String topic) {
        LOGGER.info("filterMessages method started");
        LOGGER.debug("id: {}, author: {}, text: {}", id, author, text);
        Comparator<Message> msgComp = Comparator.comparing((Message::getCreationDate));
        LOGGER.debug("filterMessages is going to compare");
/*
        switch (orderBy){
            case "text":
                msgComp = Comparator.comparing((Message::getText));
                break;
            case "id":
                msgComp = Comparator.comparing((Message::getId));
                break;
            case "author":
                msgComp = Comparator.comparing((Message::getAuthor));
                break;
            default:
                break;
        }
        if(order.equals("desc")){
            msgComp = msgComp.reversed();
        }
        List<Message> messages = em.createQuery("SELECT m FROM Message m", Message.class).getResultList();
        List<Message> msgs = messages.stream()
                .filter(m -> id == null ? true : m.getId().equals(id))
                .filter(m -> StringUtils.isEmpty(author) ? true : m.getAuthor().contains(author))
                .filter(m -> StringUtils.isEmpty(text) ? true : m.getText().contains(text))
                .filter(m -> from == null ? true : m.getCreationDate().isAfter(from))
                .filter(m -> to == null ? true : m.getCreationDate().isBefore(to))
                .filter(m -> StringUtils.isEmpty(topic) ? true : m.getMyTopic().getTopicName().equals(topic))
                .sorted(msgComp)
                .limit(limit).collect(Collectors.toList());
        if(deleted != null) {
            if (deleted.equals("visible")) {
                return msgs.stream().filter(m -> !m.isDeleted()).collect(Collectors.toList());
            } else if (deleted.equals("delete")) {
                return msgs.stream().filter(m -> m.isDeleted()).collect(Collectors.toList());
            } else {
                return msgs;
            }
        }
        return msgs;*/
//TODO ____________________________mindez criteriaBuilderrel______________________________________________

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Message> cQuery = cb.createQuery(Message.class);
        Root<Message> m = cQuery.from(Message.class);
        Join<Message, Topic> topics = m.join(Message_.myTopic);
        if(!StringUtils.isEmpty(author)){   //szerző alapján
            cQuery.select(m).where(cb.equal(m.get(Message_.author), author));
        }if(!StringUtils.isEmpty(text)){    //szöveg alapján
            cQuery.select(m).where(cb.like(m.get(Message_.text), "%" + text + "%"));
        }if(from != null){    //idő -kezdő meg van adva
            cQuery.select(m).where(cb.greaterThan(m.get(Message_.creationDate), from));
        }if(to != null){    //idő -vég meg van adva
            cQuery.select(m).where(cb.lessThan(m.get(Message_.creationDate), to));
        }if(deleted != null){
            if(deleted.equals("visible")){
                cQuery.select(m).where(cb.equal(m.get(Message_.isDeleted), Boolean.FALSE));
            }else if(deleted.equals("delete")){
                cQuery.select(m).where(cb.equal(m.get(Message_.isDeleted), Boolean.TRUE));
            }
        }if(!StringUtils.isEmpty(topic)){   //topic alapján
            cQuery.select(m).where(cb.equal(topics.get(Topic_.TOPIC_NAME), topic));
        }

        switch (orderBy){
            case "text":
                if(order.equals("desc")) {
                    cQuery.orderBy(cb.desc(m.get(Message_.text)));
                }else {
                    cQuery.orderBy(cb.asc(m.get(Message_.text)));
                }
                break;
            case "id":
                if(order.equals("desc")) {
                    cQuery.orderBy(cb.desc(m.get(Message_.creationDate)));
                }else{
                    cQuery.orderBy(cb.asc(m.get(Message_.creationDate)));
                }
                break;
            case "author":
                if(order.equals("desc")) {
                    cQuery.orderBy(cb.desc(m.get(Message_.author)));
                }else {
                    cQuery.orderBy(cb.asc(m.get(Message_.author)));
                }
                break;
            default:
                break;
        }

        List<Message> resultList = em.createQuery(cQuery).getResultList();
        return resultList;


    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Message getMessage(Long msgId) {
        Message message =em.find(Message.class, msgId);
        /*try {
            Thread.sleep(10000);
        }catch (Exception ignored){}*/
        return message;
    }
/*
    public void createMessage(Message m) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = "user.getUsername();";
        System.out.println(name);
        m.setAuthor(name);
        m.setCreationDate(LocalDateTime.now());
        m.setId((long) messages.size());
        messages.add(m);
    }*/

    @Transactional
    public void createMessage(Message m) {
        String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        m.setAuthor(loggedInUserName);
        m.setCreationDate(LocalDateTime.now());
        //m.setId((long) messages.size());

        Topic topic = em.createQuery("SELECT t from Topic t where t.topicName =:name", Topic.class)
                .setParameter("name", m.getMyTopic().getTopicName()).getSingleResult();
        topic.getMessages().add(m);
        m.setMyTopic(topic);
        em.persist(m);
    }

    @Transactional
    public void createComment(Long msgId, Message comment){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Message parentMessage = getMessage(msgId);
        comment.setAuthor(user.getUsername());
        comment.setParent(parentMessage);
        comment.setMyTopic(parentMessage.getMyTopic());
        comment.setCreationDate(LocalDateTime.now());

        em.persist(comment);
    }

    @Transactional
    public boolean delete(Long ID) {
        List<Message> messages = em.createQuery("SELECT m FROM Message m").getResultList();
        for(Message m : messages){
            if(m.getId().equals(ID)){
                m.setDeleted(true);
                return true;
                //em.remove(m);
            }
        }
        return false;
    }

    public List getTopics(){
        List<Topic> topics = em.createQuery("SELECT t FROM Topic t").getResultList();
        return topics;
    }

    //____________MÓDOSÍTÓ_____________
    @Transactional()
    public void modifyMessage(Long id, String text){
        Message m = em.createQuery("select m from Message m where m.id =:xy", Message.class).setParameter("xy", id).getSingleResult();
        m.setText(text);
    }

    public List<Message> getAllMessages(){
        return em.createQuery("select m from Message m").getResultList();
    }


}
