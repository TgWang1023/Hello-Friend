package edu.ucsb.cs48s19.templates;


public class Room {
    private String name;
    private User owner;
    private User joiner;

    public Room() { }

    public Room(String name, User owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public User getOwner() {
        return owner;
    }

    public User getJoiner() {
        return joiner;
    }

    public boolean isFull() {
        return owner != null && joiner != null;
    }

    public boolean canJoin() {
        return joiner == null;
    }

    public void sendMessage(String message) {
        // TODO: translate message to user's preffered lang
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", owner=" + owner.getName() +
                ", joiner=" + joiner.getName() +
                '}';
    }
}
