package edu.ucsb.cs48s19.templates;

import java.util.Objects;

public class Pair {
    private User receiver;
    private AdvancedMessage message;


    public Pair() { }

    public Pair(User receiver, AdvancedMessage message) {
        this.receiver = receiver;
        this.message = message;
    }

    public User getReceiver() {
        return this.receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public AdvancedMessage getMessage() {
        return this.message;
    }

    public void setMessage(AdvancedMessage message) {
        this.message = message;
    }

    public Pair sessionId(User receiver) {
        this.receiver = receiver;
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
        return Objects.equals(receiver, pair.receiver) && Objects.equals(message, pair.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiver, message);
    }

    @Override
    public String toString() {
        return "{" +
            " receiver='" + getReceiver() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }

    public static void main(String[] args) {
        Pair p = new Pair(new User(), new AdvancedMessage());
        System.out.println(p);
    }

}