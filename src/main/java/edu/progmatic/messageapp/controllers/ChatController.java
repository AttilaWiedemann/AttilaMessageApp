package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.modell.ChatMessage;
import edu.progmatic.messageapp.modell.ChatMessage_;
import edu.progmatic.messageapp.services.ChatService;
import edu.progmatic.messageapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {

    private UserService userService;
    private ChatService chatService;

    @Autowired
    public ChatController(UserService userService, ChatService chatService){
        this.userService = userService;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage message){
        chatService.createChatMessage(message);
        return message;
    }

    @GetMapping(path = "/index")
    public String getChatPage(Model model){
        ChatMessage message = new ChatMessage();
        model.addAttribute("ChatMessage", message);
        List<String> users = userService.getUsernames();
        model.addAttribute("userList", users);
        List<ChatMessage> chatMessages = chatService.getAllChatMessages();
        model.addAttribute("chatMessageList", chatMessages);
        return "index";
    }
}
