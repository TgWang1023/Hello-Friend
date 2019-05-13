
package edu.ucsb.cs48s19.hellofriend;

import edu.ucsb.cs48s19.operators.Console;
import edu.ucsb.cs48s19.operators.Manager;
import edu.ucsb.cs48s19.templates.*;
//import edu.ucsb.cs48s19.translate.Translator;
import edu.ucsb.cs48s19.translate.API_access;

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
        Console.log(joinRequest.toString());
        String sessionId = Manager.getSessionId(prefix, postfix);
        if (joinRequest.getRequest() == JoinRequest.CREATE_REQUEST) {
            int createFlag = Manager.createRoom(joinRequest, sessionId);
            if (createFlag != Manager.CREATE_SUCCESS) {
                // incomplete form
                if (createFlag == Manager.ERROR_STATE) {
                    return new AdvancedMessage(
                        "Please fill all entries in the form.",
                        Manager.SYSTEM_FLAG,
                        createFlag,
                        Manager.SYSTEM_NAME,
                        Manager.TO_SENDER_FLAG
                    );
                }
                return new AdvancedMessage(
                        API_access.translate(
                                "This room name has been occupied.",
                                "en",
                                joinRequest.getUserLanguage()), // user's language
                        Manager.SYSTEM_FLAG,
                        createFlag,
                        Manager.SYSTEM_NAME,
                        Manager.TO_SENDER_FLAG
                );
            }
            return new AdvancedMessage(
                    API_access.translate(
                            "Create success.",
                            "en",
                            joinRequest.getUserLanguage()), // user's language
                    Manager.SYSTEM_FLAG,
                    createFlag,
                    Manager.SYSTEM_NAME,
                    Manager.TO_SENDER_FLAG
            );
        } else {
            int joinFlag = Manager.joinRoom(joinRequest, sessionId);
            if (joinFlag != Manager.JOIN_SUCCESS) {
                if (joinFlag == Manager.ROOM_NOT_EXISTS) {
                    return new AdvancedMessage(
                            "Join Failed. No such room with the name.",
                            Manager.SYSTEM_FLAG,
                            joinFlag,
                            Manager.SYSTEM_NAME,
                            Manager.TO_SENDER_FLAG
                    );
                } else if (joinFlag == Manager.ROOM_IS_FULL) {
                    return new AdvancedMessage(
                            "Join Failed. The room is full.",
                            Manager.SYSTEM_FLAG,
                            joinFlag,
                            Manager.SYSTEM_NAME,
                            Manager.TO_SENDER_FLAG
                    );
                }
                // incomplete form
                else if (joinFlag == Manager.ERROR_STATE) {
                    return new AdvancedMessage(
                        "Please fill all entries in the form.",
                        Manager.SYSTEM_FLAG,
                        joinFlag,
                        Manager.SYSTEM_NAME,
                        Manager.TO_SENDER_FLAG
                    );
                }

            }
            return new AdvancedMessage(
                    "Join success.",
                    Manager.SYSTEM_FLAG,
                    joinFlag,
                    Manager.SYSTEM_NAME,
                    Manager.TO_SENDER_FLAG
            );
        }
    }

    // disconnect user
    @MessageMapping("/secured/user/disconnect/{prefix}/{postfix}")
    public void disconnectUser(
            @DestinationVariable String prefix,
            @DestinationVariable String postfix) throws Exception {
        User owner = Manager.removeUser(prefix, postfix);
        Console.log("Disconnect user.");
        Console.log(owner);
        if (owner != null) {
            String dest = String.format(
                    "/secured/user/queue/specific-room-user/%s",
                    owner.getSessionId());
            String quitMessage = "Another user has disconnected.";
            if (owner.getLanguage().compareTo("en") != 0) {
                quitMessage = API_access.translate(
                        quitMessage,
                        "en",
                        owner.getLanguage());
            }
            ops.convertAndSend(dest, new AdvancedMessage(
                    quitMessage,
                    Manager.SYSTEM_FLAG,
                    Manager.QUIT_SUCCESS,
                    Manager.SYSTEM_NAME,
                    Manager.TO_RECEIVER_FLAG
            ));

        }
    }

    // channel message
    @MessageMapping("/secured/user/send/{prefix}/{postfix}")
    public void channelMessage(
            @Payload Message message,
            @DestinationVariable String prefix,
            @DestinationVariable String postfix) throws Exception {
        Console.log(message);

        Pair[] messageList = Manager.getMessageList(prefix, postfix, message);
        for (Pair pair: messageList) {
            String dest = String.format(
                    "/secured/user/queue/specific-room-user/%s",
                    pair.getReceiver().getSessionId());
            Console.log("Send message to " + dest);
            ops.convertAndSend(dest, pair.getMessage());
        }
    }

}