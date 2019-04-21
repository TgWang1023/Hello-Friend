package edu.ucsb.cs48s19.hellofriend;

import edu.ucsb.cs48s19.templates.HelloMessage;
import edu.ucsb.cs48s19.templates.Message;
import edu.ucsb.cs48s19.templates.RoomMessage;
//import edu.ucsb.cs48s19.translate.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Controller
public class HelloFriendController {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/hello")
    @SendTo("/room/greetings")
    public Message greeting(HelloMessage message) throws Exception {
        System.out.println("Hello from: " + message.getName());
        Thread.sleep(100); // simulated delay
        return new Message("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/room_chat")
    @SendTo("/room/greetings")
    public Message RoomChat(RoomMessage roomMessage) throws Exception {
        String info = String.format("Room: %s, message: %s",
                roomMessage.getRoomNumber(), roomMessage.getRoomMessage());
        System.out.println(info);
        Thread.sleep(100);
        return new Message(info);
    }

    // experiment: get session ID
    @MessageMapping("/secured/room")
    @SendToUser("/queue/reply")
    public void UserChannel(
            @Payload RoomMessage roomMessage,
            Principal principal,
            @Header("simpSessionId") String sessionId) throws Exception {
        System.out.println("Session ID: " + sessionId);
        String info = String.format("Room message: %s",
                roomMessage.getRoomMessage());
        System.out.println(info);
        Message message = new Message(info);
        simpMessageSendingOperations.convertAndSendToUser(
                roomMessage.getRoomNumber(),
                "/secured/user/queue/specific-room", message);
    }

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

}