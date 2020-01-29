package edu.progmatic.messageapp.modell;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MESSAGE")
public class Message {

    private static Long maxId = 0L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@NotEmpty
    //@NotNull
    @Column(name = "author")
    private String author;
    @NotEmpty
    @NotNull
    @NotBlank
    @Column(name = "text")
    private String text;
    private boolean isDeleted;

    //@DateTimeFormat(pattern = "yyyy/MMMM/dd HH:mm")
    private LocalDateTime creationDate;

    @ManyToOne
    private Topic myTopic;

    @ManyToOne
    private Message parent;

    @OneToMany(mappedBy = "parent")
    private List<Message> children;

    public Message() {
    }

    public Message(String author, String text, LocalDateTime creationDate) {
        //this.id = maxId++;
        this.author = author;
        this.text = text;
        this.creationDate = creationDate;
        this.isDeleted = false;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Topic getMyTopic() {
        return myTopic;
    }

    public void setMyTopic(Topic myTopic) {
        this.myTopic = myTopic;
    }

    public static Long getMaxId() {
        return maxId;
    }

    public static void setMaxId(Long maxId) {
        Message.maxId = maxId;
    }

    public Message getParent() {
        return parent;
    }

    public void setParent(Message parent) {
        this.parent = parent;
    }

    public List<Message> getChildren() {
        return children;
    }

    public void setChildren(List<Message> children) {
        this.children = children;
    }
}
