package edu.progmatic.messageapp.controllers;


import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.services.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TopicController {

    TopicService topicService;
    @PersistenceContext
    EntityManager em;

    public TopicController(TopicService topicService){
        this.topicService = topicService;
    }

    @GetMapping("/topics")
    public String getTopic(Model model){
        model.addAttribute("topic", new Topic());
        return "topicCreator";
    }

    @GetMapping(path = "topicList")
    public String showDelete(Model model){
        model.addAttribute("topicList", em.createQuery("select t from Topic t").getResultList());
        return "listOfTopics";
    }

    @PostMapping("/topics")
    public String createTopic(@ModelAttribute Topic topic){
        topicService.creatingTopic(topic);
        return "messageList";
    }

    @GetMapping("/topic/{id}")
    public String showOneMessage(
            @PathVariable("id") Long topicId,
            Model model){

        Topic topic = em.createQuery("select tp from Topic tp where tp.id =:name", Topic.class)
                .setParameter("name", topicId).getSingleResult();

                //.getResultList();
        //Topic topic = topics.stream().filter(t -> t.getId().equals(topicId)).findFirst().get();

        model.addAttribute("topic", topic);
        //TODO itt jobb lenne a topictól elkérni a message listáját
        /*
        List<Message> messages = em.createQuery("select m from Message m").getResultList();
        model.addAttribute("msgList", messages.stream().filter(m -> m.getMyTopic().equals(topic)).collect(Collectors.toList())
        );*/
        model.addAttribute("msgList", topic.getMessages());
        return "oneTopic";
    }

    @GetMapping(value = {"/deleteTopic/{ID}"})
    public String delete(@PathVariable Long ID) {
        topicService.delete(ID);
        return "redirect:/messages";
    }

}
