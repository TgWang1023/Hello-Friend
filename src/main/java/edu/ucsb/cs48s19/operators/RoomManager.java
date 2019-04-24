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
        System.out.println(owner);
        Room newRoom = new Room(joinRequest.getRoomName(), owner);
        System.out.println(newRoom);

        roomNameToRoom.put(joinRequest.getRoomName(), newRoom);
        sessionIdToRoom.put(sessionId, newRoom);

        System.out.println(sessionIdToRoom);

        return true;
    }

    public static boolean joinRoom(
            JoinRequest joinRequest,
            String sessionId) {

        User joiner = new User(joinRequest.getUserName(),
                joinRequest.getUserLanguage(),
                sessionId);
        System.out.println(joiner);
        Room targetRoom = roomNameToRoom.get(joinRequest.getRoomName());

        if (targetRoom == null) {
            System.out.println("No name doesn't accord any room!");
            return false;
        }

        if (targetRoom.joinUser(joiner)) {
            System.out.println(targetRoom);
            sessionIdToRoom.put(sessionId, targetRoom);
            System.out.println(sessionIdToRoom);
            return true;
        }

        return false;
    }

    public static String[] getListeners(String sessionId) {
        Room room = sessionIdToRoom.get(sessionId);
        return room.getSessionIds();
    }

}
