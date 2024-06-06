package com.spring.iot.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Message {
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private Status status;

    public Message(String senderName, String receiverName, String message, String date, Status status) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.message = message;
        this.date = date;
        this.status = status;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }
}
