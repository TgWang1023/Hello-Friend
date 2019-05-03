package edu.ucsb.cs48s19.hellofriend;

import ch.qos.logback.core.pattern.color.MagentaCompositeConverter;
import edu.ucsb.cs48s19.operators.Manager;
import edu.ucsb.cs48s19.templates.AdvancedMessage;
import edu.ucsb.cs48s19.templates.JoinRequest;
import edu.ucsb.cs48s19.templates.Message;
//import edu.ucsb.cs48s19.translate.Translator;
import edu.ucsb.cs48s19.templates.Pair;
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
    public AdvancedMessage connectUser(
            @NotNull JoinRequest joinRequest,
            @DestinationVariable String prefix,
            @DestinationVariable String postfix) throws Exception {
        System.out.println(joinRequest);
        String sessionId = Manager.getSessionId(prefix, postfix);
        if (joinRequest.getRequest() == 1) {
            int createFlag = Manager.createRoom(joinRequest, sessionId);
            if (createFlag != 10) {
//                return new Message("This room name has been occupied.");
                return new AdvancedMessage(
                        "This room name has been occupied.",
                        Manager.SYSTEM_FLAG,
                        createFlag,
                        Manager.SYSTEM_NAME,
                        Manager.SENDER_FLAG
                );
            }
//            return new Message("Create Success.");
            return new AdvancedMessage(
                    "Create success.",
                    Manager.SYSTEM_FLAG,
                    createFlag,
                    Manager.SYSTEM_NAME,
                    Manager.SENDER_FLAG
            );
        } else {
            int joinFlag = Manager.joinRoom(joinRequest, sessionId);
            if (joinFlag != Manager.JOIN_SUCCESS) {
//                return new Message("Join Failed. No such room or the room is full.");
                if (joinFlag == Manager.ROOM_NOT_EXISTS) {
                    return new AdvancedMessage(
                            "Join Failed. No such room with the name.",
                            Manager.SYSTEM_FLAG,
                            joinFlag,
                            Manager.SYSTEM_NAME,
                            Manager.SENDER_FLAG
                    );
                } else if (joinFlag == Manager.ROOM_IS_FULL) {
                    return new AdvancedMessage(
                            "Join Failed. The room is full.",
                            Manager.SYSTEM_FLAG,
                            joinFlag,
                            Manager.SYSTEM_NAME,
                            Manager.SENDER_FLAG
                    );
                }
            }
//            return new Message("Join Success.");
            return new AdvancedMessage(
                    "Join success.",
                    Manager.SYSTEM_FLAG,
                    joinFlag,
                    Manager.SYSTEM_NAME,
                    Manager.SENDER_FLAG
            );
        }
    }

    // disconnect user
    @MessageMapping("/secured/user/disconnect/{prefix}/{postfix}")
    public void disconnectUser(
            @DestinationVariable String prefix,
            @DestinationVariable String postfix) throws Exception {
        Manager.removeUser(prefix, postfix);
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

        Pair[] messageList = Manager.getMessageList(prefix, postfix, message);
        for (Pair pair: messageList) {
            String dest = String.format(
                    "/secured/user/queue/specific-room-user/%s",
                    pair.getSessionId());
            System.out.println("Send message to " + dest);
            ops.convertAndSend(dest, pair.getMessage());
        }
    }

}