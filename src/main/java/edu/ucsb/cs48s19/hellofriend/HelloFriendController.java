package edu.ucsb.cs48s19.hellofriend;

import edu.ucsb.cs48s19.operators.RoomManager;
import edu.ucsb.cs48s19.templates.AdvancedMessage;
import edu.ucsb.cs48s19.templates.JoinRequest;
import edu.ucsb.cs48s19.templates.Message;
//import edu.ucsb.cs48s19.translate.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;


@Controller
public class HelloFriendController {

    @Autowired
    private MessageSendingOperations ops;

    // connect user
    @MessageMapping("/secured/user/connect/{prefix}/{postfix}")
    @SendTo("/secured/user/queue/specific-room-user/{prefix}/{postfix}")
    public Message connectUser(
            @NotNull JoinRequest joinRequest,
            @DestinationVariable String prefix,
            @DestinationVariable String postfix) throws Exception {
        System.out.println(joinRequest);
        String sessionId = RoomManager.getSessionId(prefix, postfix);
        if (joinRequest.getRequest() == 1) {
            boolean flag = RoomManager.createRoom(joinRequest, sessionId);
            if (!flag) {
                return new Message("This room name has been occupied.");
            }
            return new Message("Create Success.");
        } else {
            boolean flag = RoomManager.joinRoom(joinRequest, sessionId);
            if (!flag) {
                return new Message("Join Failed. No such room or the room is full.");
            }
            return new Message("Join Success.");
        }
    }

    // disconnect user
    @MessageMapping("/secured/user/disconnect/{prefix}/{postfix}")
    public void disconnectUser(
            @DestinationVariable String prefix,
            @DestinationVariable String postfix) throws Exception {
        RoomManager.removeUser(prefix, postfix);
        System.out.println("Disconnect user.");
    }

    // channel message
    @MessageMapping("/secured/user/send/{prefix}/{postfix}")
    public void channelMessage(
            @Payload Message message,
            @DestinationVariable String prefix,
            @DestinationVariable String postfix) throws Exception {
        System.out.println(message);

        // TODO: translate message

        String[] listenerList = RoomManager.getListeners(prefix, postfix);
        for (String listener: listenerList) {
            String dest = String.format(
                    "/secured/user/queue/specific-room-user/%s",
                    listener);
            System.out.println("Send message to " + dest);
            ops.convertAndSend(dest, message);
        }
    }

}