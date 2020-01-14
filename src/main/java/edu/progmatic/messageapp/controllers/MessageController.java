package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.User;
import edu.progmatic.messageapp.services.MessageService;
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

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class MessageController {

    private MessageService messageService;
    private InMemoryUserDetailsManager userService;

    @Autowired
    public MessageController(UserDetailsService userService, MessageService messageService){   //Qualifier
        this.userService = (InMemoryUserDetailsManager) userService;
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
            Model model){


        List<Message> msgs = messageService.filterMessages(id, author, text, from, to, limit, orderBy, order);

        model.addAttribute("msgList", msgs);
        return "messageList";
    }

    @GetMapping("/message/{id}")
    public String showOneMessage(
            @PathVariable("id") Long msgId,
            Model model){

        Message message = messageService.getMessage(msgId);

        model.addAttribute("message", message);
        return "oneMessage";
    }

    @GetMapping(path = "/showcreate")
    public String showCreateMessage(Model model) {
        Message m = new Message();
        model.addAttribute("message", m);
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
        if(userService.userExists(user.getUsername())) {
            bindingResult.rejectValue("username", "registration.username", "Existing username");
            return "/registration";
        }else{
            user.addAuthority("ROLE_USER");
            userService.createUser(user);
        }
        return "redirect:/login";
    }


}