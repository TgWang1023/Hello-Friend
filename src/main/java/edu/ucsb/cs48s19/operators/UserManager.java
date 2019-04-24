package edu.ucsb.cs48s19.operators;

public class UserManager {

    private UserManager() { }

    public static String getSessionId(String pref, String postf) {
        return String.format("%s/%s", pref, postf);
    }

    public static void removeUser(String pref, String postf) {
        String sessionId = getSessionId(pref, postf);
        RoomManager.removeUserFromRoom(sessionId);
    }


}
