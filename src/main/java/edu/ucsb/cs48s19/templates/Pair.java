package edu.ucsb.cs48s19.templates;

import java.util.Objects;

public class Pair {
    private String sessionId;
    private AdvancedMessage message;


    public Pair() {
    }

    public Pair(String sessionId, AdvancedMessage message) {
        this.sessionId = sessionId;
        this.message = message;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public AdvancedMessage getMessage() {
        return this.message;
    }

    public void setMessage(AdvancedMessage message) {
        this.message = message;
    }

    public Pair sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public Pair message(AdvancedMessage message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) o;
        return Objects.equals(sessionId, pair.sessionId) && Objects.equals(message, pair.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, message);
    }

    @Override
    public String toString() {
        return "{" +
            " sessionId='" + getSessionId() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }

    public static void main(String[] args) {
        Pair p = new Pair("123", new AdvancedMessage());
        System.out.println(p);
    }

}