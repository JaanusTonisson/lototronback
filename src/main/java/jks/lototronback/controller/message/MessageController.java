package jks.lototronback.controller.message;

import io.swagger.v3.oas.annotations.Operation;
import jks.lototronback.controller.message.dto.MessageDto;
import jks.lototronback.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/messages")
    @Operation(summary = "Saa kasutaja sõnumid")
    public List<MessageDto> getUserMessages(@RequestParam Integer userId) {
        return messageService.getUserMessages(userId);
    }

    @GetMapping("/messages/unread")
    @Operation(summary = "saa kasutaja lugemata sõnumid")
    public List<MessageDto> getUnreadMessages(@RequestParam Integer userId) {
        return messageService.getUnreadMessages(userId);
    }

    @GetMapping("/messages/unread/count")
    @Operation(summary = "Sa lugemata sõnumite arv")
    public int getUnreadMessageCount(@RequestParam Integer userId) {
        return messageService.getUnreadMessageCount(userId);
    }

    @PatchMapping("/message/{messageId}/read")
    @Operation(summary = "Märgi sõnum loetuks")
    public void markMessageAsRead(
            @RequestParam Integer userId,
            @PathVariable Integer messageId) {
        messageService.markMessageAsRead(userId, messageId);
    }
}