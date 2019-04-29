package edu.ucsb.cs48s19.operators;

import edu.ucsb.cs48s19.templates.JoinRequest;
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

    private static String[] getListeners(String sessionId) {
        Room room = sessionIdToRoom.get(sessionId);
        if (room == null) {
            return new String[0];
        }
        return room.getSessionIds();
    }

    public static String[] getListeners(String pref, String postf) {
        return getListeners(getSessionId(pref, postf));
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
