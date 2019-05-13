package edu.ucsb.cs48s19.operators;

import edu.ucsb.cs48s19.templates.*;
import edu.ucsb.cs48s19.translate.API_access;

import java.util.HashMap;

public class Manager {

    // CONSTANTS
    public static final int NORMAL_STATE = 0;
    public static final int ERROR_STATE = 1;

    public static final int CREATE_SUCCESS = 10;
    public static final int ROOM_NAME_OCCUPIED = 11;
    public static final int QUIT_SUCCESS = 12;

    public static final int JOIN_SUCCESS = 20;
    public static final int ROOM_NOT_EXISTS = 21;
    public static final int ROOM_IS_FULL = 22;

    public static final boolean SYSTEM_FLAG = true;
    public static final boolean NON_SYSTEM_FLAG = false;

    public static final boolean TO_SENDER_FLAG = false;
    public static final boolean TO_RECEIVER_FLAG = true;

    public static final String SYSTEM_NAME = "SYSTEM";


    // map room name to rooms
    private static HashMap<String, Room> roomNameToRoom = new HashMap<>();
    // map user's session ID to rooms
    private static HashMap<String, Room> sessionIdToRoom = new HashMap<>();
    // map user's session ID to users
    private static HashMap<String, User> userNameToUser = new HashMap<>();

    private Manager() { }

    public static int createRoom(
            JoinRequest joinRequest,
            String sessionId) {

        if (joinRequest.hasEmptyEntry()) {
            return 1;
        }

        if (roomNameToRoom.get(joinRequest.getRoomName()) != null) {
            Console.log("The room name is occupied!");
            return ROOM_NAME_OCCUPIED;
        }

        User owner = new User(joinRequest.getUserName(),
                joinRequest.getUserLanguage(),
                sessionId);
        userNameToUser.put(sessionId, owner);
        Room newRoom = new Room(joinRequest.getRoomName(), owner);

        roomNameToRoom.put(joinRequest.getRoomName(), newRoom);
        sessionIdToRoom.put(sessionId, newRoom);

        return CREATE_SUCCESS;
    }

    public static int joinRoom(
            JoinRequest joinRequest,
            String sessionId) {

        if (joinRequest.hasEmptyEntry()) {
            return 1;
        }

        User joiner = new User(joinRequest.getUserName(),
                joinRequest.getUserLanguage(),
                sessionId);
        Room targetRoom = roomNameToRoom.get(joinRequest.getRoomName());

        if (targetRoom == null) {
            Console.log("No room has such name!");
            return ROOM_NOT_EXISTS;
        }

        userNameToUser.put(sessionId, joiner);

        if (targetRoom.joinUser(joiner)) {
            sessionIdToRoom.put(sessionId, targetRoom);
            return JOIN_SUCCESS;
        }

        return ROOM_IS_FULL;
    }

    public static AdvancedMessage systemMessage(int errorCode, String lang) {
        String errorMessage = null;
        switch(errorCode) {
            case ROOM_NAME_OCCUPIED:
                errorMessage = "This room name has been occupied.";
            case ROOM_NOT_EXISTS:
                errorMessage = "Join Failed. The room with the name doesn't exist.";
            case ROOM_IS_FULL:
                errorMessage = "Join Failed. The room is full.";
        }
        if (lang.compareTo("en") == 0) {
            try {
                errorMessage = API_access.translate(errorMessage, "en", lang);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new AdvancedMessage(
            errorMessage,
            SYSTEM_FLAG,
            errorCode,
            SYSTEM_NAME,
            TO_SENDER_FLAG
        );
    }

    public static String getSessionId(String pref, String postf) {
        return String.format("%s/%s", pref, postf);
    }

    /**
     * 
     * @param sessionId: String; sender's session ID
     * @return sessionIdList: String[]; session ID of users in the room
     * 
     * The first session ID is sender's, then other receiver in the room.
     *      
     */
    private static User[] getListeners(String sessionId) {
        Room room = sessionIdToRoom.get(sessionId);
        if (room == null) {
            return new User[0];
        }
        User[] userList = room.getListeners();
        // if the first is not sender:
        if (sessionId.compareTo(userList[0].getSessionId()) != 0) {
            User temp = userList[1];
            userList[1] = userList[0];
            userList[0] = temp;
        }
        return userList;
    }

    private static Pair[] getMessageList(String sessionId, Message inMessage) {
        User[] userList = getListeners(sessionId);
        User sender = userList[0];
        String senderName = sender.getName();
        Pair[] messageList = new Pair[userList.length];
        // COMMENT: deal with sender's message to sender
        messageList[0] = new Pair(
                userList[0],
                new AdvancedMessage(
                        inMessage.getContent(),
                        Manager.NON_SYSTEM_FLAG,
                        Manager.NORMAL_STATE,
                        senderName,
                        Manager.TO_SENDER_FLAG
                )
        );
        Console.log(messageList[0].toString());
        // COMMENT: deal with sender's message to receiver
        if (messageList.length > 1) {
            String outMessage = inMessage.getContent();
            // COMMENT: if not the same language, translate
            if (userList[0].getLanguage()
                    .compareTo(userList[1].getLanguage())
                    != 0
                ) {
                try {
                    outMessage = API_access.translate(
                            outMessage,
                            sender.getLanguage(),
                            userList[1].getLanguage());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            messageList[1] = new Pair(
                    userList[1],
                    new AdvancedMessage(
                            outMessage,
                            Manager.NON_SYSTEM_FLAG,
                            Manager.NORMAL_STATE,
                            senderName,
                            Manager.TO_RECEIVER_FLAG
                    )
            );
            Console.log(messageList[1].toString());

        }

        return messageList;
    }

    public static Pair[] getMessageList(String pref, String postf, Message inMessage) {
        return getMessageList(getSessionId(pref, postf), inMessage);
    }

    public static String findUserName(String sessionId) {
        return userNameToUser.get(sessionId).getName();
    }

    public static User removeUser(String pref, String postf) {
        String sessionId = getSessionId(pref, postf);
        userNameToUser.remove(sessionId);
        return removeUserFromRoom(sessionId);
    }

    private static User removeUserFromRoom(String sessionId) {
        Room room = sessionIdToRoom.get(sessionId);
        if (room == null) { return null; }
        boolean flag = room.removeUser(sessionId);
        sessionIdToRoom.remove(sessionId);
        if (flag) {
            Console.log("Remove room.");
            removeRoom(room);
            return null;
        }
        return room.getOwner();
    }

    private static void removeRoom(Room room) {
        String roomName = room.getName();
        roomNameToRoom.remove(roomName);
    }


}
