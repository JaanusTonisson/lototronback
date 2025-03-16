package jks.lototronback.persistence.message;

import jks.lototronback.controller.message.dto.MessageDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper {

    @Mapping(source = "id", target = "messageId")
    @Mapping(source = "senderUser.id", target = "senderId")
    @Mapping(expression = "java(message.getSenderUser().getUsername())", target = "senderName")
    @Mapping(source = "subject", target = "subject")
    @Mapping(source = "body", target = "body")
    @Mapping(source = "senderType", target = "senderType")
    @Mapping(source = "state", target = "state")
    MessageDto toMessageDto(Message message);

    List<MessageDto> toMessageDtos(List<Message> messages);
}