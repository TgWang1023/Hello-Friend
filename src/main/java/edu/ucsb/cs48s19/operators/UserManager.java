package edu.ucsb.cs48s19.operators;

public class UserManager {

    private UserManager() { }

    public static String getSessionId(String pref, String postf) {
        return String.format("%s__%s", pref, postf);
    }

    public static String getChannel(String sessionId) {
        String[] arr = sessionId.split("__");
        return String.format("/%s/%s", arr[0], arr[1]);
    }

}
