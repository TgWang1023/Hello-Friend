package edu.ucsb.cs48s19.hellofriend;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {


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

    @MessageMapping("/translate_message")
    @SendTo("/room/greetings")
    public Message TranslateMessage(RoomMessage roomMessage) throws Exception {
        String msgCH = roomMessage.getMessageCH();
        String msgEN = roomMessage.getMessageEN();
        System.out.println(String.format("CH: %s; EN: %s", msgCH, msgEN));
        String msg;
        if (msgCH.isEmpty() && !msgEN.isEmpty()) {
            // translate into CH
            msg = "Chinese text";
        } else if (!msgCH.isEmpty() && msgEN.isEmpty()) {
            // translate into EN
            msg = "English text";
        } else {
            msg = "Please input exactly one language.";
        }
        return new Message(msg);
    }

}