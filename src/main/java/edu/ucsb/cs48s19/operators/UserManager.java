package edu.ucsb.cs48s19.operators;

public class UserManager {

    private UserManager() { }

    public static String getSessionId(String pref, String postf) {
        return String.format("%s__%s", pref, postf);
    }

}
