package edu.ucsb.cs48s19.hellofriend;

import edu.ucsb.cs48s19.operators.RoomManager;
import edu.ucsb.cs48s19.operators.UserManager;
import edu.ucsb.cs48s19.templates.HelloMessage;
import edu.ucsb.cs48s19.templates.JoinRequest;
import edu.ucsb.cs48s19.templates.Message;
//import edu.ucsb.cs48s19.templates.RoomMessage;
//import edu.ucsb.cs48s19.translate.Translator;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.*;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

//import java.security.Principal;

@Controller
public class HelloFriendController {

    @Autowired
    private MessageSendingOperations ops;

    // connect user
    @MessageMapping("/secured/user/connect/{prefix}/{postfix}")
    @SendTo("/secured/user/queue/specific-room-user/{prefix}/{postfix}")
    public Message connectUser(
            JoinRequest joinRequest,
            @DestinationVariable String prefix,
            @DestinationVariable String postfix) throws Exception {
        // TODO: en-list newly connected user
        System.out.println(joinRequest);
        String sessionId = UserManager.getSessionId(prefix, postfix);
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
        // TODO: un-list disconnected user
        System.out.println("Disconnect user.");
    }

    // Hello message
    @MessageMapping("/secured/user/hello/{prefix}/{postfix}")
    public Message greeting(
            HelloMessage message,
            @DestinationVariable String prefix,
            @DestinationVariable String postfix
    ) throws Exception {
        System.out.println("Hello from: " + message.getName());
        Thread.sleep(100); // simulated delay
        String messageContent = "Hello, "
                + HtmlUtils.htmlEscape(message.getName()) + "!";
        String dest = String.format(
                "/secured/user/queue/specific-room-user/%s/%s",
                prefix, postfix);
        ops.convertAndSend(dest, new Message(messageContent));
        return new Message();
    }

    // Room message
    @MessageMapping("/secured/user/send/{prefix}/{postfix}")
    public void UserChannel(
            @Payload Message message,
            @DestinationVariable String prefix,
            @DestinationVariable String postfix) throws Exception {
        System.out.println(message);

        // TODO: translate message

        String[] listenerList = RoomManager.getListeners(
                UserManager.getSessionId(prefix, postfix));
        for (String listener: listenerList) {
            String dest = String.format(
                    "/secured/user/queue/specific-room-user/%s",
                    listener);
            System.out.println("Send message to " + dest);
            ops.convertAndSend(dest, message);
        }

    }

}