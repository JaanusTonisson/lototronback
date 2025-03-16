package jks.lototronback.service.message;

import jks.lototronback.controller.message.dto.MessageDto;
import jks.lototronback.infrastructure.exception.DataNotFoundException;
import jks.lototronback.infrastructure.exception.ForbiddenException;
import jks.lototronback.persistence.message.Message;
import jks.lototronback.persistence.message.MessageMapper;
import jks.lototronback.persistence.message.MessageRepository;
import jks.lototronback.persistence.profile.Profile;
import jks.lototronback.persistence.profile.ProfileRepository;
import jks.lototronback.persistence.user.User;
import jks.lototronback.persistence.user.UserRepository;
import jks.lototronback.persistence.lunchevent.LunchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public List<MessageDto> getUserMessages(Integer userId) {
        List<Message> messages = messageRepository.findByReceiverUserIdOrderByIdDesc(userId);
        return messageMapper.toMessageDtos(messages);
    }

    public List<MessageDto> getUnreadMessages(Integer userId) {
        List<Message> messages = messageRepository.findByReceiverUserIdAndStateOrderByIdDesc(
                userId, "N"); // "N" for new/unread
        return messageMapper.toMessageDtos(messages);
    }

    public int getUnreadMessageCount(Integer userId) {
        return messageRepository.countByReceiverUserIdAndState(userId, "N");
    }

    @Transactional
    public void markMessageAsRead(Integer userId, Integer messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new DataNotFoundException("Sõnumit ei leitud", 404));

        // Make sure the user is the intended recipient
        if (!message.getReceiverUser().getId().equals(userId)) {
            throw new ForbiddenException("Sul ei ole õigust seda sõnumit muuta", 403);
        }

        message.setState("R"); // "R" for read
        messageRepository.save(message);
    }

    @Transactional
    public void createContactInfoMessage(User receiver, User sender, LunchEvent lunchEvent, String subject, String actionText) {
        // Get profiles for contact information
        Profile senderProfile = profileRepository.findProfileBy(sender.getId())
                .orElseThrow(() -> new DataNotFoundException("Saatja profiili ei leitud", 404));

        Message message = new Message();
        message.setReceiverUser(receiver);
        message.setSenderUser(sender);
        message.setSubject(subject);

        // Construct body with contact information
        String body = String.format(
                "%s %s %s %s on %s at %s. Kontaktandmed: %s %s, Telefon: %s",
                senderProfile.getFirstName(),
                senderProfile.getLastName(),
                actionText,
                lunchEvent.getRestaurant().getName(),
                lunchEvent.getDate().toString(),
                lunchEvent.getTime().toString(),
                senderProfile.getFirstName(),
                senderProfile.getLastName(),
                senderProfile.getPhoneNumber()
        );

        message.setBody(body);
        message.setSenderType("S"); // System message
        message.setState("N");      // New message

        messageRepository.save(message);
    }
}