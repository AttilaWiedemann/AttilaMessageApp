package edu.progmatic.messageapp.services;

import edu.progmatic.messageapp.modell.ChatMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @PersistenceContext
    EntityManager em;



    //TODO create chatMessage metódus

    @Transactional
    public void createChatMessage(ChatMessage chatMessage){
        em.persist(chatMessage);
    }


    public List<ChatMessage> getAllChatMessages(){
        return em.createQuery("select m from ChatMessage m").getResultList();
    }

}
