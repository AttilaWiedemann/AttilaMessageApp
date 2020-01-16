package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

    public List<Message> filterMessages(Long id, String author, String text, LocalDateTime from, LocalDateTime to, Integer limit, String orderBy, String order, String deleted) {
        LOGGER.info("filterMessages method started");
        LOGGER.debug("id: {}, author: {}, text: {}", id, author, text);
        Comparator<Message> msgComp = Comparator.comparing((Message::getCreationDate));
        LOGGER.debug("filterMessages is going to compare");
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
        }return msgs;
    }

    public Message getMessage(Long msgId) {
        List<Message> messages = em.createQuery("SELECT m FROM Message m").getResultList();
        Optional<Message> message = messages.stream().filter(m -> m.getId().equals(msgId)).findFirst();
        //itt a lambde kifejezés helyett a SELECT-es dolgot is lehet használni
        return message.get();
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
        List<Message> messages = em.createQuery("SELECT m FROM Message m").getResultList();
        String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        m.setAuthor(loggedInUserName);
        m.setCreationDate(LocalDateTime.now());
        //m.setId((long) messages.size());
        em.persist(m);
        messages.add(m);
    }

    @Transactional
    public void delete(Long ID) {
        List<Message> messages = em.createQuery("SELECT m FROM Message m").getResultList();
        for(Message m : messages){
            if(m.getId().equals(ID)){
                m.setDeleted(true);
                //em.remove(m);
            }
        }
    }



}
