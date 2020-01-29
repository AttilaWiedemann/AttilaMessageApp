package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.Authority;
import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.Topic;
import edu.progmatic.messageapp.modell.User;
import edu.progmatic.messageapp.services.MessageService;
import edu.progmatic.messageapp.services.TopicService;
import edu.progmatic.messageapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class MessageController {

    @PersistenceContext
    EntityManager em;

    private MessageService messageService;
    private UserService userService; //ennek a helyére lesz a saját

    @Autowired
    public MessageController(UserService userService, MessageService messageService){   //Qualifier
        this.userService = userService;
        this.messageService = messageService;
    }


    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public String showMessages(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime to,
            @RequestParam(name = "limit", defaultValue = "100", required = false) Integer limit,
            @RequestParam(name = "orderby", defaultValue = "", required = false) String orderBy,
            @RequestParam(name = "order", defaultValue = "asc", required = false) String order,
            @RequestParam(name = "deleted", required = false) String deleted,
            @RequestParam(name = "topic", required = false) String topic,
            Model model){
        List<Message> msgs = messageService.filterMessages(id, author, text, from, to, limit, orderBy, order, deleted, topic);
        //Messagek filterezése. Adminként a törölt üzenetek is
        // látszódniak, sima felhasználóként pedig csak a filterezett messagek
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().equals("[ROLE_ADMIN]")){
            model.addAttribute("msgList", msgs);
        }else {
            List<Message> filtmsgs = msgs.stream().filter(message -> !message.isDeleted()).collect(Collectors.toList());
            model.addAttribute("msgList", filtmsgs);
        }


/*
        List<Topic> topics = messageService.getTopics();
        ArrayList<String> elements = topics;
 */



        return "messageList";
    }


    @GetMapping(value = "/")
    public String showPage(){
        return "home";
    }

    @GetMapping("/message/{id}")
    public String showOneMessage(
            @PathVariable("id") Long msgId,
            Model model){

        Message message = messageService.getMessage(msgId);

        model.addAttribute("message", message);
        return "oneMessage";
    }


    //TODO---------------------------------------------------------------------------
    @GetMapping("/message/{id}/comment")
    public String showCommentCreator(@PathVariable("id") Long msgId, Model model){
        Message parentMessage = messageService.getMessage(msgId);
        Message comment = new Message();
        comment.setParent(parentMessage);
        model.addAttribute("parent", parentMessage);
        model.addAttribute("message", comment);
        return "createComment";
    }

/*
    @PostMapping("/createcomment")
    public String postingComment(@ModelAttribute("parent") Message parent,
                                 @ModelAttribute("message") Message comment){
        comment.setParent(parent);
        comment.setMyTopic(parent.getMyTopic());
        messageService.createMessage(comment);
        return "messageList";
    }*/

    @PostMapping(path = "/comment/{id}")
    public String postComment(@PathVariable("id") Long msgId, @ModelAttribute("message") Message comment, Model model){
        messageService.createComment(msgId, comment);
        return "home";
    }

    @GetMapping(path = "/showcreate")
    public String showCreateMessage(Model model) {
        Message m = new Message();
        model.addAttribute("message", m);
        List<Topic> topics = messageService.getTopics();
        model.addAttribute("topicList", topics);
        return "createMessage";
    }

    @GetMapping("/login")
    public String loggingin(){
        return "/login";
    }

    @PostMapping(path = "/createmessage")
    public String createMessage(@Valid @ModelAttribute("message") Message m, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
          return "createMessage";
        }

        messageService.createMessage(m);



        //return "home";
        //return "redirect:/messages?orderby=createDate&order=desc";
        //return "redirect:/messages?id=" + m.getId();
        return "redirect:/message/" + m.getId();
    }

    @GetMapping(path = "/registration")
    public String showRegister(Model model, User user){
        model.addAttribute(new User());
        return "/registration";
    }

    @PostMapping(path = "/registration")
    public String newUser(@Valid @ModelAttribute() User user,
                          BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "/registration";
        }
        /*if(userService.userExists(user.getUsername())) {
            bindingResult.rejectValue("username", "registration.username", "Existing username");
            return "/registration";
        }*/else{
            Set<Authority> authoritiesSet = new HashSet<>();

            authoritiesSet.add((Authority) em.createQuery("select a from Authority a where a.authority like 'ROLE_USER'").getSingleResult());
            user.setAuthorities(authoritiesSet);
            userService.createUser(user);
        }
        return "redirect:/login";
    }

    @GetMapping(value = {"/delete/{ID}"})
    public String delete(@PathVariable Long ID) {
        messageService.delete(ID);
        return "redirect:/messages";
    }

/*
    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{ID}")
    public @ResponseBody Boolean deleteJson(@PathVariable Long ID){
        return messageService.delete(ID);
    }
 */

    @GetMapping(path = "/modifymessage")
    public String modifyMessage(@RequestParam(name = "id", required = false) Long id,
                                @RequestParam(name = "text", required = false) String text){
        messageService.modifyMessage(id, text);
        return "redirect:/messages";
    }

    /*
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public @ResponseBody List<Message> getMessages(){
        return messageService.getAllMessages();
    }*/

}