package edu.ucsb.cs48s19.operators;

import edu.ucsb.cs48s19.templates.AdvancedMessage;
import edu.ucsb.cs48s19.templates.JoinRequest;
import edu.ucsb.cs48s19.templates.Pair;
import edu.ucsb.cs48s19.templates.Room;
import edu.ucsb.cs48s19.templates.User;

import java.util.HashMap;

public class RoomManager {

    // map room name to rooms
    private static HashMap<String, Room> roomNameToRoom = new HashMap<>();
    // map user's session ID to rooms
    private static HashMap<String, Room> sessionIdToRoom = new HashMap<>();

    private RoomManager() { }

    public static boolean createRoom(
            JoinRequest joinRequest,
            String sessionId) {

        if (roomNameToRoom.get(joinRequest.getRoomName()) != null) {
            System.out.println("The room name is occupied!");
            return false;
        }

        User owner = new User(joinRequest.getUserName(),
                joinRequest.getUserLanguage(),
                sessionId);
//        System.out.println(owner);
        Room newRoom = new Room(joinRequest.getRoomName(), owner);
//        System.out.println(newRoom);

        roomNameToRoom.put(joinRequest.getRoomName(), newRoom);
        sessionIdToRoom.put(sessionId, newRoom);

//        System.out.println(sessionIdToRoom);

        return true;
    }

    public static boolean joinRoom(
            JoinRequest joinRequest,
            String sessionId) {

        User joiner = new User(joinRequest.getUserName(),
                joinRequest.getUserLanguage(),
                sessionId);
//        System.out.println(joiner);
        Room targetRoom = roomNameToRoom.get(joinRequest.getRoomName());

        if (targetRoom == null) {
            System.out.println("No name doesn't accord any room!");
            return false;
        }

        if (targetRoom.joinUser(joiner)) {
//            System.out.println(targetRoom);
            sessionIdToRoom.put(sessionId, targetRoom);
//            System.out.println(sessionIdToRoom);
            return true;
        }

        return false;
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

    public static String[] getListeners(String pref, String postf) {
        return getListeners(getSessionId(pref, postf));
    }

    /**
     * Prepare (sessionId, AdvancedMessage) array and return to the controller
     * 
     * @param <AdvancedMessage>
     * @param sessionId
     * @return
     * 
     */
    private static Pair[] getMessageList(String sessionId) {
        return new Pair[1];
    }

    public static void removeUser(String pref, String postf) {
        String sessionId = getSessionId(pref, postf);
        RoomManager.removeUserFromRoom(sessionId);
    }

    private static void removeUserFromRoom(String sessionId) {
        Room room = sessionIdToRoom.get(sessionId);
        if (room == null) { return; }
        boolean flag = room.removeUser(sessionId);
        sessionIdToRoom.remove(sessionId);
        if (flag) {
            System.out.println("Remove room.");
            removeRoom(room);
        }
    }


}
