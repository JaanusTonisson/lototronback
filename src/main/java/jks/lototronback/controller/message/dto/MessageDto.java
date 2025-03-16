package jks.lototronback.controller.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Integer messageId;
    private Integer senderId;
    private String senderName;
    private String subject;
    private String body;
    private String senderType;
    private String state;
}