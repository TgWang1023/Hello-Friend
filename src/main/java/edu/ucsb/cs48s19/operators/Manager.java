package edu.ucsb.cs48s19.operators;

import edu.ucsb.cs48s19.templates.*;

import java.util.HashMap;

public class Manager {

    // CONSTANTS
    public static final int NORMAL_STATE = 0;
    public static final int ERROR_STATE = 1;

    public static final int CREATE_SUCCESS = 10;
    public static final int ROOM_NAME_OCCUPIED = 11;

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

    private static void removeRoom(Room room) {
        String roomName = room.getName();
        roomNameToRoom.remove(roomName);
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
    private static String[] getListeners(String sessionId) {
        Room room = sessionIdToRoom.get(sessionId);
        if (room == null) {
            return new String[0];
        }
        String[] sessionIdList = room.getSessionIds();
        // if the first is not sender:
        if (sessionId.compareTo(sessionIdList[0]) != 0) {
            sessionIdList[1] = sessionIdList[0];
            sessionIdList[0] = sessionId;
        }
        return sessionIdList;
    }

    private static Pair[] getMessageList(String sessionId, Message inMessage) {
        String[] sessionIdList = getListeners(sessionId);
        String senderName = userNameToUser.get(sessionId).getName();
        Pair[] messageList = new Pair[sessionIdList.length];
        // COMMENT: deal with sender's message to sender
        messageList[0] = new Pair(
                sessionIdList[0],
                new AdvancedMessage(
                        inMessage.getContent(),
                        Manager.NON_SYSTEM_FLAG,
                        Manager.NORMAL_STATE,
                        senderName,
                        Manager.TO_SENDER_FLAG
                )
        );
        // COMMENT: deal with sender's message to receiver
        if (messageList.length > 1) {
            messageList[1] = new Pair(
                    sessionIdList[1],
                    new AdvancedMessage(
                            inMessage.getContent(),
                            Manager.NON_SYSTEM_FLAG,
                            Manager.NORMAL_STATE,
                            senderName,
                            Manager.TO_RECEIVER_FLAG
                    )
            );
        }

        return messageList;
    }

    public static Pair[] getMessageList(String pref, String postf, Message inMessage) {
        return getMessageList(getSessionId(pref, postf), inMessage);
    }

    public static String findUserName(String sessionId) {
        return userNameToUser.get(sessionId).getName();
    }

    public static void removeUser(String pref, String postf) {
        String sessionId = getSessionId(pref, postf);
        userNameToUser.remove(sessionId);
        removeUserFromRoom(sessionId);
    }

    private static void removeUserFromRoom(String sessionId) {
        Room room = sessionIdToRoom.get(sessionId);
        if (room == null) { return; }
        boolean flag = room.removeUser(sessionId);
        sessionIdToRoom.remove(sessionId);
        if (flag) {
            Console.log("Remove room.");
            removeRoom(room);
        }
    }


}
