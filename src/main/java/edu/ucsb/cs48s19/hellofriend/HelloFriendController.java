package edu.ucsb.cs48s19.hellofriend;

import edu.ucsb.cs48s19.operators.RoomManager;
import edu.ucsb.cs48s19.operators.UserManager;
import edu.ucsb.cs48s19.templates.HelloMessage;
import edu.ucsb.cs48s19.templates.JoinRequest;
import edu.ucsb.cs48s19.templates.Message;
import edu.ucsb.cs48s19.templates.RoomMessage;
//import edu.ucsb.cs48s19.translate.Translator;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

//import java.security.Principal;

@Controller
public class HelloFriendController {

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
            RoomManager.createRoom(joinRequest, sessionId);
        } else {
            String msg = RoomManager.joinRoom(joinRequest, sessionId);
            msg = UserManager.getChannel(msg);
            return new Message(msg);
        }
        // TODO: send joiner the receiving URL
        return new Message("Success.");
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
    @SendTo("/secured/user/queue/specific-room-user/{prefix}/{postfix}")
    public Message greeting(HelloMessage message) throws Exception {
        System.out.println("Hello from: " + message.getName());
        Thread.sleep(100); // simulated delay
        return new Message("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    // Room message
    @MessageMapping("/secured/user/send/{prefix}/{postfix}")
    @SendTo("/secured/user/queue/specific-room-user/{prefix}/{postfix}")
    public Message UserChannel(
            @Payload RoomMessage roomMessage,
            @DestinationVariable String prefix,
            @DestinationVariable String postfix) throws Exception {
        System.out.println(roomMessage);
        String info = String.format("Room message: %s",
                roomMessage.getRoomMessage());
        Message message = new Message(info);
        return message;
    }

    /*
    @MessageMapping("/translate_message")
    @SendTo("/room/greetings")
    public Message TranslateMessage(RoomMessage roomMessage) throws Exception {
        String msgCH = roomMessage.getMessageCH();
        String msgEN = roomMessage.getMessageEN();
        System.out.println(String.format("CH: %s; EN: %s", msgCH, msgEN));
        String msg;
        if (msgCH.isEmpty() && !msgEN.isEmpty()) {
            // translate into CH
            msg = "Translated Chinese text";
        } else if (!msgCH.isEmpty() && msgEN.isEmpty()) {
            // translate into EN
            // msg = Translator.zh_CN_to_en(msgCH);
            msg = "Translated English text";
        } else {
            msg = "Please input exactly one language.";
        }
        return new Message(msg);
    }
    */

}