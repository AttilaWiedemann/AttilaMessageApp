package edu.progmatic.messageapp.modell;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import static javax.persistence.CascadeType.REMOVE;

@Entity
@Table(name = "TOPIC")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "topicName")
    private String topicName;
    @OneToMany(cascade = REMOVE, mappedBy = "myTopic")
    @JsonIgnore
    private List<Message> messages;

    public Topic() {
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }
}
