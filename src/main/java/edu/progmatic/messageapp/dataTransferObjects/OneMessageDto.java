package edu.progmatic.messageapp.dataTransferObjects;

import java.time.LocalDateTime;

public class OneMessageDto {

    String author;
    String text;
    LocalDateTime creationDate;

    public OneMessageDto(String author, String text, LocalDateTime creationDate) {
        this.author = author;
        this.text = text;
        this.creationDate = creationDate;
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
}
