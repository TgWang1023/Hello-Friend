package edu.ucsb.cs48s19.templates;

public class User {
    private String name;
    private String language;
    private String sessionId;

    public User() { }

    public User(String name, String language, String sessionId) {
        this.name = name;
        this.language = language;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    boolean hasSessionId(String sessionId) {
        return this.sessionId.compareTo(sessionId) == 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
