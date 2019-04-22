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

    public boolean joinUser(User joiner) {
        if (this.canJoin()) {
            this.joiner = joiner;
            return true;
        }
        return false;
    }

    public boolean userQuit(User quitUser) {
        // TODO
        return false;
    }

    public void sendMessage(String message) {
        // TODO: translate message to user's preffered language
        // TODO: send message to all users in the channel
    }

    public String[] getSessionIds() {
        String[] ids;
        if (this.joiner == null) {
            ids = new String[1];
            ids[0] = this.owner.getSessionId();
        } else {
            ids = new String[2];
            ids[0] = this.owner.getSessionId();
            ids[1] = this.joiner.getSessionId();
        }
        return ids;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Room{");
        sb.append("name='");
        sb.append(name);
        sb.append('\'');
        sb.append(", owner=");
        sb.append(owner.getName());
        sb.append(", joiner=");
        if (joiner == null) { sb.append("null"); }
        else { sb.append(joiner.getName()); }
        sb.append('}');

        return sb.toString();
    }
}
