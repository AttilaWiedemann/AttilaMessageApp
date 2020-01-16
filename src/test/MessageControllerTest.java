import edu.progmatic.messageapp.controllers.MessageController;
import edu.progmatic.messageapp.modell.Message;
import edu.progmatic.messageapp.services.MessageService;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MessageControllerTest {

    InMemoryUserDetailsManager uds = Mockito.mock(InMemoryUserDetailsManager.class);
    MessageService ms = Mockito.mock(MessageService.class);
    @InjectMocks
    MessageController mc = new MessageController(uds, ms);

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void showMessages() {/*
        MessageService ms = Mockito.mock(MessageService.class);
        List<Message> msgList = new ArrayList<>();
        msgList.add(new Message("Aladár", "Kapcs-ford", LocalDateTime.now()));
        Mockito.when(ms.filterMessages(Mockito.anyLong()));*/
    }

    @org.junit.Test
    public void showPage() {
    }

    @org.junit.Test
    public void showOneMessage() {
    }

    @org.junit.Test
    public void showCreateMessage() {
    }

    @org.junit.Test
    public void loggingin() throws Exception {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver("", ".html");
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(mc).setViewResolvers(viewResolver)
                .build();
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.view().name("/login"));    //modelnél .view helyett kell .modeltre hivatkozni
    }

    @org.junit.Test
    public void createMessage() {
    }

    @org.junit.Test
    public void showRegister() {
    }

    @org.junit.Test
    public void newUser() {
    }

    @org.junit.Test
    public void delete() {
    }
}