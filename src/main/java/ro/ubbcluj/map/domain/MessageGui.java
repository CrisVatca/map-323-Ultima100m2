package ro.ubbcluj.map.domain;

import java.time.LocalDateTime;

public class MessageGui {

    private Long id;
    private String from;
    private String to;
    private String message;
    private LocalDateTime data;
    private Long reply;

    public MessageGui(Long id,String from, String to, String message, LocalDateTime data, Long reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        this.reply = reply;
    }

    public Long getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Long getReply() {
        return reply;
    }
}
