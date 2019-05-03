package edu.ucsb.cs48s19.operators;

public class Console {
    private static String LOG_PROMPT = "### SERVER LOG ### ";

    public static void log(String logMessage) {
        System.out.println(LOG_PROMPT + logMessage);
    }

    public static void log(Object o) {
        System.out.println(o.toString());
    }
}