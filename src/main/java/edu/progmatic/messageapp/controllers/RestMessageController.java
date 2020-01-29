package edu.progmatic.messageapp.controllers;

import edu.progmatic.messageapp.dataTransferObjects.OneMessageDto;
import edu.progmatic.messageapp.dataTransferObjects.PostedMessageDto;
import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.modell.NoSuchMessageException;
import edu.progmatic.messageapp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/messages")
public class RestMessageController {

    private MessageService messageService;

    @Autowired
    public RestMessageController(MessageService ms){
        this.messageService = ms;
    }

    @GetMapping()
    public List<Message> returnMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping(value = "/{id}")
    public OneMessageDto returnOneMessage(@PathVariable("id") Long messageId){
        Message m = messageService.getMessage(messageId);
        /*hibával való visszatérés:
        -exception dobása. Ha a find hívás nullal tér vissza -> nem létezik -> (saját) exception dobás. (ink runtime, nem kell szarozni a throws-zal)
        -@Response status(code = httpStatus.not_found, reason="No such message")
        -hátrány: a mycontroller advice el fogja kapni a hibát, ki kell kommentelni a metódusát -> hibakód fog visszatérni
        Másik megoldás:
        controllerben:
        @Exceptionhandler(sajátExceptonöm.class) - restcontroller/controlleradvice
        bemeneti: maga az exception
        public ResponseEntity<saját dto-m>...(..){..}
            dto példányosítása
            dto.set message, hibaüzenet beállítás
            return ResponseEntity.status(httpstatus.Not_Found).body(dto objekt)

         */
        if(m == null){
            throw new NoSuchMessageException();
        }
        return new OneMessageDto(m.getAuthor(), m.getText(), m.getCreationDate());

    }

    @PostMapping()
    public PostedMessageDto postMessage(@Valid @RequestBody Message message){
        messageService.createMessage(message);
        return new PostedMessageDto(message.getId());
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody Message deleteJson(@PathVariable Long id){
        messageService.delete(id);
        return messageService.getMessage(id);
    }



}
