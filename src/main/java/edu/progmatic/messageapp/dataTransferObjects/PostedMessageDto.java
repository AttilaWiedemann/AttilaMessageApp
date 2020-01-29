package edu.progmatic.messageapp.dataTransferObjects;

public class PostedMessageDto {
    Long id;

    public PostedMessageDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
